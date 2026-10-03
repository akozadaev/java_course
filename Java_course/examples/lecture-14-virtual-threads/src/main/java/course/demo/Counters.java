package course.demo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

final class Counters {
    private Counters() {
    }

    static Counter synchronizedCounter() {
        return new Counter() {
            private int value;

            public synchronized void increment() { value++; }
            public synchronized int value() { return value; }
        };
    }

    static Counter lockedCounter() {
        return new Counter() {
            private final ReentrantLock lock = new ReentrantLock();
            private int value;

            public void increment() {
                lock.lock();
                try { value++; } finally { lock.unlock(); }
            }

            public int value() {
                lock.lock();
                try { return value; } finally { lock.unlock(); }
            }
        };
    }

    static Counter atomicCounter() {
        return new Counter() {
            private final AtomicInteger value = new AtomicInteger();
            public void increment() { value.incrementAndGet(); }
            public int value() { return value.get(); }
        };
    }
}
