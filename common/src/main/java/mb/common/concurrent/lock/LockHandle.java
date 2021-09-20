package mb.common.concurrent.lock;

import java.util.concurrent.locks.Lock;

public class LockHandle implements AutoCloseable {
    private final Lock lock;

    private LockHandle(Lock lock) {
        this.lock = lock;
    }

    public static LockHandle lock(Lock lock) {
        lock.lock();
        return new LockHandle(lock);
    }

    public static LockHandle lockInterruptibly(Lock lock) throws InterruptedException {
        lock.lockInterruptibly();
        return new LockHandle(lock);
    }

    @Override public void close() {
        lock.unlock();
    }
}
