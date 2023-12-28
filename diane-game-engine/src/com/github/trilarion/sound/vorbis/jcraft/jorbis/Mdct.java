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

/**
 *
 */
class Mdct {
    int n;
    int log2n;
    float[] trig;
    int[] bitrev;
    float scale;
    float[] _x = new float[1024];
    float[] _w = new float[1024];

    synchronized void backward(final float[] in, final float[] out) {
	if (this._x.length < this.n / 2) {
	    this._x = new float[this.n / 2];
	}
	if (this._w.length < this.n / 2) {
	    this._w = new float[this.n / 2];
	}
	final var x = this._x;
	final var w = this._w;
	final var n2 = this.n >>> 1;
	final var n4 = this.n >>> 2;
	final var n8 = this.n >>> 3;
	// rotate + step 1
	{
	    var inO = 1;
	    var xO = 0;
	    var A = n2;
	    int i;
	    for (i = 0; i < n8; i++) {
		A -= 2;
		x[xO] = -in[inO + 2] * this.trig[A + 1] - in[inO] * this.trig[A];
		xO++;
		x[xO++] = in[inO] * this.trig[A + 1] - in[inO + 2] * this.trig[A];
		inO += 4;
	    }
	    inO = n2 - 4;
	    for (i = 0; i < n8; i++) {
		A -= 2;
		x[xO] = in[inO] * this.trig[A + 1] + in[inO + 2] * this.trig[A];
		xO++;
		x[xO++] = in[inO] * this.trig[A] - in[inO + 2] * this.trig[A + 1];
		inO -= 4;
	    }
	}
	final var xxx = this.mdct_kernel(x, w, this.n, n2, n4, n8);
	var xx = 0;
	// step 8
	{
	    var B = n2;
	    int o1 = n4, o2 = o1 - 1;
	    int o3 = n4 + n2, o4 = o3 - 1;
	    for (var i = 0; i < n4; i++) {
		final var temp1 = xxx[xx] * this.trig[B + 1] - xxx[xx + 1] * this.trig[B];
		final var temp2 = -(xxx[xx] * this.trig[B] + xxx[xx + 1] * this.trig[B + 1]);
		out[o1] = -temp1;
		out[o2] = temp1;
		out[o3] = temp2;
		out[o4] = temp2;
		o1++;
		o2--;
		o3++;
		o4--;
		xx += 2;
		B += 2;
	    }
	}
    }

    void clear() {
    }

    void forward(final float[] in, final float[] out) {
    }

    void init(final int n) {
	this.bitrev = new int[n / 4];
	this.trig = new float[n + n / 4];
	this.log2n = (int) Math.rint(Math.log(n) / Math.log(2));
	this.n = n;
	final var AE = 0;
	final var AO = 1;
	final var BE = AE + n / 2;
	final var BO = BE + 1;
	final var CE = BE + n / 2;
	final var CO = CE + 1;
	// trig lookups...
	for (var i = 0; i < n / 4; i++) {
	    this.trig[AE + i * 2] = (float) Math.cos(Math.PI / n * (4 * i));
	    this.trig[AO + i * 2] = (float) -Math.sin(Math.PI / n * (4 * i));
	    this.trig[BE + i * 2] = (float) Math.cos(Math.PI / (2 * n) * (2 * i + 1));
	    this.trig[BO + i * 2] = (float) Math.sin(Math.PI / (2 * n) * (2 * i + 1));
	}
	for (var i = 0; i < n / 8; i++) {
	    this.trig[CE + i * 2] = (float) Math.cos(Math.PI / n * (4 * i + 2));
	    this.trig[CO + i * 2] = (float) -Math.sin(Math.PI / n * (4 * i + 2));
	}
	{
	    final var mask = (1 << this.log2n - 1) - 1;
	    final var msb = 1 << this.log2n - 2;
	    for (var i = 0; i < n / 8; i++) {
		var acc = 0;
		for (var j = 0; msb >>> j != 0; j++) {
		    if ((msb >>> j & i) != 0) {
			acc |= 1 << j;
		    }
		}
		this.bitrev[i * 2] = ~acc & mask;
		// bitrev[i*2]=((~acc)&mask)-1;
		this.bitrev[i * 2 + 1] = acc;
	    }
	}
	this.scale = 4.f / n;
    }

    private float[] mdct_kernel(float[] x, float[] w, final int n, final int n2, final int n4, final int n8) {
	// step 2
	var xA = n4;
	var xB = 0;
	var w2 = n4;
	var A = n2;
	for (var i = 0; i < n4;) {
	    final var x0 = x[xA] - x[xB];
	    float x1;
	    w[w2 + i] = x[xA] + x[xB++];
	    xA++;
	    x1 = x[xA] - x[xB];
	    A -= 4;
	    w[i++] = x0 * this.trig[A] + x1 * this.trig[A + 1];
	    w[i] = x1 * this.trig[A] - x0 * this.trig[A + 1];
	    w[w2 + i] = x[xA++] + x[xB++];
	    i++;
	}
	// step 3
	{
	    for (var i = 0; i < this.log2n - 3; i++) {
		var k0 = n >>> i + 2;
		final var k1 = 1 << i + 3;
		var wbase = n2 - 2;
		A = 0;
		float[] temp;
		for (var r = 0; r < k0 >>> 2; r++) {
		    var w1 = wbase;
		    w2 = w1 - (k0 >> 1);
		    final var AEv = this.trig[A];
		    float wA;
		    final var AOv = this.trig[A + 1];
		    float wB;
		    wbase -= 2;
		    k0++;
		    for (var s = 0; s < 2 << i; s++) {
			wB = w[w1] - w[w2];
			x[w1] = w[w1] + w[w2];
			w1++;
			wA = w[w1] - w[++w2];
			x[w1] = w[w1] + w[w2];
			x[w2] = wA * AEv - wB * AOv;
			x[w2 - 1] = wB * AEv + wA * AOv;
			w1 -= k0;
			w2 -= k0;
		    }
		    k0--;
		    A += k1;
		}
		temp = w;
		w = x;
		x = temp;
	    }
	}
	// step 4, 5, 6, 7
	{
	    var C = n;
	    var bit = 0;
	    var x1 = 0;
	    var x2 = n2 - 1;
	    for (var i = 0; i < n8; i++) {
		final var t1 = this.bitrev[bit];
		bit++;
		final var t2 = this.bitrev[bit++];
		final var wA = w[t1] - w[t2 + 1];
		final var wB = w[t1 - 1] + w[t2];
		final var wC = w[t1] + w[t2 + 1];
		final var wD = w[t1 - 1] - w[t2];
		final var wACE = wA * this.trig[C];
		final var wBCE = wB * this.trig[C++];
		final var wACO = wA * this.trig[C];
		final var wBCO = wB * this.trig[C++];
		x[x1++] = (wC + wACO + wBCE) * .5f;
		x[x2--] = (-wD + wBCO - wACE) * .5f;
		x[x1++] = (wD + wBCO - wACE) * .5f;
		x[x2--] = (wC - wACO - wBCE) * .5f;
	    }
	}
	return x;
    }
}
