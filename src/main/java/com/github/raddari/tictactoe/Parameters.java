package com.github.raddari.tictactoe;

public final class Parameters {
    
    private Parameters() {
        // No instances
    }
    
    /**
     * Checks that the specified integer is greater than another.
     * This method returns the checked integer so it can be used in an assignment.
     * @param check integer to check
     * @param value integer to check greater than
     * @return {@code check}
     */
    public static int requireGreater(int check, int value) {
        if (check > value) {
            return check;
        }
        throw new IllegalArgumentException("Argument must be greater than value");
    }
    
}
