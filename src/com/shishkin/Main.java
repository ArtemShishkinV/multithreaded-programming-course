package com.shishkin;

import com.shishkin.controllers.MonteCarloDiceController;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("#running");

        try (Scanner scanner = new Scanner(System.in)) {
            MonteCarloDiceController controller = new MonteCarloDiceController(scanner);
            controller.start();
        }

    }
}
