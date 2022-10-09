package com.shishkin;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        long startTime;
        System.out.println("#running");

        MonteCarloDiceCalculator monteCarloDiceController = new MonteCarloDiceCalculator(1e-7);

        startTime = System.currentTimeMillis();

        Runnable runnable = () -> System.out.println("Probability: " + monteCarloDiceController.calculateProbability());
        Thread t = new Thread(runnable);
        t.start();

        t.join(TimeUnit.MINUTES.toMillis(10));
        System.out.println((System.currentTimeMillis() - startTime) + " ms");
    }
}
