/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.point;

public class Point6D extends Point5D {
    public int u;

    public Point6D() {
        super();
    }

    public Point6D(final int nx, final int ny, final int nz, final int nw, final int nv, final int nu) {
        super(nx, ny, nz, nw, nv);
        u = nu;
    }
}
