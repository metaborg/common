package mb.common.concurrent.lock;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CloseableReentrantReadWriteLock extends ReentrantReadWriteLock {
    public CloseableReentrantReadWriteLock() {
        super();
    }

    public CloseableReentrantReadWriteLock(boolean fair) {
        super(fair);
    }


    public LockHandle lockRead() {
        return LockHandle.lock(super.readLock());
    }

    public LockHandle lockInterruptiblyRead() throws InterruptedException {
        return LockHandle.lockInterruptibly(super.readLock());
    }

    public Optional<LockHandle> tryLockRead() {
        return LockHandle.tryLock(super.readLock());
    }

    public Optional<LockHandle> tryLockRead(long time, TimeUnit unit) throws InterruptedException {
        return LockHandle.tryLock(super.readLock(), time, unit);
    }


    public LockHandle lockWrite() {
        return LockHandle.lock(super.writeLock());
    }

    public LockHandle lockInterruptiblyWrite() throws InterruptedException {
        return LockHandle.lockInterruptibly(super.writeLock());
    }

    public Optional<LockHandle> tryLockWrite() {
        return LockHandle.tryLock(super.writeLock());
    }

    public Optional<LockHandle> tryLockWrite(long time, TimeUnit unit) throws InterruptedException {
        return LockHandle.tryLock(super.writeLock(), time, unit);
    }
}
