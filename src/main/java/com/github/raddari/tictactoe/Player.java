package com.github.raddari.tictactoe;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Interface representing a player in a game of TicTacToe.
 * Ideally this interface is to be implemented by an Enum, so more player
 * symbols can be specified.
 */
public interface Player {
    
    /**
     * Gets the symbol that represents the current player.
     * @return the player's symbol
     */
    @NotNull String symbol();
    
}
