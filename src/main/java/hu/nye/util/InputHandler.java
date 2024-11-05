package hu.nye.util;

import java.util.Scanner;

public class InputHandler {
    private final Scanner inputScanner;

    public InputHandler(Scanner scanner) {
        this.inputScanner = scanner;
    }

    public int getPlayerMove(int columnRange) {
        int col = -1;
        while (true) {
            try {
                System.out.print("Enter column (0-" + columnRange + ") to make your move: ");
                col = inputScanner.nextInt();
                if (col < 0 || col > columnRange) {
                    System.out.println("Invalid column. Please enter a number between 0 and " + columnRange + ".");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                inputScanner.next(); // Clear invalid input
            }
        }
        return col;
    }
}
