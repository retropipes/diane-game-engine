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
abstract class FuncTime {
    public static FuncTime[] time_P = { new Time0() };

    abstract void free_info(Object i);

    abstract void free_look(Object i);

    abstract int inverse(Block vb, Object i, float[] in, float[] out);

    abstract Object look(DspState vd, InfoMode vm, Object i);

    abstract void pack(Object i, Buffer opb);

    abstract Object unpack(Info vi, Buffer opb);
}
