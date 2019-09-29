package com.puttysoftware.images.edges;

public class EdgePosition {
    // Fields
    private final double offset;
    // Constants
    public static final EdgePosition MIDDLE = new EdgePosition(0.5);

    // Constructor
    private EdgePosition(final double newOffset) {
        this.offset = newOffset;
    }

    // Methods
    public double getOffset() {
        return this.offset;
    }
}
