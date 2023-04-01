/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.random;

/**
 * Generates random long integers in a range.
 */
public class RandomLongRange {
    /**
     * Generate raw.
     *
     * @return the long
     */
    public static long generateRaw() {
        return RandomnessSource.nextLong();
    }

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
     * Generate.
     *
     * @return the long
     */
    public long generate() {
        if (this.maximum - this.minimum + 1 == 0) {
            return Math.abs(RandomnessSource.nextLong()) + this.minimum;
        }
        return Math.abs(RandomnessSource.nextLong() % (this.maximum - this.minimum + 1)) + this.minimum;
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
     * Sets the minimum.
     *
     * @param newMin the new minimum
     */
    public void setMinimum(final long newMin) {
        this.minimum = newMin;
    }
}
