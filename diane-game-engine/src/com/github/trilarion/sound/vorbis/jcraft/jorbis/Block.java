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

import com.github.trilarion.sound.vorbis.jcraft.jogg.Buffer;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Packet;

/**
 *
 */
public class Block {
    /// necessary stream state for linking to the framing abstraction
    float[][] pcm = {}; // this is a pointer into local storage
    Buffer opb = new Buffer();
    int lW;
    int W;
    int nW;
    int pcmend;
    int mode;
    int eofflag;
    long granulepos;
    long sequence;
    DspState vd; // For read-only access of configuration
    // bitmetrics for the frame
    int glue_bits;
    int time_bits;
    int floor_bits;
    int res_bits;

    /**
     *
     * @param vd
     */
    public Block(final DspState vd) {
	this.vd = vd;
	if (vd.analysisp != 0) {
	    this.opb.writeinit();
	}
    }

    /**
     *
     * @return
     */
    public int clear() {
	if ((this.vd != null) && (this.vd.analysisp != 0)) {
	    this.opb.writeclear();
	}
	return 0;
    }

    /**
     *
     * @param vd
     */
    public void init(final DspState vd) {
	this.vd = vd;
    }

    /**
     *
     * @param op
     * @return
     */
    public int synthesis(final Packet op) {
	final var vi = this.vd.vi;
	// first things first. Make sure decode is ready
	this.opb.readinit(op.packet_base, op.packet, op.bytes);
	// Check the packet type
	if (this.opb.read(1) != 0) {
	    // Oops. This is not an audio data packet
	    return -1;
	}
	// read our mode and pre/post windowsize
	final var _mode = this.opb.read(this.vd.modebits);
	if (_mode == -1) {
	    return -1;
	}
	this.mode = _mode;
	this.W = vi.mode_param[this.mode].blockflag;
	if (this.W != 0) {
	    this.lW = this.opb.read(1);
	    this.nW = this.opb.read(1);
	    if (this.nW == -1) {
		return -1;
	    }
	} else {
	    this.lW = 0;
	    this.nW = 0;
	}
	// more setup
	this.granulepos = op.granulepos;
	this.sequence = op.packetno - 3; // first block is third packet
	this.eofflag = op.e_o_s;
	// alloc pcm passback storage
	this.pcmend = vi.blocksizes[this.W];
	if (this.pcm.length < vi.channels) {
	    this.pcm = new float[vi.channels][];
	}
	for (var i = 0; i < vi.channels; i++) {
	    if (this.pcm[i] == null || this.pcm[i].length < this.pcmend) {
		this.pcm[i] = new float[this.pcmend];
	    } else {
		for (var j = 0; j < this.pcmend; j++) {
		    this.pcm[i][j] = 0;
		}
	    }
	}
	// unpack_header enforces range checking
	final var type = vi.map_type[vi.mode_param[this.mode].mapping];
	return FuncMapping.mapping_P[type].inverse(this, this.vd.mode[this.mode]);
    }
}
