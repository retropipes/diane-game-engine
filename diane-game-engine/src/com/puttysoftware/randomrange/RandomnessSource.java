package com.puttysoftware.randomrange;

import java.util.Random;

/**
 * A source of randomness for the other classes.
 */
class RandomnessSource {

    /** The source. */
    // Fields
    private static final Random theSource = new Random();

    /**
     * Instantiates a new randomness source.
     */
    // Constructor
    private RandomnessSource() {
        // Do nothing
    }

    /**
     * Gets the source.
     *
     * @return the source
     */
    // Methods
    private static Random getSource() {
        return RandomnessSource.theSource;
    }

    /**
     * Next integer.
     *
     * @return the integer
     */
    static int nextInt() {
        return RandomnessSource.getSource().nextInt();
    }

    /**
     * Next long.
     *
     * @return the long
     */
    static long nextLong() {
        return RandomnessSource.getSource().nextLong();
    }

    /**
     * Next double.
     *
     * @return the double
     */
    static double nextDouble() {
        return RandomnessSource.getSource().nextDouble();
    }
}
