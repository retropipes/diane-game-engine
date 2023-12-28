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

/**
 *
 */
public class DspState {
    static final float M_PI = 3.1415926539f;
    static final int VI_TRANSFORMB = 1;
    static final int VI_WINDOWB = 1;

    static float[] window(final int type, final int window, final int left, final int right) {
	final var ret = new float[window];
	switch (type) {
	case 0: // The 'vorbis window' (window 0) is sin(sin(x)*sin(x)*2pi)
	{
	    final var leftbegin = window / 4 - left / 2;
	    final var rightbegin = window - window / 4 - right / 2;
	    for (var i = 0; i < left; i++) {
		var x = (float) ((i + .5) / left * DspState.M_PI / 2.);
		x = (float) Math.sin(x);
		x *= x;
		x *= DspState.M_PI / 2.;
		x = (float) Math.sin(x);
		ret[i + leftbegin] = x;
	    }
	    for (var i = leftbegin + left; i < rightbegin; i++) {
		ret[i] = 1.f;
	    }
	    for (var i = 0; i < right; i++) {
		var x = (float) ((right - i - .5) / right * DspState.M_PI / 2.);
		x = (float) Math.sin(x);
		x *= x;
		x *= DspState.M_PI / 2.;
		x = (float) Math.sin(x);
		ret[i + rightbegin] = x;
	    }
	}
	    break;
	default:
	    // free(ret);
	    return null;
	}
	return ret;
    }

    int analysisp;
    Info vi;
    int modebits;
    float[][] pcm;
    int pcm_storage;
    int pcm_current;
    int pcm_returned;
    float[] multipliers;
    int envelope_storage;
    int envelope_current;
    int eofflag;
    int lW;
    int W;
    int nW;
    int centerW;
    long granulepos;
    long sequence;
    long glue_bits;
    long time_bits;
    long floor_bits;
    long res_bits;
    // local lookup storage
    float[][][][][] window; // block, leadin, leadout, type
    Object[][] transform;
    CodeBook[] fullbooks;
    // backend lookups are tied to the mode, not the backend or naked mapping
    Object[] mode;
    // local storage, only used on the encoding side. This way the
    // application does not need to worry about freeing some packets'
    // memory and not others'; packet storage is always tracked.
    // Cleared next call to a _dsp_ function
    byte[] header;
    byte[] header1;
    byte[] header2;

    /**
     *
     */
    public DspState() {
	this.transform = new Object[2][];
	this.window = new float[2][][][][];
	this.window[0] = new float[2][][][];
	this.window[0][0] = new float[2][][];
	this.window[0][1] = new float[2][][];
	this.window[0][0][0] = new float[2][];
	this.window[0][0][1] = new float[2][];
	this.window[0][1][0] = new float[2][];
	this.window[0][1][1] = new float[2][];
	this.window[1] = new float[2][][][];
	this.window[1][0] = new float[2][][];
	this.window[1][1] = new float[2][][];
	this.window[1][0][0] = new float[2][];
	this.window[1][0][1] = new float[2][];
	this.window[1][1][0] = new float[2][];
	this.window[1][1][1] = new float[2][];
    }

    DspState(final Info vi) {
	this();
	this.init(vi, false);
	// Adjust centerW to allow an easier mechanism for determining output
	this.pcm_returned = this.centerW;
	this.centerW -= vi.blocksizes[this.W] / 4 + vi.blocksizes[this.lW] / 4;
	this.granulepos = -1;
	this.sequence = -1;
    }

    /**
     *
     */
    public void clear() {
    }

