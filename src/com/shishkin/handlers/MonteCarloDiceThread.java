package com.shishkin.handlers;

import com.shishkin.models.Dice;
import com.shishkin.models.DiceGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonteCarloDiceThread implements Callable<Integer> {
    private static final int COUNT_DICES = 20;
    private static final int COUNT_FACES = 10;
    private static final int COUNT_BEST_ATTEMPTS = 10;
    private static final int POINT_LIMIT = 90;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final AtomicInteger progressedThreads;
    private final int countThreads;
    private static volatile MonteCarloDiceThread instance;

    private final int attempts;
    private final List<Dice> dices;


    private MonteCarloDiceThread(int attempts, int countThreads) {
        this.attempts = attempts;
        this.dices = DiceGenerator.generate(COUNT_DICES, COUNT_FACES);
        this.progressedThreads = new AtomicInteger(countThreads);
        this.countThreads = countThreads;
    }

    public static MonteCarloDiceThread getInstance(int attempts, int countThreads) {
        MonteCarloDiceThread localInstance = instance;

        if (localInstance == null) {
            synchronized (MonteCarloDiceThread.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MonteCarloDiceThread(attempts, countThreads);
                }
            }
        }

        return localInstance;
    }

    @Override
    public Integer call() {
        return flips(attempts);
    }

    private int flips(int attempts) {
        int countFavorableFlip = 0;
        int countOut = attempts / 10;

        for (int i = 0; i < attempts; i++) {
            if (isFavorableFlip()) {
                countFavorableFlip++;
            }
            if (i % countOut == 0 && i != 0) {
                this.progressedThreads.decrementAndGet();
                lock.lock();
                try {
                    System.out.printf("Thread-%d signal! %n", Thread.currentThread().threadId());
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }

        return countFavorableFlip;
    }

    private boolean isFavorableFlip() {
        return getSumBestAttempts() > POINT_LIMIT;
    }

    private int getSumBestAttempts() {
        return flipDices().stream().sorted(Comparator.reverseOrder())
                .limit(COUNT_BEST_ATTEMPTS)
                .mapToInt(Integer::valueOf).sum();
    }


    private List<Integer> flipDices() {
        return dices.stream().map(this::flip).toList();
    }

    private int flip(Dice dice) {
        int flipResult = dice.flip();
        int result = flipResult;
        if (flipResult == 10) result = flipResult + flip(dice);
        return result;
    }

    public AtomicInteger getProgressedThreads() {
        return progressedThreads;
    }

    public int getCountThreads() {
        return countThreads;
    }

    public Lock getLock() {
        return lock;
    }

    public Condition getCondition() {
        return condition;
    }
}
