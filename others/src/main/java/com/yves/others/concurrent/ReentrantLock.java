package com.yves.others.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class ReentrantLock implements Lock, java.io.Serializable {

    private final Sync sync;

    public ReentrantLock() {
        sync = new NonfairSync();
    }

    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    public void lock() {
        sync.lock();
    }

    @Override
    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.nonfairTryAcquire(1);
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    abstract static class Sync extends AbstractQueuedSynchronizer {
        abstract void lock();

        //非公平重入锁
        final boolean nonfairTryAcquire(int acquires) {
            Thread current = Thread.currentThread();
            int s = getState();
            if (s == 0) {
                if (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int nextState = getState() + acquires;
                if (nextState < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextState);
                return true;
            }
            return false;
        }

        protected final boolean tryRelease(int releases) {
            Thread current = Thread.currentThread();
            int s = getState() - releases;
            if (current != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            boolean release = false;
            if (s == 0) {
                release = true;
                setExclusiveOwnerThread(null);
            }
            setState(s);

            return release;
        }


        protected final boolean isHeldExclusively() {
            // While we must in general read state before owner,
            // we don't need to do so to check if current thread is owner
            return getExclusiveOwnerThread() == Thread.currentThread();
        }
    }


    //非公平锁 老的线程排队使用锁，新线程仍然抢占使用锁。
    static final class NonfairSync extends Sync {
        final void lock() {
            // 新的线程可能抢占已经排队的线程的锁的使用权
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
            } else {
                acquire(1);
            }
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }



    }

    //公平锁 先到先得,如果未获取到锁,构造Node addWaiter加到等待队列,等待顺序唤醒
    static final class FairSync extends Sync {
        @Override
        void lock() {
            acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                //当前线程未入队!hasQueuedPredecessors()保证了不论是新的线程还是已经排队的线程都顺序使用锁
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }


}
