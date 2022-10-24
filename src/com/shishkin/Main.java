package com.shishkin;

import com.shishkin.controllers.MonteCarloThreadsController;

public class Main {

    public static void main(String[] args) {
        System.out.println("#running");

        long start = System.currentTimeMillis();

        MonteCarloThreadsController.start();

        long finish = System.currentTimeMillis();

        System.out.println("Time: " + (finish - start) + " ms.");

    }
}
