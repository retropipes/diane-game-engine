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
class Mapping0 extends FuncMapping {
    static class InfoMapping0 {
	int submaps; // <= 16
	int[] chmuxlist = new int[256]; // up to 256 channels in a Vorbis stream
	int[] timesubmap = new int[16]; // [mux]
	int[] floorsubmap = new int[16]; // [mux] submap to floors
	int[] residuesubmap = new int[16];// [mux] submap to residue
	int[] psysubmap = new int[16]; // [mux]; encode only
	int coupling_steps;
	int[] coupling_mag = new int[256];
	int[] coupling_ang = new int[256];

	void free() {
	    this.chmuxlist = null;
	    this.timesubmap = null;
	    this.floorsubmap = null;
	    this.residuesubmap = null;
	    this.psysubmap = null;
	    this.coupling_mag = null;
	    this.coupling_ang = null;
	}
    }

    static class LookMapping0 {
	InfoMode mode;
	InfoMapping0 map;
	Object[] time_look;
	Object[] floor_look;
	Object[] floor_state;
	Object[] residue_look;
	PsyLook[] psy_look;
	FuncTime[] time_func;
	FuncFloor[] floor_func;
	FuncResidue[] residue_func;
	int ch;
	float[][] decay;
	int lastframe; // if a different mode is called, we need to
	// invalidate decay and floor state
    }

    static int seq = 0;
    float[][] pcmbundle = null;
    int[] zerobundle = null;
    int[] nonzero = null;
    Object[] floormemo = null;

    @Override
    void free_info(final Object imap) {
    }

    @Override
    void free_look(final Object imap) {
    }

    @Override
    synchronized int inverse(final Block vb, final Object l) {
	final var vd = vb.vd;
	final var vi = vd.vi;
	final var look = (LookMapping0) l;
	final var info = look.map;
	final var mode = look.mode;
	final var n = vb.pcmend = vi.blocksizes[vb.W];
	final var window = vd.window[vb.W][vb.lW][vb.nW][mode.windowtype];
	if (this.pcmbundle == null || this.pcmbundle.length < vi.channels) {
	    this.pcmbundle = new float[vi.channels][];
	    this.nonzero = new int[vi.channels];
	    this.zerobundle = new int[vi.channels];
	    this.floormemo = new Object[vi.channels];
	}
	// time domain information decode (note that applying the
	// information would have to happen later; we'll probably add a
	// function entry to the harness for that later
	// NOT IMPLEMENTED
	// recover the spectral envelope; store it in the PCM vector for now
	for (var i = 0; i < vi.channels; i++) {
	    final var pcm = vb.pcm[i];
	    final var submap = info.chmuxlist[i];
	    this.floormemo[i] = look.floor_func[submap].inverse1(vb, look.floor_look[submap], this.floormemo[i]);
	    if (this.floormemo[i] != null) {
		this.nonzero[i] = 1;
	    } else {
		this.nonzero[i] = 0;
	    }
	    for (var j = 0; j < n / 2; j++) {
		pcm[j] = 0;
	    }
	}
	for (var i = 0; i < info.coupling_steps; i++) {
	    if (this.nonzero[info.coupling_mag[i]] != 0 || this.nonzero[info.coupling_ang[i]] != 0) {
		this.nonzero[info.coupling_mag[i]] = 1;
		this.nonzero[info.coupling_ang[i]] = 1;
	    }
	}
	// recover the residue, apply directly to the spectral envelope
	for (var i = 0; i < info.submaps; i++) {
	    var ch_in_bundle = 0;
	    for (var j = 0; j < vi.channels; j++) {
		if (info.chmuxlist[j] == i) {
		    if (this.nonzero[j] != 0) {
			this.zerobundle[ch_in_bundle] = 1;
		    } else {
			this.zerobundle[ch_in_bundle] = 0;
		    }
		    this.pcmbundle[ch_in_bundle] = vb.pcm[j];
		    ch_in_bundle++;
		}
	    }
	    look.residue_func[i].inverse(vb, look.residue_look[i], this.pcmbundle, this.zerobundle, ch_in_bundle);
	}
	for (var i = info.coupling_steps - 1; i >= 0; i--) {
	    final var pcmM = vb.pcm[info.coupling_mag[i]];
	    final var pcmA = vb.pcm[info.coupling_ang[i]];
	    for (var j = 0; j < n / 2; j++) {
		final var mag = pcmM[j];
		final var ang = pcmA[j];
		if (mag > 0) {
		    if (ang > 0) {
			pcmM[j] = mag;
			pcmA[j] = mag - ang;
		    } else {
			pcmA[j] = mag;
			pcmM[j] = mag + ang;
		    }
		} else if (ang > 0) {
		    pcmM[j] = mag;
		    pcmA[j] = mag + ang;
		} else {
		    pcmA[j] = mag;
		    pcmM[j] = mag - ang;
		}
	    }
	}
	// /* compute and apply spectral envelope */
	for (var i = 0; i < vi.channels; i++) {
	    final var pcm = vb.pcm[i];
	    final var submap = info.chmuxlist[i];
	    look.floor_func[submap].inverse2(vb, look.floor_look[submap], this.floormemo[i], pcm);
	}
	// transform the PCM data; takes PCM vector, vb; modifies PCM vector
	// only MDCT right now....
	for (var i = 0; i < vi.channels; i++) {
	    final var pcm = vb.pcm[i];
	    // _analysis_output("out",seq+i,pcm,n/2,0,0);
	    ((Mdct) vd.transform[vb.W][0]).backward(pcm, pcm);
	}
	// now apply the decoded pre-window time information
	// NOT IMPLEMENTED
	// window the data
	for (var i = 0; i < vi.channels; i++) {
	    final var pcm = vb.pcm[i];
	    if (this.nonzero[i] != 0) {
		for (var j = 0; j < n; j++) {
		    pcm[j] *= window[j];
		}
	    } else {
		for (var j = 0; j < n; j++) {
		    pcm[j] = 0.f;
		}
	    }
	}
	// now apply the decoded post-window time information
	// NOT IMPLEMENTED
	// all done!
	return 0;
    }

