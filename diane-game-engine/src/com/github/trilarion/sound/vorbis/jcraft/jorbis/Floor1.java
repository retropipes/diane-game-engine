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

class Floor1 extends FuncFloor {
    static class EchstateFloor1 {
	int[] codewords;
	float[] curve;
	long frameno;
	long codes;
    }

    class InfoFloor1 {
	static final int VIF_POSIT = 63;
	static final int VIF_CLASS = 16;
	static final int VIF_PARTS = 31;
	int partitions; /* 0 to 31 */
	int[] partitionclass = new int[InfoFloor1.VIF_PARTS]; /* 0 to 15 */
	int[] class_dim = new int[InfoFloor1.VIF_CLASS]; /* 1 to 8 */
	int[] class_subs = new int[InfoFloor1.VIF_CLASS]; /* 0,1,2,3 (bits: 1<<n poss) */
	int[] class_book = new int[InfoFloor1.VIF_CLASS]; /* subs ^ dim entries */
	int[][] class_subbook = new int[InfoFloor1.VIF_CLASS][]; /* [VIF_CLASS][subs] */
	int mult; /* 1 2 3 or 4 */
	int[] postlist = new int[InfoFloor1.VIF_POSIT + 2]; /* first two implicit */
	/* encode side analysis parameters */
	float maxover;
	float maxunder;
	float maxerr;
	int twofitminsize;
	int twofitminused;
	int twofitweight;
	float twofitatten;
	int unusedminsize;
	int unusedmin_n;
	int n;

	InfoFloor1() {
	    for (var i = 0; i < this.class_subbook.length; i++) {
		this.class_subbook[i] = new int[8];
	    }
	}

	Object copy_info() {
	    final var info = this;
	    final var ret = new InfoFloor1();
	    ret.partitions = info.partitions;
	    System.arraycopy(info.partitionclass, 0, ret.partitionclass, 0, InfoFloor1.VIF_PARTS);
	    System.arraycopy(info.class_dim, 0, ret.class_dim, 0, InfoFloor1.VIF_CLASS);
	    System.arraycopy(info.class_subs, 0, ret.class_subs, 0, InfoFloor1.VIF_CLASS);
	    System.arraycopy(info.class_book, 0, ret.class_book, 0, InfoFloor1.VIF_CLASS);
	    for (var j = 0; j < InfoFloor1.VIF_CLASS; j++) {
		System.arraycopy(info.class_subbook[j], 0, ret.class_subbook[j], 0, 8);
	    }
	    ret.mult = info.mult;
	    System.arraycopy(info.postlist, 0, ret.postlist, 0, InfoFloor1.VIF_POSIT + 2);
	    ret.maxover = info.maxover;
	    ret.maxunder = info.maxunder;
	    ret.maxerr = info.maxerr;
	    ret.twofitminsize = info.twofitminsize;
	    ret.twofitminused = info.twofitminused;
	    ret.twofitweight = info.twofitweight;
	    ret.twofitatten = info.twofitatten;
	    ret.unusedminsize = info.unusedminsize;
	    ret.unusedmin_n = info.unusedmin_n;
	    ret.n = info.n;
	    return ret;
	}

	void free() {
	    this.partitionclass = null;
	    this.class_dim = null;
	    this.class_subs = null;
	    this.class_book = null;
	    this.class_subbook = null;
	    this.postlist = null;
	}
    }

    static class LookFloor1 {
	static final int VIF_POSIT = 63;
	int[] sorted_index = new int[LookFloor1.VIF_POSIT + 2];
	int[] forward_index = new int[LookFloor1.VIF_POSIT + 2];
	int[] reverse_index = new int[LookFloor1.VIF_POSIT + 2];
	int[] hineighbor = new int[LookFloor1.VIF_POSIT];
	int[] loneighbor = new int[LookFloor1.VIF_POSIT];
	int posts;
	int n;
	int quant_q;
	InfoFloor1 vi;
	int phrasebits;
	int postbits;
	int frames;

	void free() {
	    this.sorted_index = null;
	    this.forward_index = null;
	    this.reverse_index = null;
	    this.hineighbor = null;
	    this.loneighbor = null;
	}
    }

    static class Lsfit_acc {
	long x0;
	long x1;
	long xa;
	long ya;
	long x2a;
	long y2a;
	long xya;
	long n;
	long an;
	long un;
	long edgey0;
	long edgey1;
    }

