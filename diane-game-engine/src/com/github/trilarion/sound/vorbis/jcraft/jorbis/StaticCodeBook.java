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

import com.github.trilarion.sound.util.Util;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Buffer;

/**
 *
 */
class StaticCodeBook {
    // 32 bit float (not IEEE; nonnormalized mantissa +
    // biased exponent) : neeeeeee eeemmmmm mmmmmmmm mmmmmmmm
    // Why not IEEE? It's just not that important here.
    static final int VQ_FEXP = 10;
    static final int VQ_FMAN = 21;
    static final int VQ_FEXP_BIAS = 768; // bias toward values smaller than 1.
    // doesn't currently guard under/overflow

    static long float32_pack(float val) {
	var sign = 0;
	int exp;
	int mant;
	if (val < 0) {
	    sign = 0x80000000;
	    val = -val;
	}
	exp = (int) Math.floor(Math.log(val) / Math.log(2));
	mant = (int) Math.rint(Math.pow(val, StaticCodeBook.VQ_FMAN - 1 - exp));
	exp = exp + StaticCodeBook.VQ_FEXP_BIAS << StaticCodeBook.VQ_FMAN;
	return sign | exp | mant;
    }

    static float float32_unpack(final int val) {
	float mant = val & 0x1fffff;
	final float exp = (val & 0x7fe00000) >>> StaticCodeBook.VQ_FMAN;
	if ((val & 0x80000000) != 0) {
	    mant = -mant;
	}
	return StaticCodeBook.ldexp(mant, (int) exp - (StaticCodeBook.VQ_FMAN - 1) - StaticCodeBook.VQ_FEXP_BIAS);
    }

    static float ldexp(final float foo, final int e) {
	return (float) (foo * Math.pow(2, e));
    }

    int dim; // codebook dimensions (elements per vector)
    int entries; // codebook entries
    int[] lengthlist; // codeword lengths in bits
    // mapping
    int maptype; // 0=none
    // 1=implicitly populated values from map column
    // 2=listed arbitrary values
    // The below does a linear, single monotonic sequence mapping.
    int q_min; // packed 32 bit float; quant value 0 maps to minval
    int q_delta; // packed 32 bit float; val 1 - val 0 == delta
    int q_quant; // bits: 0 < quant <= 16
    int q_sequencep; // bitflag
    // additional information for log (dB) mapping; the linear mapping
    // is assumed to actually be values in dB. encodebias is used to
    // assign an error weight to 0 dB. We have two additional flags:
    // zeroflag indicates if entry zero is to represent -Inf dB; negflag
    // indicates if we're to represent negative linear values in a
    // mirror of the positive mapping.
    int[] quantlist; // map == 1: (int)(entries/dim) element column map
    // map == 2: list of dim*entries quantized entry vals

    StaticCodeBook() {
    }

    void clear() {
    }

    // there might be a straightforward one-line way to do the below
    // that's portable and totally safe against roundoff, but I haven't
    // thought of it. Therefore, we opt on the side of caution
    private int maptype1_quantvals() {
	var vals = (int) Math.floor(Math.pow(this.entries, 1. / this.dim));
	// the above *should* be reliable, but we'll not assume that FP is
	// ever reliable when bitstream sync is at stake; verify via integer
	// means that vals really is the greatest value of dim for which
	// vals^b->bim <= b->entries
	// treat the above as an initial guess
	while (true) {
	    var acc = 1;
	    var acc1 = 1;
	    for (var i = 0; i < this.dim; i++) {
		acc *= vals;
		acc1 *= vals + 1;
	    }
	    if (acc <= this.entries && acc1 > this.entries) {
		return vals;
	    }
	    if (acc > this.entries) {
		vals--;
	    } else {
		vals++;
	    }
	}
    }