    @Override
    Object look(final DspState vd, final InfoMode vm, final Object m) {
	// System.err.println("Mapping0.look");
	final var vi = vd.vi;
	final var look = new LookMapping0();
	final var info = look.map = (InfoMapping0) m;
	look.mode = vm;
	look.time_look = new Object[info.submaps];
	look.floor_look = new Object[info.submaps];
	look.residue_look = new Object[info.submaps];
	look.time_func = new FuncTime[info.submaps];
	look.floor_func = new FuncFloor[info.submaps];
	look.residue_func = new FuncResidue[info.submaps];
	for (var i = 0; i < info.submaps; i++) {
	    final var timenum = info.timesubmap[i];
	    final var floornum = info.floorsubmap[i];
	    final var resnum = info.residuesubmap[i];
	    look.time_func[i] = FuncTime.time_P[vi.time_type[timenum]];
	    look.time_look[i] = look.time_func[i].look(vd, vm, vi.time_param[timenum]);
	    look.floor_func[i] = FuncFloor.floor_P[vi.floor_type[floornum]];
	    look.floor_look[i] = look.floor_func[i].look(vd, vm, vi.floor_param[floornum]);
	    look.residue_func[i] = FuncResidue.residue_P[vi.residue_type[resnum]];
	    look.residue_look[i] = look.residue_func[i].look(vd, vm, vi.residue_param[resnum]);
	}
	if (vi.psys != 0 && vd.analysisp != 0) {
	    // ??
	}
	look.ch = vi.channels;
	return look;
    }

    @Override
    void pack(final Info vi, final Object imap, final Buffer opb) {
	final var info = (InfoMapping0) imap;
	/*
	 * another 'we meant to do it this way' hack... up to beta 4, we packed 4 binary
	 * zeros here to signify one submapping in use. We now redefine that to mean
	 * four bitflags that indicate use of deeper features; bit0:submappings,
	 * bit1:coupling, bit2,3:reserved. This is backward compatable with all actual
	 * uses of the beta code.
	 */
	if (info.submaps > 1) {
	    opb.write(1, 1);
	    opb.write(info.submaps - 1, 4);
	} else {
	    opb.write(0, 1);
	}
	if (info.coupling_steps > 0) {
	    opb.write(1, 1);
	    opb.write(info.coupling_steps - 1, 8);
	    for (var i = 0; i < info.coupling_steps; i++) {
		opb.write(info.coupling_mag[i], Util.ilog2(vi.channels));
		opb.write(info.coupling_ang[i], Util.ilog2(vi.channels));
	    }
	} else {
	    opb.write(0, 1);
	}
	opb.write(0, 2); /* 2,3:reserved */
	/* we don't write the channel submappings if we only have one... */
	if (info.submaps > 1) {
	    for (var i = 0; i < vi.channels; i++) {
		opb.write(info.chmuxlist[i], 4);
	    }
	}
	for (var i = 0; i < info.submaps; i++) {
	    opb.write(info.timesubmap[i], 8);
	    opb.write(info.floorsubmap[i], 8);
	    opb.write(info.residuesubmap[i], 8);
	}
    }

    // also responsible for range checking
    @Override
    Object unpack(final Info vi, final Buffer opb) {
	final var info = new InfoMapping0();
	if (opb.read(1) != 0) {
	    info.submaps = opb.read(4) + 1;
	} else {
	    info.submaps = 1;
	}
	if (opb.read(1) != 0) {
	    info.coupling_steps = opb.read(8) + 1;
	    for (var i = 0; i < info.coupling_steps; i++) {
		final var testM = info.coupling_mag[i] = opb.read(Util.ilog2(vi.channels));
		final var testA = info.coupling_ang[i] = opb.read(Util.ilog2(vi.channels));
		if (testM < 0 || testA < 0 || testM == testA || testM >= vi.channels || testA >= vi.channels) {
		    // goto err_out;
		    info.free();
		    return null;
		}
	    }
	}
	if (opb.read(2) > 0) { /* 2,3:reserved */
	    info.free();
	    return null;
	}
	if (info.submaps > 1) {
	    for (var i = 0; i < vi.channels; i++) {
		info.chmuxlist[i] = opb.read(4);
		if (info.chmuxlist[i] >= info.submaps) {
		    info.free();
		    return null;
		}
	    }
	}
	for (var i = 0; i < info.submaps; i++) {
	    info.timesubmap[i] = opb.read(8);
	    if (info.timesubmap[i] >= vi.times) {
		info.free();
		return null;
	    }
	    info.floorsubmap[i] = opb.read(8);
	    if (info.floorsubmap[i] >= vi.floors) {
		info.free();
		return null;
	    }
	    info.residuesubmap[i] = opb.read(8);
	    if (info.residuesubmap[i] >= vi.residues) {
		info.free();
		return null;
	    }
	}
	return info;
    }
}
