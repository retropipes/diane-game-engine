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
class Residue1 extends Residue0 {
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
	    return Residue0._01inverse(vb, vl, in, used, 1);
	}
	return 0;
    }
}
