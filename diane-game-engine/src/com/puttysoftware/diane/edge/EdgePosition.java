/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.edge;

public class EdgePosition {
    // Constants
    public static final EdgePosition MIDDLE = new EdgePosition(0.5);
    // Fields
    private final double offset;

    // Constructor
    private EdgePosition(final double newOffset) {
        this.offset = newOffset;
    }

    // Methods
    public double getOffset() {
        return this.offset;
    }
}
