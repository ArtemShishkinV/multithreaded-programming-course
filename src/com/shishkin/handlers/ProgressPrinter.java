package com.shishkin.handlers;

public class ProgressPrinter implements Runnable {
    private final MonteCarloDiceThread monteCarloDiceThread;
    private static volatile ProgressPrinter instance;


    private ProgressPrinter(MonteCarloDiceThread monteCarloDiceThread) {
        this.monteCarloDiceThread = monteCarloDiceThread;
    }

    public static ProgressPrinter getInstance(MonteCarloDiceThread monteCarloDiceThread) {
        ProgressPrinter localInstance = instance;

        if (localInstance == null) {
            synchronized (ProgressPrinter.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProgressPrinter(monteCarloDiceThread);
                }
            }
        }

        return localInstance;
    }

    @Override
    public void run() {
        for (int i = 10; i < 100 + 1; i += 10) {
            monteCarloDiceThread.getLock().lock();
            try {
                while (!(monteCarloDiceThread.getProgressedThreads()
                        .compareAndSet(0, monteCarloDiceThread.getCountThreads()))) {
                    monteCarloDiceThread.getCondition().await();
                }
                System.out.printf("Task completed %d %% %n", i);
                monteCarloDiceThread.getCondition().signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                monteCarloDiceThread.getLock().unlock();
            }
        }
    }
}
