package com.shishkin.controllers;

import com.shishkin.handlers.MonteCarloDiceThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MonteCarloThreadsController {
    private static final int COUNT_CPU = 4;
    private final int attempts;



    public MonteCarloThreadsController(double eps) {
        this.attempts = (int) (1 / eps);
    }

    public double calculateProbability() throws Exception {
        int countFavorableFlip;
        int countThread;

        if (attempts <= 1_000_000) {
            countFavorableFlip = getCountFavorableFlipSequential();
        } else if (attempts <= 4_000_000) {
            countThread = 2;
            countFavorableFlip = getCountFavorableFlipParallel(countThread);
        } else if (attempts <= 18_000_000) {
            countThread = 3;
            countFavorableFlip = getCountFavorableFlipParallel(countThread);
        } else {
            countFavorableFlip = getCountFavorableFlipParallel(COUNT_CPU);
        }

        System.out.println("Good flips: " + countFavorableFlip +
                " of " + attempts + " attempts");
        return ((double) countFavorableFlip) / attempts;
    }

    private int getCountFavorableFlipSequential() {
        MonteCarloDiceThread monteCarloDiceThread = MonteCarloDiceThread.getInstance(attempts);
        return monteCarloDiceThread.call();
    }

    private int getCountFavorableFlipParallel(int countThreads) throws ExecutionException, InterruptedException {
        List<Future<Integer>> results = new ArrayList<>();
        int attemptsInTask = this.attempts / countThreads;
        int countFavorableFlip = 0;

        try (ExecutorService executors = Executors.newFixedThreadPool(countThreads)) {
            System.out.println("Attempts in thread: " + attemptsInTask);

            MonteCarloDiceThread monteCarloDiceThread = MonteCarloDiceThread.getInstance(attemptsInTask);

            for(int i = 0; i < countThreads; i++) {
                results.add(executors.submit(monteCarloDiceThread));
            }
        }

        for (Future<Integer> result:
             results) {
            countFavorableFlip += result.get();
        }

        return countFavorableFlip;
    }
}