    // Analysis side code, but directly related to blocking. Thus it's
    // here and not in analysis.c (which is for analysis transforms only).
    // The init is here because some of it is shared
    final int init(final Info vi, final boolean encp) {
	this.vi = vi;
	this.modebits = Util.ilog2(vi.modes);
	this.transform[0] = new Object[DspState.VI_TRANSFORMB];
	this.transform[1] = new Object[DspState.VI_TRANSFORMB];
	// MDCT is tranform 0
	this.transform[0][0] = new Mdct();
	this.transform[1][0] = new Mdct();
	((Mdct) this.transform[0][0]).init(vi.blocksizes[0]);
	((Mdct) this.transform[1][0]).init(vi.blocksizes[1]);
	this.window[0][0][0] = new float[DspState.VI_WINDOWB][];
	this.window[0][0][1] = this.window[0][0][0];
	this.window[0][1][0] = this.window[0][0][0];
	this.window[0][1][1] = this.window[0][0][0];
	this.window[1][0][0] = new float[DspState.VI_WINDOWB][];
	this.window[1][0][1] = new float[DspState.VI_WINDOWB][];
	this.window[1][1][0] = new float[DspState.VI_WINDOWB][];
	this.window[1][1][1] = new float[DspState.VI_WINDOWB][];
	for (var i = 0; i < DspState.VI_WINDOWB; i++) {
	    this.window[0][0][0][i] = DspState.window(i, vi.blocksizes[0], vi.blocksizes[0] / 2, vi.blocksizes[0] / 2);
	    this.window[1][0][0][i] = DspState.window(i, vi.blocksizes[1], vi.blocksizes[0] / 2, vi.blocksizes[0] / 2);
	    this.window[1][0][1][i] = DspState.window(i, vi.blocksizes[1], vi.blocksizes[0] / 2, vi.blocksizes[1] / 2);
	    this.window[1][1][0][i] = DspState.window(i, vi.blocksizes[1], vi.blocksizes[1] / 2, vi.blocksizes[0] / 2);
	    this.window[1][1][1][i] = DspState.window(i, vi.blocksizes[1], vi.blocksizes[1] / 2, vi.blocksizes[1] / 2);
	}
	this.fullbooks = new CodeBook[vi.books];
	for (var i = 0; i < vi.books; i++) {
	    this.fullbooks[i] = new CodeBook();
	    this.fullbooks[i].init_decode(vi.book_param[i]);
	}
	// initialize the storage vectors to a decent size greater than the
	// minimum
	this.pcm_storage = 8192; // we'll assume later that we have
	// a minimum of twice the blocksize of
	// accumulated samples in analysis
	this.pcm = new float[vi.channels][];
	{
	    for (var i = 0; i < vi.channels; i++) {
		this.pcm[i] = new float[this.pcm_storage];
	    }
	}
	// all 1 (large block) or 0 (small block)
	// explicitly set for the sake of clarity
	this.lW = 0; // previous window size
	this.W = 0; // current window size
	// all vector indexes; multiples of samples_per_envelope_step
	this.centerW = vi.blocksizes[1] / 2;
	this.pcm_current = this.centerW;
	// initialize all the mapping/backend lookups
	this.mode = new Object[vi.modes];
	for (var i = 0; i < vi.modes; i++) {
	    final var mapnum = vi.mode_param[i].mapping;
	    final var maptype = vi.map_type[mapnum];
	    this.mode[i] = FuncMapping.mapping_P[maptype].look(this, vi.mode_param[i], vi.map_param[mapnum]);
	}
	return 0;
    }

