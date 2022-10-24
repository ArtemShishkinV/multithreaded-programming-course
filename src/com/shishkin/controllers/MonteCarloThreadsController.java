package com.shishkin.controllers;

import com.shishkin.handlers.MonteCarloDiceThread;

import java.util.concurrent.TimeUnit;

public class MonteCarloThreadsController {
    private MonteCarloThreadsController() {
    }

    public static void start() {
        MonteCarloDiceThread monteCarloDiceThread = MonteCarloDiceThread.getInstance();
        Thread t1 = new Thread(monteCarloDiceThread);
        t1.start();
        try {
            t1.join(TimeUnit.MINUTES.toMillis(2));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