    static final int floor1_rangedb = 140;
    static final int VIF_POSIT = 63;
    private static final float[] FLOOR_fromdB_LOOKUP = { 1.0649863e-07F, 1.1341951e-07F, 1.2079015e-07F, 1.2863978e-07F,
	    1.3699951e-07F, 1.4590251e-07F, 1.5538408e-07F, 1.6548181e-07F, 1.7623575e-07F, 1.8768855e-07F,
	    1.9988561e-07F, 2.128753e-07F, 2.2670913e-07F, 2.4144197e-07F, 2.5713223e-07F, 2.7384213e-07F,
	    2.9163793e-07F, 3.1059021e-07F, 3.3077411e-07F, 3.5226968e-07F, 3.7516214e-07F, 3.9954229e-07F,
	    4.2550680e-07F, 4.5315863e-07F, 4.8260743e-07F, 5.1396998e-07F, 5.4737065e-07F, 5.8294187e-07F,
	    6.2082472e-07F, 6.6116941e-07F, 7.0413592e-07F, 7.4989464e-07F, 7.9862701e-07F, 8.5052630e-07F,
	    9.0579828e-07F, 9.6466216e-07F, 1.0273513e-06F, 1.0941144e-06F, 1.1652161e-06F, 1.2409384e-06F,
	    1.3215816e-06F, 1.4074654e-06F, 1.4989305e-06F, 1.5963394e-06F, 1.7000785e-06F, 1.8105592e-06F,
	    1.9282195e-06F, 2.0535261e-06F, 2.1869758e-06F, 2.3290978e-06F, 2.4804557e-06F, 2.6416497e-06F,
	    2.8133190e-06F, 2.9961443e-06F, 3.1908506e-06F, 3.3982101e-06F, 3.6190449e-06F, 3.8542308e-06F,
	    4.1047004e-06F, 4.3714470e-06F, 4.6555282e-06F, 4.9580707e-06F, 5.2802740e-06F, 5.6234160e-06F,
	    5.9888572e-06F, 6.3780469e-06F, 6.7925283e-06F, 7.2339451e-06F, 7.7040476e-06F, 8.2047000e-06F,
	    8.7378876e-06F, 9.3057248e-06F, 9.9104632e-06F, 1.0554501e-05F, 1.1240392e-05F, 1.1970856e-05F,
	    1.2748789e-05F, 1.3577278e-05F, 1.4459606e-05F, 1.5399272e-05F, 1.6400004e-05F, 1.7465768e-05F,
	    1.8600792e-05F, 1.9809576e-05F, 2.1096914e-05F, 2.2467911e-05F, 2.3928002e-05F, 2.5482978e-05F,
	    2.7139006e-05F, 2.8902651e-05F, 3.0780908e-05F, 3.2781225e-05F, 3.4911534e-05F, 3.7180282e-05F,
	    3.9596466e-05F, 4.2169667e-05F, 4.4910090e-05F, 4.7828601e-05F, 5.0936773e-05F, 5.4246931e-05F,
	    5.7772202e-05F, 6.1526565e-05F, 6.5524908e-05F, 6.9783085e-05F, 7.4317983e-05F, 7.9147585e-05F,
	    8.4291040e-05F, 8.9768747e-05F, 9.5602426e-05F, 0.00010181521F, 0.00010843174F, 0.00011547824F,
	    0.00012298267F, 0.00013097477F, 0.00013948625F, 0.00014855085F, 0.00015820453F, 0.00016848555F,
	    0.00017943469F, 0.00019109536F, 0.00020351382F, 0.00021673929F, 0.00023082423F, 0.00024582449F,
	    0.00026179955F, 0.00027881276F, 0.00029693158F, 0.00031622787F, 0.00033677814F, 0.00035866388F,
	    0.00038197188F, 0.00040679456F, 0.00043323036F, 0.00046138411F, 0.00049136745F, 0.00052329927F,
	    0.00055730621F, 0.00059352311F, 0.00063209358F, 0.00067317058F, 0.00071691700F, 0.00076350630F,
	    0.00081312324F, 0.00086596457F, 0.00092223983F, 0.00098217216F, 0.0010459992F, 0.0011139742F, 0.0011863665F,
	    0.0012634633F, 0.0013455702F, 0.0014330129F, 0.0015261382F, 0.0016253153F, 0.0017309374F, 0.0018434235F,
	    0.0019632195F, 0.0020908006F, 0.0022266726F, 0.0023713743F, 0.0025254795F, 0.0026895994F, 0.0028643847F,
	    0.0030505286F, 0.0032487691F, 0.0034598925F, 0.0036847358F, 0.0039241906F, 0.0041792066F, 0.0044507950F,
	    0.0047400328F, 0.0050480668F, 0.0053761186F, 0.0057254891F, 0.0060975636F, 0.0064938176F, 0.0069158225F,
	    0.0073652516F, 0.0078438871F, 0.0083536271F, 0.0088964928F, 0.009474637F, 0.010090352F, 0.010746080F,
	    0.011444421F, 0.012188144F, 0.012980198F, 0.013823725F, 0.014722068F, 0.015678791F, 0.016697687F,
	    0.017782797F, 0.018938423F, 0.020169149F, 0.021479854F, 0.022875735F, 0.024362330F, 0.025945531F,
	    0.027631618F, 0.029427276F, 0.031339626F, 0.033376252F, 0.035545228F, 0.037855157F, 0.040315199F,
	    0.042935108F, 0.045725273F, 0.048696758F, 0.051861348F, 0.055231591F, 0.058820850F, 0.062643361F,
	    0.066714279F, 0.071049749F, 0.075666962F, 0.080584227F, 0.085821044F, 0.091398179F, 0.097337747F,
	    0.10366330F, 0.11039993F, 0.11757434F, 0.12521498F, 0.13335215F, 0.14201813F, 0.15124727F, 0.16107617F,
	    0.17154380F, 0.18269168F, 0.19456402F, 0.20720788F, 0.22067342F, 0.23501402F, 0.25028656F, 0.26655159F,
	    0.28387361F, 0.30232132F, 0.32196786F, 0.34289114F, 0.36517414F, 0.38890521F, 0.41417847F, 0.44109412F,
	    0.46975890F, 0.50028648F, 0.53279791F, 0.56742212F, 0.60429640F, 0.64356699F, 0.68538959F, 0.72993007F,
	    0.77736504F, 0.82788260F, 0.88168307F, 0.9389798F, 1.F };