    // Unike in analysis, the window is only partially applied for each
    // block. The time domain envelope is not yet handled at the point of
    // calling (as it relies on the previous block).
    /**
     *
     * @param vb
     * @return
     */
    public int synthesis_blockin(final Block vb) {
	// Shift out any PCM/multipliers that we returned previously
	// centerW is currently the center of the last block added
	if (this.centerW > this.vi.blocksizes[1] / 2 && this.pcm_returned > 8192) {
	    // don't shift too much; we need to have a minimum PCM buffer of
	    // 1/2 long block
	    var shiftPCM = this.centerW - this.vi.blocksizes[1] / 2;
	    shiftPCM = this.pcm_returned < shiftPCM ? this.pcm_returned : shiftPCM;
	    this.pcm_current -= shiftPCM;
	    this.centerW -= shiftPCM;
	    this.pcm_returned -= shiftPCM;
	    if (shiftPCM != 0) {
		for (var i = 0; i < this.vi.channels; i++) {
		    System.arraycopy(this.pcm[i], shiftPCM, this.pcm[i], 0, this.pcm_current);
		}
	    }
	}
	this.lW = this.W;
	this.W = vb.W;
	this.nW = -1;
	this.glue_bits += vb.glue_bits;
	this.time_bits += vb.time_bits;
	this.floor_bits += vb.floor_bits;
	this.res_bits += vb.res_bits;
	if (this.sequence + 1 != vb.sequence) {
	    this.granulepos = -1; // out of sequence; lose count
	}
	this.sequence = vb.sequence;
	{
	    final var sizeW = this.vi.blocksizes[this.W];
	    var _centerW = this.centerW + this.vi.blocksizes[this.lW] / 4 + sizeW / 4;
	    final var beginW = _centerW - sizeW / 2;
	    final var endW = beginW + sizeW;
	    var beginSl = 0;
	    var endSl = 0;
	    // Do we have enough PCM/mult storage for the block?
	    if (endW > this.pcm_storage) {
		// expand the storage
		this.pcm_storage = endW + this.vi.blocksizes[1];
		for (var i = 0; i < this.vi.channels; i++) {
		    final var foo = new float[this.pcm_storage];
		    System.arraycopy(this.pcm[i], 0, foo, 0, this.pcm[i].length);
		    this.pcm[i] = foo;
		}
	    }
	    // overlap/add PCM
	    switch (this.W) {
	    case 0:
		beginSl = 0;
		endSl = this.vi.blocksizes[0] / 2;
		break;
	    case 1:
		beginSl = this.vi.blocksizes[1] / 4 - this.vi.blocksizes[this.lW] / 4;
		endSl = beginSl + this.vi.blocksizes[this.lW] / 2;
		break;
	    }
	    for (var j = 0; j < this.vi.channels; j++) {
		final var _pcm = beginW;
		// the overlap/add section
		var i = 0;
		for (i = beginSl; i < endSl; i++) {
		    this.pcm[j][_pcm + i] += vb.pcm[j][i];
		}
		// the remaining section
		for (; i < sizeW; i++) {
		    this.pcm[j][_pcm + i] = vb.pcm[j][i];
		}
	    }
	    // track the frame number... This is for convenience, but also
	    // making sure our last packet doesn't end with added padding. If
	    // the last packet is partial, the number of samples we'll have to
	    // return will be past the vb->granulepos.
	    //
	    // This is not foolproof! It will be confused if we begin
	    // decoding at the last page after a seek or hole. In that case,
	    // we don't have a starting point to judge where the last frame
	    // is. For this reason, vorbisfile will always try to make sure
	    // it reads the last two marked pages in proper sequence
	    if (this.granulepos == -1) {
		this.granulepos = vb.granulepos;
	    } else {
		this.granulepos += _centerW - this.centerW;
		if (vb.granulepos != -1 && this.granulepos != vb.granulepos) {
		    if (this.granulepos > vb.granulepos && vb.eofflag != 0) {
			// partial last frame. Strip the padding off
			_centerW -= this.granulepos - vb.granulepos;
		    } // else{ Shouldn't happen *unless* the bitstream is out of
		      // spec. Either way, believe the bitstream }
		    this.granulepos = vb.granulepos;
		}
	    }
	    // Update, cleanup
	    this.centerW = _centerW;
	    this.pcm_current = endW;
	    if (vb.eofflag != 0) {
		this.eofflag = 1;
	    }
	}
	return 0;
    }

    /**
     *
     * @param vi
     * @return
     */
    public int synthesis_init(final Info vi) {
	this.init(vi, false);
	// Adjust centerW to allow an easier mechanism for determining output
	this.pcm_returned = this.centerW;
	this.centerW -= vi.blocksizes[this.W] / 4 + vi.blocksizes[this.lW] / 4;
	this.granulepos = -1;
	this.sequence = -1;
	return 0;
    }

    // pcm==NULL indicates we just want the pending samples, no more
    /**
     *
     * @param _pcm
     * @param index
     * @return
     */
    public int synthesis_pcmout(final float[][][] _pcm, final int[] index) {
	if (this.pcm_returned < this.centerW) {
	    if (_pcm != null) {
		for (var i = 0; i < this.vi.channels; i++) {
		    index[i] = this.pcm_returned;
		}
		_pcm[0] = this.pcm;
	    }
	    return this.centerW - this.pcm_returned;
	}
	return 0;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public int synthesis_read(final int bytes) {
	if (bytes != 0 && this.pcm_returned + bytes > this.centerW) {
	    return -1;
	}
	this.pcm_returned += bytes;
	return 0;
    }
}
