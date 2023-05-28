/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.point;

public class Point5D extends Point4D {
    public int v;

    public Point5D() {
    }

    public Point5D(final int nx, final int ny, final int nz, final int nw, final int nv) {
	super(nx, ny, nz, nw);
	this.v = nv;
    }
}
