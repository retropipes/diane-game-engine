/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.point;

public class Point5D extends Point4D {
    public int v;

    public Point5D() {
        super();
    }

    public Point5D(final int nx, final int ny, final int nz, final int nw, final int nv) {
        super(nx, ny, nz, nw);
        v = nv;
    }
}
