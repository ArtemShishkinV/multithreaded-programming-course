package com.shishkin.handlers;

import com.shishkin.models.Dice;
import com.shishkin.models.DiceGenerator;

import java.util.Comparator;
import java.util.List;

public final class MonteCarloDiceThread implements Runnable {
    private static final int COUNT_DICES = 20;
    private static final int COUNT_FACES = 10;
    private static final int COUNT_BEST_ATTEMPTS = 10;
    private static final int POINT_LIMIT = 90;
    private static int id = 0;

    private static volatile MonteCarloDiceThread instance;
    private int countFavorableFlip = 0;

    private final int attempts;
    private final List<Dice> dices;

    private MonteCarloDiceThread(int attempts) {
        this.attempts = attempts;
        this.dices = DiceGenerator.generate(COUNT_DICES, COUNT_FACES);
        id++;
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

    public int getThreadId() {
        return id;
    }

    @Override
    public void run() {
//        System.out.println("Thread with id " + getThreadId());
        flips(attempts);
    }

    public int getCountFavorableFlip() {
        return countFavorableFlip;
    }

    private void flips(int attempts) {
        for (int i = 0; i < attempts; i++) {
            if (isFavorableFlip()) {
                synchronized (this) {
                    countFavorableFlip++;
                }
            }
        }
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
