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
class Residue0 extends FuncResidue {
    static class InfoResidue0 {
	// block-partitioned VQ coded straight residue
	int begin;
	int end;
	// first stage (lossless partitioning)
	int grouping; // group n vectors per partition
	int partitions; // possible codebooks for a partition
	int groupbook; // huffbook for partitioning
	int[] secondstages = new int[64]; // expanded out to pointers in lookup
	int[] booklist = new int[256]; // list of second stage books
	// encode-only heuristic settings
	float[] entmax = new float[64]; // book entropy threshholds
	float[] ampmax = new float[64]; // book amp threshholds
	int[] subgrp = new int[64]; // book heuristic subgroup size
	int[] blimit = new int[64]; // subgroup position limits
    }

    static class LookResidue0 {
	InfoResidue0 info;
	int map;
	int parts;
	int stages;
	CodeBook[] fullbooks;
	CodeBook phrasebook;
	int[][] partbooks;
	int partvals;
	int[][] decodemap;
	int postbits;
	int phrasebits;
	int frames;
    }

    private static int[][][] _01inverse_partword = new int[2][][]; // _01inverse
    // is
    // synchronized
    // for
    static int[][] _2inverse_partword = null;

    // re-using partword
    synchronized static int _01inverse(final Block vb, final Object vl, final float[][] in, final int ch,
	    final int decodepart) {
	int i, j, k, l, s;
	final var look = (LookResidue0) vl;
	final var info = look.info;
	// move all this setup out later
	final var samples_per_partition = info.grouping;
	final var partitions_per_word = look.phrasebook.dim;
	final var n = info.end - info.begin;
	final var partvals = n / samples_per_partition;
	final var partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
	if (Residue0._01inverse_partword.length < ch) {
	    Residue0._01inverse_partword = new int[ch][][];
	}
	for (j = 0; j < ch; j++) {
	    if (Residue0._01inverse_partword[j] == null || Residue0._01inverse_partword[j].length < partwords) {
		Residue0._01inverse_partword[j] = new int[partwords][];
	    }
	}
	for (s = 0; s < look.stages; s++) {
	    // each loop decodes on partition codeword containing
	    // partitions_pre_word partitions
	    for (i = 0, l = 0; i < partvals; l++) {
		if (s == 0) {
		    // fetch the partition word for each channel
		    for (j = 0; j < ch; j++) {
			final var temp = look.phrasebook.decode(vb.opb);
			if (temp == -1) {
			    return 0;
			}
			Residue0._01inverse_partword[j][l] = look.decodemap[temp];
			if (Residue0._01inverse_partword[j][l] == null) {
			    return 0;
			}
		    }
		}
		// now we decode residual values for the partitions
		for (k = 0; k < partitions_per_word && i < partvals; k++, i++) {
		    for (j = 0; j < ch; j++) {
			final var offset = info.begin + i * samples_per_partition;
			final var index = Residue0._01inverse_partword[j][l][k];
			if ((info.secondstages[index] & 1 << s) != 0) {
			    final var stagebook = look.fullbooks[look.partbooks[index][s]];
			    if (stagebook != null) {
				if (decodepart == 0) {
				    if (stagebook.decodevs_add(in[j], offset, vb.opb, samples_per_partition) == -1) {
					return 0;
				    }
				} else if ((decodepart == 1) && (stagebook.decodev_add(in[j], offset, vb.opb,
					samples_per_partition) == -1)) {
				    return 0;
				}
			    }
			}
		    }
		}
	    }
	}
	return 0;
    }

    synchronized static int _2inverse(final Block vb, final Object vl, final float[][] in, final int ch) {
	int i, k, l, s;
	final var look = (LookResidue0) vl;
	final var info = look.info;
	// move all this setup out later
	final var samples_per_partition = info.grouping;
	final var partitions_per_word = look.phrasebook.dim;
	final var n = info.end - info.begin;
	final var partvals = n / samples_per_partition;
	final var partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
	if (Residue0._2inverse_partword == null || Residue0._2inverse_partword.length < partwords) {
	    Residue0._2inverse_partword = new int[partwords][];
	}
	for (s = 0; s < look.stages; s++) {
	    for (i = 0, l = 0; i < partvals; l++) {
		if (s == 0) {
		    // fetch the partition word for each channel
		    final var temp = look.phrasebook.decode(vb.opb);
		    if (temp == -1) {
			return 0;
		    }
		    Residue0._2inverse_partword[l] = look.decodemap[temp];
		    if (Residue0._2inverse_partword[l] == null) {
			return 0;
		    }
		}
		// now we decode residual values for the partitions
		for (k = 0; k < partitions_per_word && i < partvals; k++, i++) {
		    final var offset = info.begin + i * samples_per_partition;
		    final var index = Residue0._2inverse_partword[l][k];
		    if ((info.secondstages[index] & 1 << s) != 0) {
			final var stagebook = look.fullbooks[look.partbooks[index][s]];
			if ((stagebook != null)
				&& (stagebook.decodevv_add(in, offset, ch, vb.opb, samples_per_partition) == -1)) {
			    return 0;
			}
		    }
		}
	    }
	}
	return 0;
    }

