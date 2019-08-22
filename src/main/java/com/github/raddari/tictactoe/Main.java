package com.github.raddari.tictactoe;

import java.util.Scanner;

import static com.github.raddari.tictactoe.TicTacToe.StandardPlayer;

public class Main {
    
    private static Scanner sc;
    
    public static void main(String[] args) {
        sc = new Scanner(System.in);
        do {
            playGame();
        } while (promptPlayAgain());
        sc.close();
    }
    
    private static void playGame() {
        var game = new TicTacToe(3);
        Player nextPlayer = StandardPlayer.X;
        while (true) {
            if (!game.hasWinner()) {
                // Keep playing
                promptInput(nextPlayer, game);
                nextPlayer = nextPlayer == StandardPlayer.X ? StandardPlayer.O : StandardPlayer.X;
            } else {
                var winner = game.getWinner();
                assert winner != null;
                System.out.printf("Winner: %s%n", winner == StandardPlayer.NONE ? "Draw!" : winner.symbol());
                return;
            }
        }
    }
    
    private static void promptInput(Player player, TicTacToe game) {
        System.out.println(game.boardAsString());
        boolean validInput;
        do {
            validInput = true;
            System.out.printf("Player %s, please enter the row and column where you want to play%n", player.symbol());
            System.out.println("For example: 1 3 for the first row, third column");
            
            try {
                var tokens = sc.nextLine().split(" ");
                int row = Integer.parseInt(tokens[0]);
                int col = Integer.parseInt(tokens[1]);
                if (!game.playAt(player, row - 1, col - 1)) {
                    System.out.println("A player has already played there, or the position is off the grid!");
                    validInput = false;
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Invalid input");
                validInput = false;
            }
        } while (!validInput);
        System.out.println();
    }
    
    private static boolean promptPlayAgain() {
        System.out.println("Would you like to play again? (y/n)");
        return sc.nextLine().equals("y");
    }
    
}
