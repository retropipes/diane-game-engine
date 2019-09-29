package com.puttysoftware.randomrange;

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
     * Instantiates a new random decimal range.
     */
    // Constructor
    private RandomDecimalRange() {
        // Do nothing
    }

    /**
     * Sets the minimum.
     *
     * @param newMin the new minimum
     */
    // Methods
    public static void setMinimum(final double newMin) {
        RandomDecimalRange.minimum = newMin;
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
     * Generate float.
     *
     * @return the float
     */
    public static float generateFloat() {
        return (float) RandomDecimalRange.generateDouble();
    }

    /**
     * Generate double.
     *
     * @return the double
     */
    public static double generateDouble() {
        return Math.abs(RandomnessSource.nextDouble()
                % (RandomDecimalRange.maximum - RandomDecimalRange.minimum + 1))
                + RandomDecimalRange.minimum;
    }

    /**
     * Generate raw double.
     *
     * @return the double
     */
    public static double generateRawDouble() {
        return RandomnessSource.nextDouble();
    }
}
