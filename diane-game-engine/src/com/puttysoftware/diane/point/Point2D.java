/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.point;

public class Point2D extends Point1D {
    public int y;

    public Point2D() {
        super();
    }

    public Point2D(final int nx, final int ny) {
        super(nx);
        y = ny;
    }
}
