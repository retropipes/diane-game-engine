package com.puttysoftware.diane.randomrange;

import java.util.Random;

/**
 * A source of randomness for the other classes.
 */
class RandomnessSource {
    /** The source. */
    // Fields
    private static final Random theSource = new Random();

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
     * Next double.
     *
     * @return the double
     */
    static double nextDouble() {
	return RandomnessSource.getSource().nextDouble();
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
     * Instantiates a new randomness source.
     */
    // Constructor
    private RandomnessSource() {
	// Do nothing
    }
}