    private static void render_line(final int x0, final int x1, final int y0, final int y1, final float[] d) {
	final var dy = y1 - y0;
	final var adx = x1 - x0;
	var ady = Math.abs(dy);
	final var base = dy / adx;
	final var sy = dy < 0 ? base - 1 : base + 1;
	var x = x0;
	var y = y0;
	var err = 0;
	ady -= Math.abs(base * adx);
	d[x] *= Floor1.FLOOR_fromdB_LOOKUP[y];
	while (++x < x1) {
	    err = err + ady;
	    if (err >= adx) {
		err -= adx;
		y += sy;
	    } else {
		y += base;
	    }
	    d[x] *= Floor1.FLOOR_fromdB_LOOKUP[y];
	}
    }

    private static int render_point(final int x0, final int x1, int y0, int y1, final int x) {
	y0 &= 0x7fff; /* mask off flag */
	y1 &= 0x7fff;
	{
	    final var dy = y1 - y0;
	    final var adx = x1 - x0;
	    final var ady = Math.abs(dy);
	    final var err = ady * (x - x0);
	    final var off = err / adx;
	    if (dy < 0) {
		return y0 - off;
	    }
	    return y0 + off;
	}
    }

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

    @Override
    Object inverse1(final Block vb, final Object ii, final Object memo) {
	final var look = (LookFloor1) ii;
	final var info = look.vi;
	final var books = vb.vd.fullbooks;
	/* unpack wrapped/predicted values from stream */
	if (vb.opb.read(1) == 1) {
	    int[] fit_value = null;
	    if (memo instanceof int[]) {
		fit_value = (int[]) memo;
	    }
	    if (fit_value == null || fit_value.length < look.posts) {
		fit_value = new int[look.posts];
	    } else {
		Arrays.fill(fit_value, 0);
	    }
	    fit_value[0] = vb.opb.read(Util.ilog(look.quant_q - 1));
	    fit_value[1] = vb.opb.read(Util.ilog(look.quant_q - 1));
	    /* partition by partition */
	    for (int i = 0, j = 2; i < info.partitions; i++) {
		final var clss = info.partitionclass[i];
		final var cdim = info.class_dim[clss];
		final var csubbits = info.class_subs[clss];
		final var csub = 1 << csubbits;
		var cval = 0;
		/* decode the partition's first stage cascade value */
		if (csubbits != 0) {
		    cval = books[info.class_book[clss]].decode(vb.opb);
		    if (cval == -1) {
			return null;
		    }
		}
		for (var k = 0; k < cdim; k++) {
		    final var book = info.class_subbook[clss][cval & csub - 1];
		    cval >>>= csubbits;
		    if (book >= 0) {
			fit_value[j + k] = books[book].decode(vb.opb);
			if (fit_value[j + k] == -1) {
			    return null;
			}
		    } else {
			fit_value[j + k] = 0;
		    }
		}
		j += cdim;
	    }
	    /*
	     * unwrap positive values and reconsitute via linear interpolation
	     */
	    for (var i = 2; i < look.posts; i++) {
		final var predicted = Floor1.render_point(info.postlist[look.loneighbor[i - 2]],
			info.postlist[look.hineighbor[i - 2]], fit_value[look.loneighbor[i - 2]],
			fit_value[look.hineighbor[i - 2]], info.postlist[i]);
		final var hiroom = look.quant_q - predicted;
		final var loroom = predicted;
		final var room = (hiroom < loroom ? hiroom : loroom) << 1;
		var val = fit_value[i];
		if (val != 0) {
		    if (val >= room) {
			if (hiroom > loroom) {
			    val = val - loroom;
			} else {
			    val = -1 - (val - hiroom);
			}
		    } else if ((val & 1) != 0) {
			val = -(val + 1 >>> 1);
		    } else {
			val >>= 1;
		    }
		    fit_value[i] = val + predicted;
		    fit_value[look.loneighbor[i - 2]] &= 0x7fff;
		    fit_value[look.hineighbor[i - 2]] &= 0x7fff;
		} else {
		    fit_value[i] = predicted | 0x8000;
		}
	    }
	    return fit_value;
	}
	return null;
    }

