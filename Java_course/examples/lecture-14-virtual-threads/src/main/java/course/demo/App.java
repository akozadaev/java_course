package course.demo;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws Exception {
        var limiter = new WorkLimiter(4);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = new ArrayList<java.util.concurrent.Future<String>>();
            for (int i = 0; i < 20; i++) {
                int request = i;
                futures.add(executor.submit(
                        () -> limiter.call(() -> "request-" + request)));
            }
            for (var future : futures) {
                System.out.println(future.get());
            }
        }
        System.out.println("peak concurrency = " + limiter.peakConcurrency());
    }
}
