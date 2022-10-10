package com.shishkin.controllers;

import com.shishkin.services.MonteCarloDiceService;
import com.shishkin.views.ConsoleView;
import com.shishkin.views.types.MenuOption;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MonteCarloDiceController {
    private final ConsoleView consoleView;
    private final Scanner scanner;
    private final MonteCarloDiceService monteCarloDiceService;

    public MonteCarloDiceController(Scanner scanner) {
        this.consoleView = new ConsoleView();
        this.scanner = scanner;
        this.monteCarloDiceService = new MonteCarloDiceService();
    }


    public void start() {
        String option;
        boolean isProceedExecution = true;
        while (isProceedExecution) {
            consoleView.outMainMenu();
            option = consoleView.selectMenuItem(scanner);
            isProceedExecution = selectOption(option);
        }
    }

    private boolean selectOption(String option) {
        String[] strings = option.toLowerCase(Locale.ROOT).trim().split("\\s+");
        MenuOption selectOption = MenuOption.NONE.findByValue(strings[0]);

        try {
            switch (selectOption) {
                case START -> System.out.println("Task ID: " + monteCarloDiceService.start(strings[1]) + " is running");
                case STOP -> monteCarloDiceService.stop(strings[1]);
                case AWAIT -> monteCarloDiceService.await(strings[1]);
                case EXIT -> {
                    monteCarloDiceService.exit();
                    return false;
                }
                default -> System.out.println("Invalid input");
            }
        } catch (NoSuchElementException e) {
            System.out.println("Thread this id: " + strings[1] + " not found.");
        }

        return true;
    }
}
