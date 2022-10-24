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


    private static final MonteCarloDiceThread INSTANCE = new MonteCarloDiceThread(0.000000065);

    private int countFavorableFlip = 0;
    private int attempts;

    private final List<Dice> dices;

    private MonteCarloDiceThread(double eps) {
        this.attempts = (int) (1 / eps);
        this.dices = DiceGenerator.generate(COUNT_DICES, COUNT_FACES);
    }

    public static MonteCarloDiceThread getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {
        int oldAttempts = attempts;

        if (attempts <= 1_000_000) {
            flips(attempts);
        } else {
            attempts = attempts / 3;
            System.out.println("Attempts in thread: " + attempts);

            Thread t1 = new Thread(() -> flips(attempts));
            Thread t2 = new Thread(() -> flips(attempts));
            Thread t3 = new Thread(() -> flips(attempts));

            t1.start();
            t2.start();
            t3.start();

            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                attempts = oldAttempts;
            }
        }

        System.out.println("Good flips: " + countFavorableFlip +
                " of " + attempts + " attempts");
        System.out.println(((double) countFavorableFlip) / attempts);
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
