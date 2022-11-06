package com.shishkin.models;

public class CountDownLatchImpl {
    private volatile int count = 0;

    public CountDownLatchImpl(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.count = count;
    }


    public synchronized void await() throws InterruptedException {
        while (this.count > 0) {
            this.wait();
        }
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
