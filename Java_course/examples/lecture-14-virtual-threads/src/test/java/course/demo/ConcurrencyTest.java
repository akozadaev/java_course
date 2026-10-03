package course.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ConcurrencyTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    @Test
    void unsafeReadModifyWriteDeterministicallyLosesUpdate() throws Exception {
        var bothRead = new CountDownLatch(2);
        var mayWrite = new CountDownLatch(1);
        var finished = new CountDownLatch(2);
        int[] value = {0};
        Runnable increment = () -> {
            int observed = value[0];
            bothRead.countDown();
            await(mayWrite);
            value[0] = observed + 1;
            finished.countDown();
        };

        Thread.ofVirtual().start(increment);
        Thread.ofVirtual().start(increment);
        assertTrue(bothRead.await(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS));
        mayWrite.countDown();
        assertTrue(finished.await(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS));
        assertEquals(1, value[0], "both tasks overwrite the same observed value");
    }

    static Stream<Counter> safeCounters() {
        return Stream.of(Counters.synchronizedCounter(), Counters.lockedCounter(),
                Counters.atomicCounter());
    }

    @ParameterizedTest
    @MethodSource("safeCounters")
    void safeCounterDoesNotLoseUpdates(Counter counter) throws Exception {
        int tasks = 1_000;
        var start = new CountDownLatch(1);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < tasks; i++) {
                futures.add(executor.submit(() -> {
                    await(start);
                    counter.increment();
                }));
            }
            start.countDown();
            for (var future : futures) future.get();
        }
        assertEquals(tasks, counter.value());
    }

    @Test
    void futureGetMakesTaskFailureObservable() throws Exception {
        var attempted = new CountDownLatch(1);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> future = executor.submit(() -> {
                attempted.countDown();
                throw new IllegalStateException("remote service failed");
            });
            assertTrue(attempted.await(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS));
            var failure = assertThrows(ExecutionException.class, future::get);
            var cause = assertInstanceOf(IllegalStateException.class, failure.getCause());
            assertEquals("remote service failed", cause.getMessage());
        }
    }

    @Test
    void cancellationInterruptsStartedTask() throws Exception {
        var started = new CountDownLatch(1);
        var interrupted = new CountDownLatch(1);
        var blocker = new CountDownLatch(1);
        var interruptStatusRestored = new AtomicBoolean();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<?> future = executor.submit(() -> {
                started.countDown();
                try {
                    blocker.await();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    interruptStatusRestored.set(Thread.currentThread().isInterrupted());
                    interrupted.countDown();
                }
            });
            assertTrue(started.await(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS));
            assertTrue(future.cancel(true));
            assertTrue(interrupted.await(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS));
            assertTrue(interruptStatusRestored.get());
            assertThrows(CancellationException.class, future::get);
        }
    }

    @Test
    void semaphoreLimitsLoadWithoutTimingAssumptions() throws Exception {
        int limit = 3;
        int tasks = 100;
        var firstWaveEntered = new CountDownLatch(limit);
        var releaseFirstWave = new CountDownLatch(1);
        var limiter = new WorkLimiter(limit);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Integer>> futures = new ArrayList<>();
            for (int i = 0; i < tasks; i++) {
                int result = i;
                futures.add(executor.submit(() -> limiter.call(() -> {
                    firstWaveEntered.countDown();
                    if (releaseFirstWave.getCount() > 0) releaseFirstWave.await();
                    return result;
                })));
            }
            assertTrue(firstWaveEntered.await(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS));
            assertEquals(limit, limiter.peakConcurrency());
            releaseFirstWave.countDown();
            for (int i = 0; i < tasks; i++) assertEquals(i, futures.get(i).get());
        }
        assertEquals(limit, limiter.peakConcurrency());
    }

    private static void await(CountDownLatch latch) {
        try {
            if (!latch.await(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS))
                throw new AssertionError("synchronization timed out");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError("test worker interrupted", exception);
        }
    }
}