    int pack(final Buffer opb) {
	int i;
	var ordered = false;
	opb.write(0x564342, 24);
	opb.write(this.dim, 16);
	opb.write(this.entries, 24);
	// pack the codewords. There are two packings; length ordered and
	// length random. Decide between the two now.
	for (i = 1; i < this.entries; i++) {
	    if (this.lengthlist[i] < this.lengthlist[i - 1]) {
		break;
	    }
	}
	if (i == this.entries) {
	    ordered = true;
	}
	if (ordered) {
	    // length ordered. We only need to say how many codewords of
	    // each length. The actual codewords are generated
	    // deterministically
	    var count = 0;
	    opb.write(1, 1); // ordered
	    opb.write(this.lengthlist[0] - 1, 5); // 1 to 32
	    for (i = 1; i < this.entries; i++) {
		final var _this = this.lengthlist[i];
		final var _last = this.lengthlist[i - 1];
		if (_this > _last) {
		    for (var j = _last; j < _this; j++) {
			opb.write(i - count, Util.ilog(this.entries - count));
			count = i;
		    }
		}
	    }
	    opb.write(i - count, Util.ilog(this.entries - count));
	} else {
	    // length random. Again, we don't code the codeword itself, just
	    // the length. This time, though, we have to encode each length
	    opb.write(0, 1); // unordered
	    // algortihmic mapping has use for 'unused entries', which we tag
	    // here. The algorithmic mapping happens as usual, but the unused
	    // entry has no codeword.
	    for (i = 0; i < this.entries; i++) {
		if (this.lengthlist[i] == 0) {
		    break;
		}
	    }
	    if (i == this.entries) {
		opb.write(0, 1); // no unused entries
		for (i = 0; i < this.entries; i++) {
		    opb.write(this.lengthlist[i] - 1, 5);
		}
	    } else {
		opb.write(1, 1); // we have unused entries; thus we tag
		for (i = 0; i < this.entries; i++) {
		    if (this.lengthlist[i] == 0) {
			opb.write(0, 1);
		    } else {
			opb.write(1, 1);
			opb.write(this.lengthlist[i] - 1, 5);
		    }
		}
	    }
	}
	// is the entry number the desired return value, or do we have a
	// mapping? If we have a mapping, what type?
	opb.write(this.maptype, 4);
	switch (this.maptype) {
	case 0:
	    // no mapping
	    break;
	case 1:
	case 2:
	    // implicitly populated value mapping
	    // explicitly populated value mapping
	    if (this.quantlist == null) {
		// no quantlist? error
		return -1;
	    }
	    // values that define the dequantization
	    opb.write(this.q_min, 32);
	    opb.write(this.q_delta, 32);
	    opb.write(this.q_quant - 1, 4);
	    opb.write(this.q_sequencep, 1); {
	    var quantvals = 0;
	    switch (this.maptype) {
	    case 1:
		// a single column of (c->entries/c->dim) quantized values for
		// building a full value list algorithmically (square lattice)
		quantvals = this.maptype1_quantvals();
		break;
	    case 2:
		// every value (c->entries*c->dim total) specified explicitly
		quantvals = this.entries * this.dim;
		break;
	    }
	    // quantized values
	    for (i = 0; i < quantvals; i++) {
		opb.write(Math.abs(this.quantlist[i]), this.q_quant);
	    }
	}
	    break;
	default:
	    // error case; we don't have any other map types now
	    return -1;
	}
	return 0;
    }

