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

class CodeBook {
    static class DecodeAux {
	int[] tab;
	int[] tabl;
	int tabn;
	int[] ptr0;
	int[] ptr1;
	int aux; // number of tree entries
    }

    private static float dist(final int el, final float[] ref, final int index, final float[] b, final int step) {
	var acc = (float) 0.;
	for (var i = 0; i < el; i++) {
	    final var val = ref[index + i] - b[i * step];
	    acc += val * val;
	}
	return acc;
    }

    // given a list of word lengths, generate a list of codewords. Works
    // for length ordered or unordered, always assigns the lowest valued
    // codewords first. Extended to handle unused entries (length 0)
    static int[] make_words(final int[] l, final int n) {
	final var marker = new int[33];
	final var r = new int[n];
	for (var i = 0; i < n; i++) {
	    final var length = l[i];
	    if (length > 0) {
		var entry = marker[length];
		// when we claim a node for an entry, we also claim the nodes
		// below it (pruning off the imagined tree that may have dangled
		// from it) as well as blocking the use of any nodes directly
		// above for leaves
		// update ourself
		if (length < 32 && entry >>> length != 0) {
		    // error condition; the lengths must specify an
		    // overpopulated tree
		    // free(r);
		    return null;
		}
		r[i] = entry;
		// Look to see if the next shorter marker points to the node
		// above. if so, update it and repeat.
		{
		    for (var j = length; j > 0; j--) {
			if ((marker[j] & 1) != 0) {
			    // have to jump branches
			    if (j == 1) {
				marker[1]++;
			    } else {
				marker[j] = marker[j - 1] << 1;
			    }
			    break; // invariant says next upper marker would
			    // already
			    // have been moved if it was on the same path
			}
			marker[j]++;
		    }
		}
		// prune the tree; the implicit invariant says all the longer
		// markers were dangling from our just-taken node. Dangle them
		// from our *new* node.
		for (var j = length + 1; j < 33; j++) {
		    if (marker[j] >>> 1 != entry) {
			break;
		    }
		    entry = marker[j];
		    marker[j] = marker[j - 1] << 1;
		}
	    }
	}
	// bitreverse the words because our bitwise packer/unpacker is LSb
	// endian
	for (var i = 0; i < n; i++) {
	    var temp = 0;
	    for (var j = 0; j < l[i]; j++) {
		temp <<= 1;
		temp |= r[i] >>> j & 1;
	    }
	    r[i] = temp;
	}
	return r;
    }

    int dim; // codebook dimensions (elements per vector)
    int entries; // codebook entries
    StaticCodeBook c = new StaticCodeBook();
    float[] valuelist; // list of dim*entries actual entry values
    int[] codelist; // list of bitstream codewords for each entry
    DecodeAux decode_tree;
    private int[] t = new int[15]; // decodevs_add is synchronized for re-using
    // t.

    int best(final float[] a, final int step) {
	// brute force it!
	{
	    var besti = -1;
	    var best = 0.f;
	    var e = 0;
	    for (var i = 0; i < this.entries; i++) {
		if (this.c.lengthlist[i] > 0) {
		    final var _this = CodeBook.dist(this.dim, this.valuelist, e, a, step);
		    if (besti == -1 || _this < best) {
			best = _this;
			besti = i;
		    }
		}
		e += this.dim;
	    }
	    return besti;
	}
    }

    // returns the entry number and *modifies a* to the remainder value
    int besterror(final float[] a, final int step, final int addmul) {
	final var best = this.best(a, step);
	switch (addmul) {
	case 0:
	    for (int i = 0, o = 0; i < this.dim; i++, o += step) {
		a[o] -= this.valuelist[best * this.dim + i];
	    }
	    break;
	case 1:
	    for (int i = 0, o = 0; i < this.dim; i++, o += step) {
		final var val = this.valuelist[best * this.dim + i];
		if (val == 0) {
		    a[o] = 0;
		} else {
		    a[o] /= val;
		}
	    }
	    break;
	}
	return best;
    }

    void clear() {
    }

