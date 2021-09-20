package mb.common.concurrent.lock;

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

    public LockHandle lockWrite() {
        return LockHandle.lock(super.writeLock());
    }

    public LockHandle lockInterruptiblyWrite() throws InterruptedException {
        return LockHandle.lockInterruptibly(super.writeLock());
    }
}
