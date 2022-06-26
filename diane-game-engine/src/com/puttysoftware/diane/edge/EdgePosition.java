/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.edge;

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
