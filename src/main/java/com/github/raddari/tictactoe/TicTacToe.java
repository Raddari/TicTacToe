package com.github.raddari.tictactoe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    /** Keep track of the win condition */
    private final Map<Player, Integer[]> winCondition;
    
    public TicTacToe(int boardSize) {
        this.boardSize = Parameters.requireGreater(boardSize, 2);
        this.board = new StandardPlayer[boardSize][boardSize];
        this.winCondition = new HashMap<>();
        this.maxScore = (2 * boardSize + 2) * boardSize;
        
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
        updateCondition(player, row, col);
        return true;
    }
    
    /**
     * Checks if someone has won the game, or if there was a draw.
     * @return {@code null} if the board isn't full and there isn't a winner<br />
     *         {@link StandardPlayer#NONE} if the board is full and there isn't a winner
     *         (aka a draw)<br />
     *         otherwise, the winning player
     */
    public @Nullable Player checkForWinner() {
        // TODO: make this more efficient than n^2
        var playerScores = winCondition.entrySet().stream()
                                   .collect(Collectors.toMap(Map.Entry::getKey,
                                           e -> Arrays.stream(e.getValue())
                                                        .reduce(Integer::sum)));
        
        var totalScore = playerScores.values().stream()
                                 .mapToInt(i -> i.orElse(0))
                                 .sum();
        // First check it's a draw
        if (totalScore >= maxScore) {
            return StandardPlayer.NONE;
        }
        // Then check if any of the players have won
        for (var entry : winCondition.entrySet()) {
            for (var i : entry.getValue()) {
                if (i >= maxScore) {
                    return entry.getKey();
                }
            }
        }
        return null;
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
    
    private void updateCondition(@NotNull Player player, int row, int col) {
        ensurePlayer(player);
        var moves = winCondition.get(player);
        moves[row]++;
        moves[boardSize + col]++;
        // Check diagonals
        // These start from the index boardSize * 2 in the win condition array
        if (row == col) {
            // We're on the right diagonal
            moves[2 * boardSize]++;
        }
        if (row == boardSize - col - 1) {
            // We're on the left diagonal
            moves[2 * boardSize + 1]++;
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
            return this != StandardPlayer.NONE ? this.name() : " ";
        }
    }
}
