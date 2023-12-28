/*
 * Copyright (C) 2004 - 2008 JavaZOOM : vorbisspi@javazoom.net
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
package com.github.trilarion.sound.vorbis.sampled.spi;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import com.github.trilarion.sound.sampled.spi.MatrixFormatConversionProvider;
import com.github.trilarion.sound.vorbis.sampled.DecodedVorbisAudioInputStream;

/**
 * ConversionProvider for VORBIS files.
 */
public class VorbisFormatConversionProvider extends MatrixFormatConversionProvider {
    private static final AudioFormat[] INPUT_FORMATS = {
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 32000.0F, -1, 1, -1, -1, false), // 0
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 32000.0F, -1, 2, -1, -1, false), // 1
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 44100.0F, -1, 1, -1, -1, false), // 2
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 44100.0F, -1, 2, -1, -1, false), // 3
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 48000.0F, -1, 1, -1, -1, false), // 4
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 48000.0F, -1, 2, -1, -1, false), // 5
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 16000.0F, -1, 1, -1, -1, false), // 18
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 16000.0F, -1, 2, -1, -1, false), // 19
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 22050.0F, -1, 1, -1, -1, false), // 20
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 22050.0F, -1, 2, -1, -1, false), // 21
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 24000.0F, -1, 1, -1, -1, false), // 22
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 24000.0F, -1, 2, -1, -1, false), // 23
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 8000.0F, -1, 1, -1, -1, false), // 36
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 8000.0F, -1, 2, -1, -1, false), // 37
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 11025.0F, -1, 1, -1, -1, false), // 38
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 11025.0F, -1, 2, -1, -1, false), // 39
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 12000.0F, -1, 1, -1, -1, false), // 40
	    new AudioFormat(VorbisAudioFileReader.VORBISENC, 12000.0F, -1, 2, -1, -1, false), // 41
    };
    private static final AudioFormat[] OUTPUT_FORMATS = { new AudioFormat(8000.0F, 16, 1, true, false), // 0
	    new AudioFormat(8000.0F, 16, 1, true, true), // 1
	    new AudioFormat(8000.0F, 16, 2, true, false), // 2
	    new AudioFormat(8000.0F, 16, 2, true, true), // 3
	    new AudioFormat(11025.0F, 16, 1, true, false), // 4
	    new AudioFormat(11025.0F, 16, 1, true, true), // 5
	    new AudioFormat(11025.0F, 16, 2, true, false), // 6
	    new AudioFormat(11025.0F, 16, 2, true, true), // 7
	    new AudioFormat(12000.0F, 16, 1, true, false), // 8
	    new AudioFormat(12000.0F, 16, 1, true, true), // 9
	    new AudioFormat(12000.0F, 16, 2, true, false), // 10
	    new AudioFormat(12000.0F, 16, 2, true, true), // 11
	    new AudioFormat(16000.0F, 16, 1, true, false), // 12
	    new AudioFormat(16000.0F, 16, 1, true, true), // 13
	    new AudioFormat(16000.0F, 16, 2, true, false), // 14
	    new AudioFormat(16000.0F, 16, 2, true, true), // 15
	    new AudioFormat(22050.0F, 16, 1, true, false), // 16
	    new AudioFormat(22050.0F, 16, 1, true, true), // 17
	    new AudioFormat(22050.0F, 16, 2, true, false), // 18
	    new AudioFormat(22050.0F, 16, 2, true, true), // 19
	    new AudioFormat(24000.0F, 16, 1, true, false), // 20
	    new AudioFormat(24000.0F, 16, 1, true, true), // 21
	    new AudioFormat(24000.0F, 16, 2, true, false), // 22
	    new AudioFormat(24000.0F, 16, 2, true, true), // 23
	    new AudioFormat(32000.0F, 16, 1, true, false), // 24
	    new AudioFormat(32000.0F, 16, 1, true, true), // 25
	    new AudioFormat(32000.0F, 16, 2, true, false), // 26
	    new AudioFormat(32000.0F, 16, 2, true, true), // 27
	    new AudioFormat(44100.0F, 16, 1, true, false), // 28
	    new AudioFormat(44100.0F, 16, 1, true, true), // 29
	    new AudioFormat(44100.0F, 16, 2, true, false), // 30
	    new AudioFormat(44100.0F, 16, 2, true, true), // 31
	    new AudioFormat(48000.0F, 16, 1, true, false), // 32
	    new AudioFormat(48000.0F, 16, 1, true, true), // 33
	    new AudioFormat(48000.0F, 16, 2, true, false), // 34
	    new AudioFormat(48000.0F, 16, 2, true, true), // 35
    };
    private static final boolean t = true;
    private static final boolean f = false;
    /*
     * One row for each source format.
     */
    private static final boolean[][] CONVERSIONS = { { VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
	    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
	    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f }, // 0
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 1
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 2
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 3
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 4
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t }, // 5
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 18
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 19
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 20
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 21
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 22
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 23
	    { VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 36
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 37
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 38
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 39
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 40
	    { VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.t,
		    VorbisFormatConversionProvider.t, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f, VorbisFormatConversionProvider.f,
		    VorbisFormatConversionProvider.f }, // 41
    };

    /**
     * Constructor.
     */
    public VorbisFormatConversionProvider() {
	super(Arrays.asList(VorbisFormatConversionProvider.INPUT_FORMATS),
		Arrays.asList(VorbisFormatConversionProvider.OUTPUT_FORMATS),
		VorbisFormatConversionProvider.CONVERSIONS);
    }

    /**
     * Returns converted AudioInputStream.
     *
     * @param audioInputStream
     * @return
     */
    @Override
    public AudioInputStream getAudioInputStream(final AudioFormat targetFormat,
	    final AudioInputStream audioInputStream) {
	if (this.isConversionSupported(targetFormat, audioInputStream.getFormat())) {
	    return new DecodedVorbisAudioInputStream(targetFormat, audioInputStream);
	}
	throw new IllegalArgumentException("conversion not supported");
    }
}
