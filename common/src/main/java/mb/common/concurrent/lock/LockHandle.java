package mb.common.concurrent.lock;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class LockHandle implements AutoCloseable {
    private final Lock lock;

    private LockHandle(Lock lock) {
        this.lock = lock;
    }

    @Override public void close() {
        lock.unlock();
    }


    public static LockHandle lock(Lock lock) {
        lock.lock();
        return new LockHandle(lock);
    }

    public static LockHandle lockInterruptibly(Lock lock) throws InterruptedException {
        lock.lockInterruptibly();
        return new LockHandle(lock);
    }

    public static Optional<LockHandle> tryLock(Lock lock) {
        if(lock.tryLock()) {
            return Optional.of(new LockHandle(lock));
        }
        return Optional.empty();
    }

    public static Optional<LockHandle> tryLock(Lock lock, long time, TimeUnit unit) throws InterruptedException {
        if(lock.tryLock(time, unit)) {
            return Optional.of(new LockHandle(lock));
        }
        return Optional.empty();
    }
}