    @Override
    int inverse2(final Block vb, final Object i, final Object memo, final float[] out) {
	final var look = (LookFloor1) i;
	final var info = look.vi;
	final var n = vb.vd.vi.blocksizes[vb.mode] / 2;
	if (memo != null) {
	    /* render the lines */
	    final var fit_value = (int[]) memo;
	    var hx = 0;
	    var lx = 0;
	    var ly = fit_value[0] * info.mult;
	    for (var j = 1; j < look.posts; j++) {
		final var current = look.forward_index[j];
		var hy = fit_value[current] & 0x7fff;
		if (hy == fit_value[current]) {
		    hy *= info.mult;
		    hx = info.postlist[current];
		    Floor1.render_line(lx, hx, ly, hy, out);
		    lx = hx;
		    ly = hy;
		}
	    }
	    for (var j = hx; j < n; j++) {
		out[j] *= out[j - 1]; /* be certain */
	    }
	    return 1;
	}
	for (var j = 0; j < n; j++) {
	    out[j] = 0.f;
	}
	return 0;
    }

    @Override
    Object look(final DspState vd, final InfoMode mi, final Object i) {
	var _n = 0;
	final var sortpointer = new int[Floor1.VIF_POSIT + 2];
	// Info vi=vd.vi;
	final var info = (InfoFloor1) i;
	final var look = new LookFloor1();
	look.vi = info;
	look.n = info.postlist[1];
	/*
	 * we drop each position value in-between already decoded values, and use linear
	 * interpolation to predict each new value past the edges. The positions are
	 * read in the order of the position list... we precompute the bounding
	 * positions in the lookup. Of course, the neighbors can change (if a position
	 * is declined), but this is an initial mapping
	 */
	for (var j = 0; j < info.partitions; j++) {
	    _n += info.class_dim[info.partitionclass[j]];
	}
	_n += 2;
	look.posts = _n;
	/* also store a sorted position index */
	for (var j = 0; j < _n; j++) {
	    sortpointer[j] = j;
	}
	// qsort(sortpointer,n,sizeof(int),icomp); // !!
	int foo;
	for (var j = 0; j < _n - 1; j++) {
	    for (var k = j; k < _n; k++) {
		if (info.postlist[sortpointer[j]] > info.postlist[sortpointer[k]]) {
		    foo = sortpointer[k];
		    sortpointer[k] = sortpointer[j];
		    sortpointer[j] = foo;
		}
	    }
	}
	/* points from sort order back to range number */
	System.arraycopy(sortpointer, 0, look.forward_index, 0, _n);
	/* points from range order to sorted position */
	for (var j = 0; j < _n; j++) {
	    look.reverse_index[look.forward_index[j]] = j;
	}
	/* we actually need the post values too */
	for (var j = 0; j < _n; j++) {
	    look.sorted_index[j] = info.postlist[look.forward_index[j]];
	}
	/* quantize values to multiplier spec */
	look.quant_q = switch (info.mult) {
	case 1: /* 1024 -> 256 */
	    yield 256;
	case 2: /* 1024 -> 128 */
	    yield 128;
	case 3: /* 1024 -> 86 */
	    yield 86;
	case 4: /* 1024 -> 64 */
	    yield 64;
	default:
	    yield -1;
	};
	/*
	 * discover our neighbors for decode where we don't use fit flags (that would
	 * push the neighbors outward)
	 */
	for (var j = 0; j < _n - 2; j++) {
	    var lo = 0;
	    var hi = 1;
	    var lx = 0;
	    var hx = look.n;
	    final var currentx = info.postlist[j + 2];
	    for (var k = 0; k < j + 2; k++) {
		final var x = info.postlist[k];
		if (x > lx && x < currentx) {
		    lo = k;
		    lx = x;
		}
		if (x < hx && x > currentx) {
		    hi = k;
		    hx = x;
		}
	    }
	    look.loneighbor[j] = lo;
	    look.hineighbor[j] = hi;
	}
	return look;
    }

