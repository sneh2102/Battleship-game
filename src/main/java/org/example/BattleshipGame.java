package org.example;

import java.util.Random;
import java.util.Scanner;

enum ElementType {
    EMPTY, SHIP, GRENADE
}

enum Owner {
    NONE, USER, COMPUTER
}

public class BattleshipGame {
    private static final int BOARD_SIZE = 8;

    private final Grid grid;
    private final Scanner scanner;
    private final Random random;

    public BattleshipGame() {
        grid = new Grid(BOARD_SIZE);
        scanner = new Scanner(System.in);
        random = new Random();

        initializeBoard();
        placeUserShipsAndGrenades();
        placeComputerShipsAndGrenades();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                grid.setElement(i, j, new GridElement());
            }
        }
    }

    private void placeUserShipsAndGrenades() {
        System.out.println("Welcome to Battleship! Let's set up the board.");

        for (int i = 0; i < 6; i++) {
            System.out.println("Enter coordinates for your ship #" + (i + 1) + ": ");
            placeElement(ElementType.SHIP, Owner.USER);
        }

        for (int i = 0; i < 4; i++) {
            System.out.println("Enter coordinates for your grenade #" + (i + 1) + ": ");
            placeElement(ElementType.GRENADE, Owner.USER);
        }
    }

    private void placeComputerShipsAndGrenades() {
        System.out.println("Computer is setting up its board...");

        for (int i = 0; i < 6; i++) {
            placeElementRandomly(ElementType.SHIP, Owner.COMPUTER);
        }

        for (int i = 0; i < 4; i++) {
            placeElementRandomly(ElementType.GRENADE, Owner.COMPUTER);
        }
    }

    private void placeElement(ElementType type, Owner owner) {
        while (true) {
            String coordinates = scanner.nextLine().toUpperCase();
            int col = coordinates.charAt(0) - 'A';
            int row = coordinates.charAt(1) - '1';

            if (grid.isValidCoordinate(row, col)) {
                GridElement element = grid.getElement(row, col);
                if (element.getType() == ElementType.EMPTY) {
                    element.setType(type);
                    element.setOwner(owner);
                    break;
                } else {
                    System.out.println("Invalid coordinates or position already occupied. Try again.");
                }
            } else {
                System.out.println("Invalid coordinates. Try again.");
            }
        }
    }

    private void placeElementRandomly(ElementType type, Owner owner) {
        while (true) {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);

            GridElement element = grid.getElement(row, col);
            if (element.getType() == ElementType.EMPTY) {
                element.setType(type);
                element.setOwner(owner);
                break;
            }
        }
    }

    public void play() {
        while (true) {
            userTurn();
            printBoard();
            if (isGameOver()) {
                System.out.println("Congratulations! You sunk all ships. You win!");
                break;
            }

            computerTurn();
            printBoard();
            if (isGameOver()) {
                System.out.println("Oh no! All your ships are sunk. You lose!");
                break;
            }
        }
        scanner.close();
    }

    private void userTurn() {
        System.out.println("Your turn! Enter coordinates to shoot (e.g., B3): ");
        while (true) {
            String coordinates = scanner.nextLine().toUpperCase();
            int col = coordinates.charAt(0) - 'A';
            int row = coordinates.charAt(1) - '1';

            if (grid.isValidCoordinate(row, col)) {
                GridElement element = grid.getElement(row, col);
                if (!element.isCalled()) {
                    element.setCalled(true);
                    if (element.getType() == ElementType.SHIP && element.getOwner() == Owner.COMPUTER) {
                        System.out.println("Hit! Computer's ship at " + coordinates + " sunk.");
                    } else if (element.getType() == ElementType.GRENADE && element.getOwner() == Owner.COMPUTER) {
                        System.out.println("Boom! You hit a grenade. Your next turn is skipped.");
                        computerTurn();
                    } else if (element.getType() == ElementType.GRENADE && element.getOwner() == Owner.USER) {
                        System.out.println("Boom! You hit your own grenade at " + (char) ('A' + col) + (row + 1) + ". Computer's next turn is skipped.");
                        userTurn();
                    } else if (element.getType() == ElementType.SHIP && element.getOwner() == Owner.USER) {
                        System.out.println("Ops! You hit your own ship at " + coordinates);
                    } else {
                        System.out.println("Miss! No ship or grenade here.");
                    }
                    break;
                } else {
                    System.out.println("Coordinates already used. Try again.");
                }
            } else {
                System.out.println("Invalid coordinates. Try again.");
            }
        }
    }

    private void computerTurn() {
        System.out.println("Computer's turn...");
        int col, row;
        while (true) {
            col = random.nextInt(BOARD_SIZE);
            row = random.nextInt(BOARD_SIZE);
            GridElement element = grid.getElement(row, col);
            if (!element.isCalled()) {
                element.setCalled(true);
                break;
            }
        }

        GridElement element = grid.getElement(row, col);
        if (element.getType() == ElementType.SHIP && element.getOwner() == Owner.USER) {
            System.out.println("Computer hit your ship at " + (char) ('A' + col) + (row + 1));
        } else if (element.getType() == ElementType.GRENADE && element.getOwner() == Owner.USER) {
            System.out.println("Boom! Computer hit your grenade at " + (char) ('A' + col) + (row + 1) + ". Computer's next turn is skipped.");
            userTurn();
        } else if (element.getType() == ElementType.GRENADE && element.getOwner() == Owner.COMPUTER) {
            System.out.println("Boom! Computer hit his own grenade at " + (char) ('A' + col) + (row + 1) + ". Computer's next turn is skipped.");
            userTurn();
        } else if (element.getType() == ElementType.SHIP && element.getOwner() == Owner.COMPUTER) {
            System.out.println("Computer hit his own ship at " + (char) ('A' + col) + (row + 1));
        } else {
            System.out.println("Computer missed at " + (char) ('A' + col) + (row + 1));
        }
    }

    private boolean isGameOver() {
        boolean userShipsRemaining = false;
        boolean computerShipsRemaining = false;

        // Check if any user ships are remaining
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                GridElement element = grid.getElement(i, j);
                if (element.getType() == ElementType.SHIP && element.getOwner() == Owner.USER && !element.isCalled()) {
                    userShipsRemaining = true;
                    break;
                }
            }
            if (userShipsRemaining) {
                break;
            }
        }

        // Check if any computer ships are remaining
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                GridElement element = grid.getElement(i, j);
                if (element.getType() == ElementType.SHIP && element.getOwner() == Owner.COMPUTER && !element.isCalled()) {
                    computerShipsRemaining = true;
                    break;
                }
            }
            if (computerShipsRemaining) {
                break;
            }
        }

        return !userShipsRemaining || !computerShipsRemaining;
    }

    private void printBoard() {
        System.out.println("  A B C D E F G H");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                GridElement element = grid.getElement(i, j);
                if (element.isCalled()) {
                    if (element.getType() == ElementType.SHIP && element.getOwner() == Owner.USER) {
                        System.out.print("S ");
                    } else if (element.getType() == ElementType.GRENADE && element.getOwner() == Owner.USER) {
                        System.out.print("G ");
                    } else if (element.getType() == ElementType.SHIP && element.getOwner() == Owner.COMPUTER) {
                        System.out.print("s ");
                    } else if (element.getType() == ElementType.GRENADE && element.getOwner() == Owner.COMPUTER) {
                        System.out.print("g ");
                    } else {
                        System.out.print("* ");
                    }
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}


