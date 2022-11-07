package com.shishkin.models;

import java.util.concurrent.TimeUnit;

public class CountDownLatchImpl {
    private volatile int count;

    public CountDownLatchImpl(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.count = count;
    }


    public synchronized void await() throws InterruptedException {
        while (this.count > 0) {
            this.wait();
        }
    }

    public synchronized boolean tryAwait(long timeout, TimeUnit unit) throws InterruptedException {
        long timeoutEnd = System.currentTimeMillis() + unit.toMillis(timeout);
        while(count > 0) {
            long startWait = System.currentTimeMillis();
            this.wait(timeoutEnd - startWait);
            if (System.currentTimeMillis() >= timeoutEnd) {
                break;
            }
        }
        return count == 0;
    }

    public synchronized void countDown() {
        this.count--;
        if (this.count == 0) {
            this.notifyAll();
        }
    }

    public int getCount() {
        return count;
    }
}