    @Override
    void pack(final Object i, final Buffer opb) {
	final var info = (InfoFloor1) i;
	var count = 0;
	int rangebits;
	final var maxposit = info.postlist[1];
	var maxclass = -1;
	/* save out partitions */
	opb.write(info.partitions, 5); /* only 0 to 31 legal */
	for (var j = 0; j < info.partitions; j++) {
	    opb.write(info.partitionclass[j], 4); /* only 0 to 15 legal */
	    if (maxclass < info.partitionclass[j]) {
		maxclass = info.partitionclass[j];
	    }
	}
	/* save out partition classes */
	for (var j = 0; j < maxclass + 1; j++) {
	    opb.write(info.class_dim[j] - 1, 3); /* 1 to 8 */
	    opb.write(info.class_subs[j], 2); /* 0 to 3 */
	    if (info.class_subs[j] != 0) {
		opb.write(info.class_book[j], 8);
	    }
	    for (var k = 0; k < 1 << info.class_subs[j]; k++) {
		opb.write(info.class_subbook[j][k] + 1, 8);
	    }
	}
	/* save out the post list */
	opb.write(info.mult - 1, 2); /* only 1,2,3,4 legal now */
	opb.write(Util.ilog2(maxposit), 4);
	rangebits = Util.ilog2(maxposit);
	for (int j = 0, k = 0; j < info.partitions; j++) {
	    count += info.class_dim[info.partitionclass[j]];
	    for (; k < count; k++) {
		opb.write(info.postlist[k + 2], rangebits);
	    }
	}
    }

    @Override
    Object unpack(final Info vi, final Buffer opb) {
	int count = 0, maxclass = -1, rangebits;
	final var info = new InfoFloor1();
	/* read partitions */
	info.partitions = opb.read(5); /* only 0 to 31 legal */
	for (var j = 0; j < info.partitions; j++) {
	    info.partitionclass[j] = opb.read(4); /* only 0 to 15 legal */
	    if (maxclass < info.partitionclass[j]) {
		maxclass = info.partitionclass[j];
	    }
	}
	/* read partition classes */
	for (var j = 0; j < maxclass + 1; j++) {
	    info.class_dim[j] = opb.read(3) + 1; /* 1 to 8 */
	    info.class_subs[j] = opb.read(2); /* 0,1,2,3 bits */
	    if (info.class_subs[j] < 0) {
		info.free();
		return null;
	    }
	    if (info.class_subs[j] != 0) {
		info.class_book[j] = opb.read(8);
	    }
	    if (info.class_book[j] < 0 || info.class_book[j] >= vi.books) {
		info.free();
		return null;
	    }
	    for (var k = 0; k < 1 << info.class_subs[j]; k++) {
		info.class_subbook[j][k] = opb.read(8) - 1;
		if (info.class_subbook[j][k] < -1 || info.class_subbook[j][k] >= vi.books) {
		    info.free();
		    return null;
		}
	    }
	}
	/* read the post list */
	info.mult = opb.read(2) + 1; /* only 1,2,3,4 legal now */
	rangebits = opb.read(4);
	for (int j = 0, k = 0; j < info.partitions; j++) {
	    count += info.class_dim[info.partitionclass[j]];
	    for (; k < count; k++) {
		final var t = info.postlist[k + 2] = opb.read(rangebits);
		if (t < 0 || t >= 1 << rangebits) {
		    info.free();
		    return null;
		}
	    }
	}
	info.postlist[0] = 0;
	info.postlist[1] = 1 << rangebits;
	return info;
    }
}