    // Decode side is specced and easier, because we don't need to find
    // matches using different criteria; we simply read and map. There are
    // two things we need to do 'depending':
    //
    // We may need to support interleave. We don't really, but it's
    // convenient to do it here rather than rebuild the vector later.
    //
    // Cascades may be additive or multiplicitive; this is not inherent in
    // the codebook, but set in the code using the codebook. Like
    // interleaving, it's easiest to do it here.
    // stage==0 -> declarative (set the value)
    // stage==1 -> additive
    // stage==2 -> multiplicitive
    // returns the entry number or -1 on eof
    int decode(final Buffer b) {
	var ptr = 0;
	final var t = this.decode_tree;
	final var lok = b.look(t.tabn);
	if (lok >= 0) {
	    ptr = t.tab[lok];
	    b.adv(t.tabl[lok]);
	    if (ptr <= 0) {
		return -ptr;
	    }
	}
	do {
	    switch (b.read1()) {
	    case 0:
		ptr = t.ptr0[ptr];
		break;
	    case 1:
		ptr = t.ptr1[ptr];
		break;
	    case -1:
	    default:
		return -1;
	    }
	} while (ptr > 0);
	return -ptr;
    }

    int decodev_add(final float[] a, final int offset, final Buffer b, final int n) {
	int i, j, entry;
	int t;
	if (this.dim > 8) {
	    for (i = 0; i < n;) {
		entry = this.decode(b);
		if (entry == -1) {
		    return -1;
		}
		t = entry * this.dim;
		for (j = 0; j < this.dim;) {
		    a[offset + i] += this.valuelist[t + j++];
		    i++;
		}
	    }
	} else {
	    for (i = 0; i < n;) {
		entry = this.decode(b);
		if (entry == -1) {
		    return -1;
		}
		t = entry * this.dim;
		j = 0;
		switch (this.dim) {
		case 8:
		    a[offset + i] += this.valuelist[t + j++];
		    i++;
		case 7:
		    a[offset + i++] += this.valuelist[t + j++];
		case 6:
		    a[offset + i++] += this.valuelist[t + j++];
		case 5:
		    a[offset + i++] += this.valuelist[t + j++];
		case 4:
		    a[offset + i++] += this.valuelist[t + j++];
		case 3:
		    a[offset + i++] += this.valuelist[t + j++];
		case 2:
		    a[offset + i++] += this.valuelist[t + j++];
		case 1:
		    a[offset + i++] += this.valuelist[t + j++];
		case 0:
		    break;
		}
	    }
	}
	return 0;
    }

    int decodev_set(final float[] a, final int offset, final Buffer b, final int n) {
	int i, j, entry;
	int t;
	for (i = 0; i < n;) {
	    entry = this.decode(b);
	    if (entry == -1) {
		return -1;
	    }
	    t = entry * this.dim;
	    for (j = 0; j < this.dim;) {
		a[offset + i] = this.valuelist[t + j++];
		i++;
	    }
	}
	return 0;
    }

    // returns the entry number or -1 on eof
    int decodevs(final float[] a, final int index, final Buffer b, final int step, final int addmul) {
	final var entry = this.decode(b);
	if (entry == -1) {
	    return -1;
	}
	switch (addmul) {
	case -1:
	    for (int i = 0, o = 0; i < this.dim; i++, o += step) {
		a[index + o] = this.valuelist[entry * this.dim + i];
	    }
	    break;
	case 0:
	    for (int i = 0, o = 0; i < this.dim; i++, o += step) {
		a[index + o] += this.valuelist[entry * this.dim + i];
	    }
	    break;
	case 1:
	    for (int i = 0, o = 0; i < this.dim; i++, o += step) {
		a[index + o] *= this.valuelist[entry * this.dim + i];
	    }
	    break;
	default:
	    // System.err.println("CodeBook.decodeves: addmul="+addmul);
	}
	return entry;
    }

    synchronized int decodevs_add(final float[] a, final int offset, final Buffer b, final int n) {
	final var step = n / this.dim;
	int entry;
	int i, j, o;
	if (this.t.length < step) {
	    this.t = new int[step];
	}
	for (i = 0; i < step; i++) {
	    entry = this.decode(b);
	    if (entry == -1) {
		return -1;
	    }
	    this.t[i] = entry * this.dim;
	}
	for (i = 0, o = 0; i < this.dim; i++, o += step) {
	    for (j = 0; j < step; j++) {
		a[offset + o + j] += this.valuelist[this.t[j] + i];
	    }
	}
	return 0;
    }

