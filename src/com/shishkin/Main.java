package com.shishkin;

import com.shishkin.controllers.MonteCarloThreadsController;

public class Main {

    public static void main(String[] args) {
        System.out.println("#running");
        double result = 0.0;
        long start = System.currentTimeMillis();

        MonteCarloThreadsController controller = new MonteCarloThreadsController(0.0000001);

        try {
            result = controller.calculateProbability();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        long finish = System.currentTimeMillis();

        System.out.println("Probability: " + result);
        System.out.println("Time: " + (finish - start) + " ms.");

    }
}