    // unpacks a codebook from the packet buffer into the codebook struct,
    // readies the codebook auxiliary structures for decode
    int unpack(final Buffer opb) {
	int i;
	// memset(s,0,sizeof(static_codebook));
	// make sure alignment is correct
	if (opb.read(24) != 0x564342) {
	    // goto _eofout;
	    this.clear();
	    return -1;
	}
	// first the basic parameters
	this.dim = opb.read(16);
	this.entries = opb.read(24);
	if (this.entries == -1) {
	    // goto _eofout;
	    this.clear();
	    return -1;
	}
	// codeword ordering.... length ordered or unordered?
	switch (opb.read(1)) {
	case 0:
	    // unordered
	    this.lengthlist = new int[this.entries];
	    // allocated but unused entries?
	    if (opb.read(1) != 0) {
		// yes, unused entries
		for (i = 0; i < this.entries; i++) {
		    if (opb.read(1) != 0) {
			final var num = opb.read(5);
			if (num == -1) {
			    // goto _eofout;
			    this.clear();
			    return -1;
			}
			this.lengthlist[i] = num + 1;
		    } else {
			this.lengthlist[i] = 0;
		    }
		}
	    } else {
		// all entries used; no tagging
		for (i = 0; i < this.entries; i++) {
		    final var num = opb.read(5);
		    if (num == -1) {
			// goto _eofout;
			this.clear();
			return -1;
		    }
		    this.lengthlist[i] = num + 1;
		}
	    }
	    break;
	case 1: // ordered
	{
	    var length = opb.read(5) + 1;
	    this.lengthlist = new int[this.entries];
	    for (i = 0; i < this.entries;) {
		final var num = opb.read(Util.ilog(this.entries - i));
		if (num == -1) {
		    // goto _eofout;
		    this.clear();
		    return -1;
		}
		for (var j = 0; j < num; j++, i++) {
		    this.lengthlist[i] = length;
		}
		length++;
	    }
	}
	    break;
	default:
	    // EOF
	    return -1;
	}
	// Do we have a mapping to unpack?
	switch (this.maptype = opb.read(4)) {
	case 0:
	    // no mapping
	    break;
	case 1:
	case 2:
	    // implicitly populated value mapping
	    // explicitly populated value mapping
	    this.q_min = opb.read(32);
	    this.q_delta = opb.read(32);
	    this.q_quant = opb.read(4) + 1;
	    this.q_sequencep = opb.read(1); {
	    var quantvals = 0;
	    switch (this.maptype) {
	    case 1:
		quantvals = this.maptype1_quantvals();
		break;
	    case 2:
		quantvals = this.entries * this.dim;
		break;
	    }
	    // quantized values
	    this.quantlist = new int[quantvals];
	    for (i = 0; i < quantvals; i++) {
		this.quantlist[i] = opb.read(this.q_quant);
	    }
	    if (this.quantlist[quantvals - 1] == -1) {
		// goto _eofout;
		this.clear();
		return -1;
	    }
	}
	    break;
	default:
	    // goto _eofout;
	    this.clear();
	    return -1;
	}
	// all set
	return 0;
	// _errout:
	// _eofout:
	// vorbis_staticbook_clear(s);
	// return(-1);
    }

    // unpack the quantized list of values for encode/decode
    // we need to deal with two map types: in map type 1, the values are
    // generated algorithmically (each column of the vector counts through
    // the values in the quant vector). in map type 2, all the values came
    // in in an explicit list. Both value lists must be unpacked
    float[] unquantize() {
	if (this.maptype == 1 || this.maptype == 2) {
	    int quantvals;
	    final var mindel = StaticCodeBook.float32_unpack(this.q_min);
	    final var delta = StaticCodeBook.float32_unpack(this.q_delta);
	    final var r = new float[this.entries * this.dim];
	    // maptype 1 and 2 both use a quantized value vector, but
	    // different sizes
	    switch (this.maptype) {
	    case 1:
		// most of the time, entries%dimensions == 0, but we need to be
		// well defined. We define that the possible vales at each
		// scalar is values == entries/dim. If entries%dim != 0, we'll
		// have 'too few' values (values*dim<entries), which means that
		// we'll have 'left over' entries; left over entries use zeroed
		// values (and are wasted). So don't generate codebooks like
		// that
		quantvals = this.maptype1_quantvals();
		for (var j = 0; j < this.entries; j++) {
		    var last = 0.f;
		    var indexdiv = 1;
		    for (var k = 0; k < this.dim; k++) {
			final var index = j / indexdiv % quantvals;
			float val = this.quantlist[index];
			val = Math.abs(val) * delta + mindel + last;
			if (this.q_sequencep != 0) {
			    last = val;
			}
			r[j * this.dim + k] = val;
			indexdiv *= quantvals;
		    }
		}
		break;
	    case 2:
		for (var j = 0; j < this.entries; j++) {
		    var last = 0.f;
		    for (var k = 0; k < this.dim; k++) {
			float val = this.quantlist[j * this.dim + k];
			// if((j*dim+k)==0){System.err.println(" | 0 -> "+val+"
			// | ");}
			val = Math.abs(val) * delta + mindel + last;
			if (this.q_sequencep != 0) {
			    last = val;
			}
			r[j * this.dim + k] = val;
			// if((j*dim+k)==0){System.err.println(" $ r[0] ->
			// "+r[0]+" | ");}
		    }
		}
		// System.err.println("\nr[0]="+r[0]);
	    }
	    return r;
	}
	return null;
    }
}
