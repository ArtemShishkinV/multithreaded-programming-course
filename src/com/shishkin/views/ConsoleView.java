package com.shishkin.views;

import java.util.Scanner;

public class ConsoleView {

    public void outMainMenu() {
        System.out.printf("Main menu.%n %s %n %s %n %s %n %s %n",
                "1. start <n>",
                "2. stop <n>",
                "3. await <n>",
                "4. exit");
    }

    public String selectMenuItem(Scanner scanner) {
        System.out.println("Select option.");
        if(scanner.hasNextLine()) return scanner.nextLine();
        return "";
    }

}