    @Override
    void free_info(final Object i) {
    }

    @Override
    void free_look(final Object i) {
    }

    @Override
    int inverse(final Block vb, final Object vl, final float[][] in, final int[] nonzero, final int ch) {
	var used = 0;
	for (var i = 0; i < ch; i++) {
	    if (nonzero[i] != 0) {
		in[used] = in[i];
		used++;
	    }
	}
	if (used != 0) {
	    return Residue0._01inverse(vb, vl, in, used, 0);
	}
	return 0;
    }

    @Override
    Object look(final DspState vd, final InfoMode vm, final Object vr) {
	final var info = (InfoResidue0) vr;
	final var look = new LookResidue0();
	var acc = 0;
	int dim;
	var maxstage = 0;
	look.info = info;
	look.map = vm.mapping;
	look.parts = info.partitions;
	look.fullbooks = vd.fullbooks;
	look.phrasebook = vd.fullbooks[info.groupbook];
	dim = look.phrasebook.dim;
	look.partbooks = new int[look.parts][];
	for (var j = 0; j < look.parts; j++) {
	    final var i = info.secondstages[j];
	    final var stages = Util.ilog(i);
	    if (stages != 0) {
		if (stages > maxstage) {
		    maxstage = stages;
		}
		look.partbooks[j] = new int[stages];
		for (var k = 0; k < stages; k++) {
		    if ((i & 1 << k) != 0) {
			look.partbooks[j][k] = info.booklist[acc];
			acc++;
		    }
		}
	    }
	}
	look.partvals = (int) Math.rint(Math.pow(look.parts, dim));
	look.stages = maxstage;
	look.decodemap = new int[look.partvals][];
	for (var j = 0; j < look.partvals; j++) {
	    var val = j;
	    var mult = look.partvals / look.parts;
	    look.decodemap[j] = new int[dim];
	    for (var k = 0; k < dim; k++) {
		final var deco = val / mult;
		val -= deco * mult;
		mult /= look.parts;
		look.decodemap[j][k] = deco;
	    }
	}
	return look;
    }

    @Override
    void pack(final Object vr, final Buffer opb) {
	final var info = (InfoResidue0) vr;
	var acc = 0;
	opb.write(info.begin, 24);
	opb.write(info.end, 24);
	opb.write(info.grouping - 1, 24); /*
					   * residue vectors to group and code with a partitioned book
					   */
	opb.write(info.partitions - 1, 6); /* possible partition choices */
	opb.write(info.groupbook, 8); /* group huffman book */
	/*
	 * secondstages is a bitmask; as encoding progresses pass by pass, a bitmask of
	 * one indicates this partition class has bits to write this pass
	 */
	for (var j = 0; j < info.partitions; j++) {
	    final var i = info.secondstages[j];
	    if (Util.ilog(i) > 3) {
		/* yes, this is a minor hack due to not thinking ahead */
		opb.write(i, 3);
		opb.write(1, 1);
		opb.write(i >>> 3, 5);
	    } else {
		opb.write(i, 4); /* trailing zero */
	    }
	    acc += Util.icount(i);
	}
	for (var j = 0; j < acc; j++) {
	    opb.write(info.booklist[j], 8);
	}
    }

    @Override
    Object unpack(final Info vi, final Buffer opb) {
	var acc = 0;
	final var info = new InfoResidue0();
	info.begin = opb.read(24);
	info.end = opb.read(24);
	info.grouping = opb.read(24) + 1;
	info.partitions = opb.read(6) + 1;
	info.groupbook = opb.read(8);
	for (var j = 0; j < info.partitions; j++) {
	    var cascade = opb.read(3);
	    if (opb.read(1) != 0) {
		cascade |= opb.read(5) << 3;
	    }
	    info.secondstages[j] = cascade;
	    acc += Util.icount(cascade);
	}
	for (var j = 0; j < acc; j++) {
	    info.booklist[j] = opb.read(8);
	}
	if (info.groupbook >= vi.books) {
	    this.free_info(info);
	    return null;
	}
	for (var j = 0; j < acc; j++) {
	    if (info.booklist[j] >= vi.books) {
		this.free_info(info);
		return null;
	    }
	}
	return info;
    }
}
