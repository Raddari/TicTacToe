package com.github.raddari.tictactoe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>TicTacToe - the game itself needs no explanation.
 *
 * <p>This class represents a game of TicTacToe. It contains the state of
 * the board, as well a way to check for a winner. Two players go head to
 * head to play the game using the {@link #playAt(Player, int, int) method.
 * Whether the players are humans or computers is left entirely to the implementation.
 *
 * <p>The nested {@link StandardPlayer} enum contains the symbols for two players.
 * The value {@link StandardPlayer#NONE} should be used to represent an empty space.
 */
public class TicTacToe {
    
    /** Horizontal and vertical positions on the board */
    private final int boardSize;
    /** Maximum score available. Used in calculations for {@link #winCondition} */
    private final int maxScore;
    /** Stores the moves of the players */
    private final Player[][] board;
    private final Map<Player, Integer[]> winCondition;
    /** Track the total amount of moves */
    private int totalScore;
    private Player winner;
    
    public TicTacToe(int boardSize) {
        this.boardSize = Parameters.requireGreater(boardSize, 2);
        this.board = new StandardPlayer[boardSize][boardSize];
        this.winCondition = new HashMap<>();
        this.maxScore = (2 * boardSize + 2) * boardSize;
        this.totalScore = 0;
        this.winner = null;
        
        initBoard();
    }
    
    /**
     * Attempts to set the specified row and column of the board to the given player.
     * @param player {@link StandardPlayer} to set at the location
     * @param row row number
     * @param col column number
     * @return {@code true} if a player can play that location
     */
    public boolean playAt(@NotNull Player player, int row, int col) {
        if (board[row][col] != StandardPlayer.NONE || !checkBounds(row, col)) {
            return false;
        }
        board[row][col] = player;
        updateScore(player, row, col);
        checkIfWinner(player);
        return true;
    }
    
    /**
     * Check if the game has been won, or it's a draw.
     * @return {@code true} if there is a winner or draw
     */
    public boolean hasWinner() {
        return winner != null;
    }
    
    /**
     * Gets the winner of the game.
     * @return the winner, or {@code null} if there isn't one
     */
    public @Nullable Player getWinner() {
        return winner;
    }
    
    /**
     * Gets a string representation of the board's state.
     * @return a string representing the board
     */
    public String boardAsString() {
        var out = new StringBuilder();
        for (var row : board) {
            for (var player : row) {
                out.append(String.format("%s ", player.symbol()));
            }
            out.append("\n");
        }
        return out.toString();
    }
    
    private void initBoard() {
        for (var i = 0; i < boardSize; i++) {
            for (var j = 0; j < boardSize; j++) {
                board[i][j] = StandardPlayer.NONE;
            }
        }
    }
    
    private boolean checkBounds(int row, int col) {
        return 0 <= row && row < boardSize && 0 <= col && col < boardSize;
    }
    
    private void ensurePlayer(@NotNull Player player) {
        if (!winCondition.containsKey(player)) {
            Integer[] scores = new Integer[2 * boardSize + 2];
            Arrays.fill(scores, 0);
            winCondition.put(player, scores);
        }
    }
    
    private void updateScore(@NotNull Player player, int row, int col) {
        ensurePlayer(player);
        var moves = winCondition.get(player);
        moves[row]++;
        moves[boardSize + col]++;
        totalScore += 2;
        // Check diagonals
        // These start from the index boardSize * 2 in the win condition array
        if (row == col) {
            // We're on the right diagonal
            moves[2 * boardSize]++;
            totalScore++;
        }
        if (row == boardSize - col - 1) {
            // We're on the left diagonal
            moves[2 * boardSize + 1]++;
            totalScore++;
        }
    }
    
    private void checkIfWinner(@NotNull Player player) {
        var moves = winCondition.get(player);
        // Check for draw first, so it can be overwritten by a player winning
        if (totalScore >= maxScore) {
            winner = StandardPlayer.NONE;
        }
        for (var i : moves) {
            if (i >= boardSize) {
                winner = player;
                break;
            }
        }
    }
    
    /**
     * <p>Representations of player symbols.
     * <p>These are extendable by creating an enum which implements {@link Player}.
     */
    public enum StandardPlayer implements Player {
        X, O, NONE;
        
        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String symbol() {
            return this == StandardPlayer.NONE ? " " : name();
        }
    }
}
