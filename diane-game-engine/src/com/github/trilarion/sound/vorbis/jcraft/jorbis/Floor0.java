/*
 * Copyright (C) 2000 ymnk<ymnk@jcraft.com>
 *               2015 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.trilarion.sound.vorbis.jcraft.jorbis;

import java.util.Arrays;

import com.github.trilarion.sound.util.Util;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Buffer;

class Floor0 extends FuncFloor {
    static class EchstateFloor0 {
	int[] codewords;
	float[] curve;
	long frameno;
	long codes;
    }

    static class InfoFloor0 {
	int order;
	int rate;
	int barkmap;
	int ampbits;
	int ampdB;
	int numbooks; // <= 16
	int[] books = new int[16];
    }

    static class LookFloor0 {
	int n;
	int ln;
	int m;
	int[] linearmap;
	InfoFloor0 vi;
	Lpc lpclook = new Lpc();
    }

    static float fromdB(final float x) {
	return (float) Math.exp(x * .11512925);
    }

    static void lpc_to_curve(final float[] curve, final float[] lpc, final float amp, final LookFloor0 l,
	    final String name, final int frameno) {
	// l->m+1 must be less than l->ln, but guard in case we get a bad stream
	final var lcurve = new float[Math.max(l.ln * 2, l.m * 2 + 2)];
	if (amp == 0) {
	    for (var j = 0; j < l.n; j++) {
		curve[j] = 0.0f;
	    }
	    return;
	}
	l.lpclook.lpc_to_curve(lcurve, lpc, amp);
	for (var i = 0; i < l.n; i++) {
	    curve[i] = lcurve[l.linearmap[i]];
	}
    }

    static void lsp_to_lpc(final float[] lsp, final float[] lpc, final int m) {
	int i, j;
	final var m2 = m / 2;
	final var O = new float[m2];
	final var E = new float[m2];
	float A;
	final var Ae = new float[m2 + 1];
	final var Ao = new float[m2 + 1];
	float B;
	final var Be = new float[m2];
	final var Bo = new float[m2];
	float temp;
	// even/odd roots setup
	for (i = 0; i < m2; i++) {
	    O[i] = (float) (-2. * Math.cos(lsp[i * 2]));
	    E[i] = (float) (-2. * Math.cos(lsp[i * 2 + 1]));
	}
	// set up impulse response
	for (j = 0; j < m2; j++) {
	    Ae[j] = 0.f;
	    Ao[j] = 1.f;
	    Be[j] = 0.f;
	    Bo[j] = 1.f;
	}
	Ao[j] = 1.f;
	Ae[j] = 1.f;
	// run impulse response
	for (i = 1; i < m + 1; i++) {
	    A = B = 0.f;
	    for (j = 0; j < m2; j++) {
		temp = O[j] * Ao[j] + Ae[j];
		Ae[j] = Ao[j];
		Ao[j] = A;
		A += temp;
		temp = E[j] * Bo[j] + Be[j];
		Be[j] = Bo[j];
		Bo[j] = B;
		B += temp;
	    }
	    lpc[i - 1] = (A + Ao[j] + B - Ae[j]) / 2;
	    Ao[j] = A;
	    Ae[j] = B;
	}
    }

    static float toBARK(final float f) {
	return (float) (13.1 * Math.atan(.00074 * f) + 2.24 * Math.atan(f * f * 1.85e-8) + 1e-4 * f);
    }

    float[] lsp = null;

    @Override
    int forward(final Block vb, final Object i, final float[] in, final float[] out, final Object vs) {
	return 0;
    }

    @Override
    void free_info(final Object i) {
    }

    @Override
    void free_look(final Object i) {
    }

    @Override
    void free_state(final Object vs) {
    }

    int inverse(final Block vb, final Object i, final float[] out) {
	// System.err.println("Floor0.inverse "+i.getClass()+"]");
	final var look = (LookFloor0) i;
	final var info = look.vi;
	final var ampraw = vb.opb.read(info.ampbits);
	if (ampraw > 0) { // also handles the -1 out of data case
	    final var maxval = (1 << info.ampbits) - 1;
	    final var amp = (float) ampraw / maxval * info.ampdB;
	    final var booknum = vb.opb.read(Util.ilog(info.numbooks));
	    if (booknum != -1 && booknum < info.numbooks) {
		synchronized (this) {
		    if (this.lsp == null || this.lsp.length < look.m) {
			this.lsp = new float[look.m];
		    } else {
			for (var j = 0; j < look.m; j++) {
			    this.lsp[j] = 0.f;
			}
		    }
		    final var b = vb.vd.fullbooks[info.books[booknum]];
		    var last = 0.f;
		    for (var j = 0; j < look.m; j++) {
			out[j] = 0.0f;
		    }
		    for (var j = 0; j < look.m; j += b.dim) {
			if (b.decodevs(this.lsp, j, vb.opb, 1, -1) == -1) {
			    for (var k = 0; k < look.n; k++) {
				out[k] = 0.0f;
			    }
			    return 0;
			}
		    }
		    for (var j = 0; j < look.m;) {
			for (var k = 0; k < b.dim; k++, j++) {
			    this.lsp[j] += last;
			}
			last = this.lsp[j - 1];
		    }
		    // take the coefficients back to a spectral envelope curve
		    Lsp.lsp_to_curve(out, look.linearmap, look.n, look.ln, this.lsp, look.m, amp, info.ampdB);
		    return 1;
		}
	    }
	}
	return 0;
    }

    @Override
    Object inverse1(final Block vb, final Object i, final Object memo) {
	final var look = (LookFloor0) i;
	final var info = look.vi;
	float[] lsp = null;
	if (memo instanceof float[]) {
	    lsp = (float[]) memo;
	}
	final var ampraw = vb.opb.read(info.ampbits);
	if (ampraw > 0) { // also handles the -1 out of data case
	    final var maxval = (1 << info.ampbits) - 1;
	    final var amp = (float) ampraw / maxval * info.ampdB;
	    final var booknum = vb.opb.read(Util.ilog(info.numbooks));
	    if (booknum != -1 && booknum < info.numbooks) {
		final var b = vb.vd.fullbooks[info.books[booknum]];
		var last = 0.f;
		if (lsp == null || lsp.length < look.m + 1) {
		    lsp = new float[look.m + 1];
		} else {
		    Arrays.fill(lsp, 0.f);
		}
		for (var j = 0; j < look.m; j += b.dim) {
		    if (b.decodev_set(lsp, j, vb.opb, b.dim) == -1) {
			return null;
		    }
		}
		for (var j = 0; j < look.m;) {
		    for (var k = 0; k < b.dim; k++, j++) {
			lsp[j] += last;
		    }
		    last = lsp[j - 1];
		}
		lsp[look.m] = amp;
		return lsp;
	    }
	}
	return null;
    }

    @Override
    int inverse2(final Block vb, final Object i, final Object memo, final float[] out) {
	final var look = (LookFloor0) i;
	final var info = look.vi;
	if (memo != null) {
	    final var lsp = (float[]) memo;
	    final var amp = lsp[look.m];
	    Lsp.lsp_to_curve(out, look.linearmap, look.n, look.ln, lsp, look.m, amp, info.ampdB);
	    return 1;
	}
	for (var j = 0; j < look.n; j++) {
	    out[j] = 0.f;
	}
	return 0;
    }

    @Override
    Object look(final DspState vd, final InfoMode mi, final Object i) {
	float scale;
	final var vi = vd.vi;
	final var info = (InfoFloor0) i;
	final var look = new LookFloor0();
	look.m = info.order;
	look.n = vi.blocksizes[mi.blockflag] / 2;
	look.ln = info.barkmap;
	look.vi = info;
	look.lpclook.init(look.ln, look.m);
	// we choose a scaling constant so that:
	scale = look.ln / Floor0.toBARK((float) (info.rate / 2.));
	// the mapping from a linear scale to a smaller bark scale is
	// straightforward. We do *not* make sure that the linear mapping
	// does not skip bark-scale bins; the decoder simply skips them and
	// the encoder may do what it wishes in filling them. They're
	// necessary in some mapping combinations to keep the scale spacing
	// accurate
	look.linearmap = new int[look.n];
	for (var j = 0; j < look.n; j++) {
	    var val = (int) Math.floor(Floor0.toBARK((float) (info.rate / 2. / look.n * j)) * scale); // bark
	    // numbers
	    // represent
	    // band
	    // edges
	    if (val >= look.ln) {
		val = look.ln; // guard against the approximation
	    }
	    look.linearmap[j] = val;
	}
	return look;
    }

    @Override
    void pack(final Object i, final Buffer opb) {
	final var info = (InfoFloor0) i;
	opb.write(info.order, 8);
	opb.write(info.rate, 16);
	opb.write(info.barkmap, 16);
	opb.write(info.ampbits, 6);
	opb.write(info.ampdB, 8);
	opb.write(info.numbooks - 1, 4);
	for (var j = 0; j < info.numbooks; j++) {
	    opb.write(info.books[j], 8);
	}
    }

    Object state(final Object i) {
	final var state = new EchstateFloor0();
	final var info = (InfoFloor0) i;
	// a safe size if usually too big (dim==1)
	state.codewords = new int[info.order];
	state.curve = new float[info.barkmap];
	state.frameno = -1;
	return state;
    }

    @Override
    Object unpack(final Info vi, final Buffer opb) {
	final var info = new InfoFloor0();
	info.order = opb.read(8);
	info.rate = opb.read(16);
	info.barkmap = opb.read(16);
	info.ampbits = opb.read(6);
	info.ampdB = opb.read(8);
	info.numbooks = opb.read(4) + 1;
	if (info.order < 1 || info.rate < 1 || info.barkmap < 1 || info.numbooks < 1) {
	    return null;
	}
	for (var j = 0; j < info.numbooks; j++) {
	    info.books[j] = opb.read(8);
	    if (info.books[j] < 0 || info.books[j] >= vi.books) {
		return null;
	    }
	}
	return info;
    }
}
