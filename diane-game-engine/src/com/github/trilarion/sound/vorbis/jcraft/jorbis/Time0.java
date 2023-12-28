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

/**
 *
 */
class Time0 extends FuncTime {
    @Override
    void free_info(final Object i) {
    }

    @Override
    void free_look(final Object i) {
    }

    @Override
    int inverse(final Block vb, final Object i, final float[] in, final float[] out) {
	return 0;
    }

    @Override
    Object look(final DspState vd, final InfoMode mi, final Object i) {
	return "";
    }

    @Override
    void pack(final Object i, final Buffer opb) {
    }

    @Override
    Object unpack(final Info vi, final Buffer opb) {
	return "";
    }
}
