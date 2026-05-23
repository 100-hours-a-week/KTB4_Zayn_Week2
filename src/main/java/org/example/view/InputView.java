package org.example.view;

import org.example.common.ErrorMessage;
import org.example.common.ViewMessage;

import java.util.Scanner;

public class InputView {
    private final Scanner sc = new Scanner(System.in);

    public void pressAnyKey() {
        sc.nextLine();
    }

    public void close() {
        sc.close();
    }

    public int userInput() {
        System.out.print(ViewMessage.USER_INPUT_FORMAT.getMessage());
        return validateUserInput(sc.nextLine().trim()); // List<FootballTeam>의 index
    }

    private int validateUserInput(String input) {
        if (input == null || input.isBlank())
            throw new IllegalArgumentException(ErrorMessage.IS_NULL_OR_IS_EMPTY.getMessage());

        return parseAndValidateDigit(input);
    }

    private int parseAndValidateDigit(String s) {
        try {
            return Integer.parseInt(s) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_NUMBER_FORMAT.getMessage());
        }
    }
}