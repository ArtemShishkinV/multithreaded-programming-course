package com.shishkin;

import com.shishkin.controllers.MonteCarloThreadsController;

public class Main {

    public static void main(String[] args) {
        System.out.println("#running");

        long start = System.currentTimeMillis();

        MonteCarloThreadsController controller = new MonteCarloThreadsController(0.000000065);
        double result = controller.calculateProbability();

        long finish = System.currentTimeMillis();

        System.out.println("Probability: " + result);
        System.out.println("Time: " + (finish - start) + " ms.");

    }
}
