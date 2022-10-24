package com.shishkin.controllers;

import com.shishkin.handlers.MonteCarloDiceThread;

public class MonteCarloThreadsController {
    private final int attempts;

    public MonteCarloThreadsController(double eps) {
        this.attempts = (int) (1 / eps);
    }

    public double calculateProbability() {
        MonteCarloDiceThread monteCarloDiceThread;

        if (attempts <= 1_000_000) {
            monteCarloDiceThread = MonteCarloDiceThread.getInstance(attempts);
            monteCarloDiceThread.run();
        } else {
            int attemptsInThread = attempts / 3;
            System.out.println("Attempts in thread: " + attemptsInThread);

            monteCarloDiceThread = MonteCarloDiceThread.getInstance(attemptsInThread);

            Thread t1 = new Thread(monteCarloDiceThread);
            Thread t2 = new Thread(monteCarloDiceThread);
            Thread t3 = new Thread(monteCarloDiceThread);

            t1.start();
            t2.start();
            t3.start();

            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Good flips: " + monteCarloDiceThread.getCountFavorableFlip() +
                " of " + attempts + " attempts");
        return ((double) monteCarloDiceThread.getCountFavorableFlip()) / attempts;
    }
}
