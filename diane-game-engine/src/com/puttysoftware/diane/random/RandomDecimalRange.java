/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.random;

/**
 * Generates random decimal numbers in a range.
 */
public class RandomDecimalRange {
    /** The minimum. */
    // Fields
    private static double minimum;
    /** The maximum. */
    private static double maximum;

    /**
     * Generate double.
     *
     * @return the double
     */
    public static double generateDouble() {
	return Math.abs(RandomnessSource.nextDouble() % (RandomDecimalRange.maximum - RandomDecimalRange.minimum + 1))
		+ RandomDecimalRange.minimum;
    }

    /**
     * Generate float.
     *
     * @return the float
     */
    public static float generateFloat() {
	return (float) RandomDecimalRange.generateDouble();
    }

    /**
     * Generate raw double.
     *
     * @return the double
     */
    public static double generateRawDouble() {
	return RandomnessSource.nextDouble();
    }

    /**
     * Sets the maximum.
     *
     * @param newMax the new maximum
     */
    public static void setMaximum(final double newMax) {
	RandomDecimalRange.maximum = newMax;
    }

    /**
     * Sets the minimum.
     *
     * @param newMin the new minimum
     */
    public static void setMinimum(final double newMin) {
	RandomDecimalRange.minimum = newMin;
    }

    /**
     * Instantiates a new random decimal range.
     */
    // Constructor
    private RandomDecimalRange() {
	// Do nothing
    }
}
