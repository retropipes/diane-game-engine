/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.random;

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
     * Next bounded integer.
     *
     * @return the bounded integer
     */
    static int nextBoundedInt(final int bound) {
        return RandomnessSource.getSource().nextInt(bound);
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