    int decodevv_add(final float[][] a, final int offset, final int ch, final Buffer b, final int n) {
	int i, j, entry;
	var chptr = 0;
	for (i = offset / ch; i < (offset + n) / ch;) {
	    entry = this.decode(b);
	    if (entry == -1) {
		return -1;
	    }
	    final var t = entry * this.dim;
	    for (j = 0; j < this.dim; j++) {
		a[chptr][i] += this.valuelist[t + j];
		chptr++;
		if (chptr == ch) {
		    chptr = 0;
		    i++;
		}
	    }
	}
	return 0;
    }

    // returns the number of bits
    int encode(final int a, final Buffer b) {
	b.write(this.codelist[a], this.c.lengthlist[a]);
	return this.c.lengthlist[a];
    }

    // returns the number of bits and *modifies a* to the quantization value
    int encodev(final int best, final float[] a, final Buffer b) {
	for (var k = 0; k < this.dim; k++) {
	    a[k] = this.valuelist[best * this.dim + k];
	}
	return this.encode(best, b);
    }

    // res0 (multistage, interleave, lattice)
    // returns the number of bits and *modifies a* to the remainder value
    int encodevs(final float[] a, final Buffer b, final int step, final int addmul) {
	final var best = this.besterror(a, step, addmul);
	return this.encode(best, b);
    }

    // One the encode side, our vector writers are each designed for a
    // specific purpose, and the encoder is not flexible without modification:
    //
    // The LSP vector coder uses a single stage nearest-match with no
    // interleave, so no step and no error return. This is specced by floor0
    // and doesn't change.
    //
    // Residue0 encoding interleaves, uses multiple stages, and each stage
    // peels of a specific amount of resolution from a lattice (thus we want
    // to match by threshhold, not nearest match). Residue doesn't *have* to
    // be encoded that way, but to change it, one will need to add more
    // infrastructure on the encode side (decode side is specced and simpler)
    // floor0 LSP (single stage, non interleaved, nearest match)
    // returns entry number and *modifies a* to the quantization value
    int errorv(final float[] a) {
	final var best = this.best(a, 1);
	for (var k = 0; k < this.dim; k++) {
	    a[k] = this.valuelist[best * this.dim + k];
	}
	return best;
    }

    int init_decode(final StaticCodeBook s) {
	this.c = s;
	this.entries = s.entries;
	this.dim = s.dim;
	this.valuelist = s.unquantize();
	this.decode_tree = this.make_decode_tree();
	if (this.decode_tree == null) {
	    this.clear();
	    return -1;
	}
	return 0;
    }

    // build the decode helper tree from the codewords
    DecodeAux make_decode_tree() {
	var top = 0;
	final var t = new DecodeAux();
	final var ptr0 = t.ptr0 = new int[this.entries * 2];
	final var ptr1 = t.ptr1 = new int[this.entries * 2];
	final var codelist = CodeBook.make_words(this.c.lengthlist, this.c.entries);
	if (codelist == null) {
	    return null;
	}
	t.aux = this.entries * 2;
	for (var i = 0; i < this.entries; i++) {
	    if (this.c.lengthlist[i] > 0) {
		var ptr = 0;
		int j;
		for (j = 0; j < this.c.lengthlist[i] - 1; j++) {
		    final var bit = codelist[i] >>> j & 1;
		    if (bit == 0) {
			if (ptr0[ptr] == 0) {
			    top++;
			    ptr0[ptr] = top;
			}
			ptr = ptr0[ptr];
		    } else {
			if (ptr1[ptr] == 0) {
			    top++;
			    ptr1[ptr] = top;
			}
			ptr = ptr1[ptr];
		    }
		}
		if ((codelist[i] >>> j & 1) == 0) {
		    ptr0[ptr] = -i;
		} else {
		    ptr1[ptr] = -i;
		}
	    }
	}
	t.tabn = Util.ilog(this.entries) - 4;
	if (t.tabn < 5) {
	    t.tabn = 5;
	}
	final var n = 1 << t.tabn;
	t.tab = new int[n];
	t.tabl = new int[n];
	for (var i = 0; i < n; i++) {
	    var p = 0;
	    var j = 0;
	    for (j = 0; j < t.tabn && (p > 0 || j == 0); j++) {
		if ((i & 1 << j) != 0) {
		    p = ptr1[p];
		} else {
		    p = ptr0[p];
		}
	    }
	    t.tab[i] = p; // -code
	    t.tabl[i] = j; // length
	}
	return t;
    }
}
