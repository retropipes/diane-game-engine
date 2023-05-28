/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.point;

public class Point2D extends Point1D {
    public int y;

    public Point2D() {
    }

    public Point2D(final int nx, final int ny) {
	super(nx);
	this.y = ny;
    }
}
