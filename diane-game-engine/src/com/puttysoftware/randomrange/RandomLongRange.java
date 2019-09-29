package com.puttysoftware.randomrange;

/**
 * Generates random long integers in a range.
 */
public class RandomLongRange {

    /** The minimum. */
    // Fields
    private long minimum;

    /** The maximum. */
    private long maximum;

    /**
     * Instantiates a new random long range.
     *
     * @param min the min
     * @param max the max
     */
    // Constructor
    public RandomLongRange(final long min, final long max) {
        this.minimum = min;
        this.maximum = max;
    }

    /**
     * Sets the minimum.
     *
     * @param newMin the new minimum
     */
    // Methods
    public void setMinimum(final long newMin) {
        this.minimum = newMin;
    }

    /**
     * Sets the maximum.
     *
     * @param newMax the new maximum
     */
    public void setMaximum(final long newMax) {
        this.maximum = newMax;
    }

    /**
     * Generate.
     *
     * @return the long
     */
    public long generate() {
        if (this.maximum - this.minimum + 1 == 0) {
            return Math.abs(RandomnessSource.nextLong()) + this.minimum;
        }
        return Math.abs(
                RandomnessSource.nextLong() % (this.maximum - this.minimum + 1))
                + this.minimum;
    }

    /**
     * Generate raw.
     *
     * @return the long
     */
    public static long generateRaw() {
        return RandomnessSource.nextLong();
    }
}
