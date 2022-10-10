package com.shishkin.handlers;

import com.shishkin.models.Dice;
import com.shishkin.models.DiceGenerator;

import java.util.Comparator;
import java.util.List;

public class MonteCarloDiceThread extends Thread {
    private static final int COUNT_DICES = 20;
    private static final int COUNT_FACES = 10;
    private static final int COUNT_BEST_ATTEMPTS = 10;
    private static final int POINT_LIMIT = 90;
    private final int attempts;
    private final List<Dice> dices;

    private final int id;

    public MonteCarloDiceThread(int id, double eps) {
        this.attempts = (int) (1 / eps);
        this.dices = DiceGenerator.generate(COUNT_DICES, COUNT_FACES);
        this.id = id;
    }

    public int getThreadId() {
        return id;
    }

    @Override
    public void run() {
        int countFavorableFlip = 0;

        for (int i = 0; i < attempts; i++) {
            if (isFavorableFlip()) {
                countFavorableFlip++;
            }

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted: " + Thread.currentThread().getName());
                return;
            }
        }
        System.out.println(((double) countFavorableFlip) / attempts);
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
