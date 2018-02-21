package org.jhaws.common.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DummyLock implements Lock {
    public void lock() {
        //
    }

    public void lockInterruptibly() {
        //
    }

    public boolean tryLock() {
        return true;
    }

    public boolean tryLock(long time, TimeUnit unit) {
        return true;
    }

    public void unlock() {
        //
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}
