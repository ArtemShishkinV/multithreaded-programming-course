package com.shishkin.handlers;

import com.shishkin.models.Dice;
import com.shishkin.models.DiceGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

public class MonteCarloDiceThread implements Callable<Integer> {
    private static final int COUNT_DICES = 20;
    private static final int COUNT_FACES = 10;
    private static final int COUNT_BEST_ATTEMPTS = 10;
    private static final int POINT_LIMIT = 90;

    private static volatile MonteCarloDiceThread instance;

    private static final Semaphore SEMAPHORE = new Semaphore(2, true);

    private final int attempts;
    private final List<Dice> dices;


    private MonteCarloDiceThread(int attempts) {
        this.attempts = attempts;
        this.dices = DiceGenerator.generate(COUNT_DICES, COUNT_FACES);
    }

    public static MonteCarloDiceThread getInstance(int attempts) {
        MonteCarloDiceThread localInstance = instance;

        if (localInstance == null) {
            synchronized (MonteCarloDiceThread.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MonteCarloDiceThread(attempts);
                }
            }
        }

        return localInstance;
    }

    @Override
    public Integer call() {
        int result = 0;
        try {
            SEMAPHORE.acquire();
            result = flips(attempts);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            SEMAPHORE.release();
        }
        return result;
    }

    private int flips(int attempts) {
        int countFavorableFlip = 0;
        int countOut = attempts / 10;
        int attemptsDivide = attempts / 100;
        Long id = Thread.currentThread().threadId();

        for (int i = 0; i < attempts; i++) {
            if (isFavorableFlip()) {
                countFavorableFlip++;
            }
            if (i % countOut == 0) System.out.printf("Thread-%d, completed %d %% %n", id, i / attemptsDivide);
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
}
