package com.puttysoftware.randomrange;

/**
 * Generates random integers in a range.
 */
public class RandomRange {

    /** The minimum. */
    // Fields
    private int minimum;

    /** The maximum. */
    private int maximum;

    /**
     * Instantiates a new random range.
     *
     * @param min the min
     * @param max the max
     */
    // Constructor
    public RandomRange(final int min, final int max) {
        this.minimum = min;
        this.maximum = max;
    }

    /**
     * Sets the minimum.
     *
     * @param newMin the new minimum
     */
    // Methods
    public void setMinimum(final int newMin) {
        this.minimum = newMin;
    }

    /**
     * Sets the maximum.
     *
     * @param newMax the new maximum
     */
    public void setMaximum(final int newMax) {
        this.maximum = newMax;
    }

    /**
     * Generate.
     *
     * @return the generated integer
     */
    public static int generate(final int minimum, final int maximum) {
        if (maximum - minimum + 1 == 0) {
            return Math.abs(RandomnessSource.nextInt()) + minimum;
        }
        return Math.abs(
                RandomnessSource.nextInt() % (maximum - minimum + 1))
                + minimum;
    }

    /**
     * Generate.
     *
     * @return the generated integer
     */
    public int generate() {
        if (this.maximum - this.minimum + 1 == 0) {
            return Math.abs(RandomnessSource.nextInt()) + this.minimum;
        }
        return Math.abs(
                RandomnessSource.nextInt() % (this.maximum - this.minimum + 1))
                + this.minimum;
    }

    /**
     * Generate raw.
     *
     * @return the int
     */
    public static int generateRaw() {
        return RandomnessSource.nextInt();
    }
}
