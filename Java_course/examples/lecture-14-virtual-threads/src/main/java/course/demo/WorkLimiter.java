package course.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

final class WorkLimiter {
    private final Semaphore permits;
    private final AtomicInteger active = new AtomicInteger();
    private final AtomicInteger peak = new AtomicInteger();

    WorkLimiter(int limit) {
        if (limit < 1) throw new IllegalArgumentException("limit must be positive");
        permits = new Semaphore(limit);
    }

    <T> T call(Callable<T> operation) throws Exception {
        permits.acquire();
        int current = active.incrementAndGet();
        peak.accumulateAndGet(current, Math::max);
        try {
            return operation.call();
        } finally {
            active.decrementAndGet();
            permits.release();
        }
    }

    int peakConcurrency() { return peak.get(); }
}
