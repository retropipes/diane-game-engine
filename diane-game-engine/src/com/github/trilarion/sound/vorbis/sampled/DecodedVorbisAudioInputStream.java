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
package com.github.trilarion.sound.vorbis.sampled;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import com.github.trilarion.sound.sampled.AsynchronousFilteredAudioInputStream;
import com.github.trilarion.sound.sampled.CircularBuffer;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Packet;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Page;
import com.github.trilarion.sound.vorbis.jcraft.jogg.StreamState;
import com.github.trilarion.sound.vorbis.jcraft.jogg.SyncState;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.Block;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.Comment;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.DspState;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.Info;

/**
 * This class implements the Vorbis decoding.
 */
public class DecodedVorbisAudioInputStream extends AsynchronousFilteredAudioInputStream
	implements CircularBuffer.BufferListener {
    private static final Logger LOG = Logger.getLogger(DecodedVorbisAudioInputStream.class.getName());
    static final int playState_NeedHeaders = 0;
    static final int playState_ReadData = 1;
    static final int playState_WriteData = 2;
    static final int playState_Done = 3;
    static final int playState_BufferFull = 4;
    static final int playState_Corrupt = -1;
    private final InputStream oggBitStream_;
    private SyncState oggSyncState_ = null;
    private StreamState oggStreamState_ = null;
    private Page oggPage_ = null;
    private Packet oggPacket_ = null;
    private Info vorbisInfo = null;
    private Comment vorbisComment = null;
    private DspState vorbisDspState = null;
    private Block vorbisBlock = null;
    private int playState;
    private final int bufferMultiple_ = 4;
    private final int bufferSize_ = this.bufferMultiple_ * 256 * 2;
    private int convsize = this.bufferSize_ * 2;
    private final byte[] convbuffer = new byte[this.convsize];
    private byte[] buffer = null;
    private int bytes = 0;
    private float[][][] _pcmf = null;
    private int[] _index = null;
    private int index = 0;
    private int i = 0;
    // bout is now a global so that we can continue from when we have a buffer
    // full.
    int bout = 0;
    private long currentBytes = 0;

    /**
     * Constructor.
     *
     * @param outputFormat
     * @param bitStream
     */
    public DecodedVorbisAudioInputStream(final AudioFormat outputFormat, final AudioInputStream bitStream) {
	super(outputFormat, -1);
	this.oggBitStream_ = bitStream;
	this.init_jorbis();
	this.index = 0;
	this.playState = DecodedVorbisAudioInputStream.playState_NeedHeaders;
    }

    /**
     * Close the stream.
     *
     * @throws java.io.IOException
     */
    @Override
    public void close() throws IOException {
	super.close();
	this.oggBitStream_.close();
    }

    private void continueFromBufferFull() {
	if (this.getCircularBuffer().availableWrite() < 2 * this.vorbisInfo.channels * this.bout) {
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE,
		    "Too much data in this data packet, better return, let the channel drain, and try again...");
	    // Don't change play state.
	    return;
	}
	this.getCircularBuffer().write(this.convbuffer, 0, 2 * this.vorbisInfo.channels * this.bout);
	// Don't change play state. Let outputSamples change play state, if
	// necessary.
	this.outputSamples();
    }

    /**
     * Main loop.
     */
    @Override
    public void dataReady() {
	switch (this.playState) {
	case playState_NeedHeaders:
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "playState = playState_NeedHeaders");
	    break;
	case playState_ReadData:
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "playState = playState_ReadData");
	    break;
	case playState_WriteData:
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "playState = playState_WriteData");
	    break;
	case playState_Done:
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "playState = playState_Done");
	    break;
	case playState_BufferFull:
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "playState = playState_BufferFull");
	    break;
	case playState_Corrupt:
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "playState = playState_Corrupt");
	    break;
	}
	// This code was developed by the jCraft group, as JOrbisPlayer.java,
	// slightly
	// modified by jOggPlayer developer and adapted by JavaZOOM to suit the
	// JavaSound
	// SPI. Then further modified by Tom Kimpton to correctly play ogg files
	// that
	// would hang the player.
	switch (this.playState) {
	case playState_NeedHeaders:
	    try {
		// Headers (+ Comments).
		this.readHeaders();
	    } catch (final IOException ioe) {
		this.playState = DecodedVorbisAudioInputStream.playState_Corrupt;
		return;
	    }
	    this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
	    break;
	case playState_ReadData:
	    int result;
	    this.index = this.oggSyncState_.buffer(this.bufferSize_);
	    this.buffer = this.oggSyncState_.data;
	    this.bytes = this.readFromStream(this.buffer, this.index, this.bufferSize_);
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "More data : {0}", this.bytes);
	    if (this.bytes == -1) {
		this.playState = DecodedVorbisAudioInputStream.playState_Done;
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE,
			"Ogg Stream empty. Settings playState to playState_Done.");
		break;
	    }
	    this.oggSyncState_.wrote(this.bytes);
	    if (this.bytes == 0) {
		if (this.oggPage_.eos() != 0 || this.oggStreamState_.e_o_s != 0 || this.oggPacket_.e_o_s != 0) {
		    DecodedVorbisAudioInputStream.LOG.log(Level.FINE,
			    "oggSyncState wrote 0 bytes: settings playState to playState_Done.");
		    this.playState = DecodedVorbisAudioInputStream.playState_Done;
		}
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE,
			"oggSyncState wrote 0 bytes: but stream not yet empty.");
		break;
	    }
	    result = this.oggSyncState_.pageout(this.oggPage_);
	    if (result == 0) {
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Setting playState to playState_ReadData.");
		this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
		break;
	    } // need more data
	    if (result == -1) { // missing or corrupt data at this page position
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE,
			"Corrupt or missing data in bitstream; setting playState to playState_ReadData");
		this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
		break;
	    }
	    this.oggStreamState_.pagein(this.oggPage_);
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Setting playState to playState_WriteData.");
	    this.playState = DecodedVorbisAudioInputStream.playState_WriteData;
	    break;
	case playState_WriteData:
	    // Decoding !
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Decoding");
	    label: while (true) {
		result = this.oggStreamState_.packetout(this.oggPacket_);
		switch (result) {
		case 0:
		    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Packetout returned 0, going to read state.");
		    this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
		    break label;
		case -1:
		    // missing or corrupt data at this page position
		    // no reason to complain; already complained above
		    DecodedVorbisAudioInputStream.LOG.log(Level.FINE,
			    "Corrupt or missing data in packetout bitstream; going to read state...");
		    // playState = playState_ReadData;
		    // break;
		    // continue;
		    break;
		default:
		    // we have a packet. Decode it
		    if (this.vorbisBlock.synthesis(this.oggPacket_) != 0) {
			// if(TDebug.TraceAudioConverter)
			// TDebug.out("vorbisBlock.synthesis() returned !0,
			// going to read state");
			DecodedVorbisAudioInputStream.LOG.log(Level.FINE,
				"VorbisBlock.synthesis() returned !0, continuing.");
			continue;
		    }
		    // success!
		    this.vorbisDspState.synthesis_blockin(this.vorbisBlock);
		    this.outputSamples();
		    if (this.playState == DecodedVorbisAudioInputStream.playState_BufferFull) {
			return;
		    }
		    break;
		}
	    } // while(true)
	    if (this.oggPage_.eos() != 0) {
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Settings playState to playState_Done.");
		this.playState = DecodedVorbisAudioInputStream.playState_Done;
	    }
	    break;
	case playState_BufferFull:
	    this.continueFromBufferFull();
	    break;
	case playState_Corrupt:
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Corrupt Song.");
	    // drop through to playState_Done...
	case playState_Done:
	    this.oggStreamState_.clear();
	    this.vorbisBlock.clear();
	    this.vorbisDspState.clear();
	    this.vorbisInfo.clear();
	    this.oggSyncState_.clear();
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Done Song.");
	    try {
		if (this.oggBitStream_ != null) {
		    this.oggBitStream_.close();
		}
		this.getCircularBuffer().close();
	    } catch (final Exception e) {
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE, e.getMessage());
	    }
	    break;
	} // switch
    }

    /**
     * Initializes all the jOrbis and jOgg vars that are used for song playback.
     */
    private void init_jorbis() {
	this.oggSyncState_ = new SyncState();
	this.oggStreamState_ = new StreamState();
	this.oggPage_ = new Page();
	this.oggPacket_ = new Packet();
	this.vorbisInfo = new Info();
	this.vorbisComment = new Comment();
	this.vorbisDspState = new DspState();
	this.vorbisBlock = new Block(this.vorbisDspState);
	this.buffer = null;
	this.bytes = 0;
	this.currentBytes = 0L;
	this.oggSyncState_.init();
    }

    /**
     * This routine was extracted so that when the output buffer fills up, we can
     * break out of the loop, let the music channel drain, then continue from where
     * we were.
     */
    private void outputSamples() {
	int samples;
	while ((samples = this.vorbisDspState.synthesis_pcmout(this._pcmf, this._index)) > 0) {
	    final var pcmf = this._pcmf[0];
	    this.bout = samples < this.convsize ? samples : this.convsize;
	    // convert doubles to 16 bit signed ints (host order) and
	    // interleave
	    for (this.i = 0; this.i < this.vorbisInfo.channels; this.i++) {
		var pointer = this.i * 2;
		// int ptr=i;
		final var mono = this._index[this.i];
		for (var j = 0; j < this.bout; j++) {
		    final var fVal = pcmf[this.i][mono + j] * 32767.;
		    var val = (int) fVal;
		    if (val > 32767) {
			val = 32767;
		    }
		    if (val < -32768) {
			val = -32768;
		    }
		    if (val < 0) {
			val = val | 0x8000;
		    }
		    this.convbuffer[pointer] = (byte) val;
		    this.convbuffer[pointer + 1] = (byte) (val >>> 8);
		    pointer += 2 * this.vorbisInfo.channels;
		}
	    }
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "about to write: {0}",
		    2 * this.vorbisInfo.channels * this.bout);
	    if (this.getCircularBuffer().availableWrite() < 2 * this.vorbisInfo.channels * this.bout) {
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE,
			"Too much data in this data packet, better return, let the channel drain, and try again...");
		this.playState = DecodedVorbisAudioInputStream.playState_BufferFull;
		return;
	    }
	    this.getCircularBuffer().write(this.convbuffer, 0, 2 * this.vorbisInfo.channels * this.bout);
	    if (this.bytes < this.bufferSize_) {
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Finished with final buffer of music?");
	    }
	    if (this.vorbisDspState.synthesis_read(this.bout) != 0) {
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "VorbisDspState.synthesis_read returned -1.");
	    }
	} // while(samples...)
	this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
    }

    /**
     * Reads from the oggBitStream_ a specified number of Bytes(bufferSize_) worth
     * starting at index and puts them in the specified buffer[].
     *
     * @param buffer
     * @param index
     * @param bufferSize_
     * @return the number of bytes read or -1 if error.
     */
    private int readFromStream(final byte[] buffer, final int index, final int bufferSize_) {
	int bytes;
	try {
	    bytes = this.oggBitStream_.read(buffer, index, bufferSize_);
	} catch (final Exception e) {
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Cannot Read Selected Song");
	    bytes = -1;
	}
	this.currentBytes = this.currentBytes + bytes;
	return bytes;
    }

    /**
     * Reads headers and comments.
     */
    private void readHeaders() throws IOException {
	DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "readHeaders(");
	this.index = this.oggSyncState_.buffer(this.bufferSize_);
	this.buffer = this.oggSyncState_.data;
	this.bytes = this.readFromStream(this.buffer, this.index, this.bufferSize_);
	if (this.bytes == -1) {
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Cannot get any data from selected Ogg bitstream.");
	    throw new IOException("Cannot get any data from selected Ogg bitstream.");
	}
	this.oggSyncState_.wrote(this.bytes);
	if (this.oggSyncState_.pageout(this.oggPage_) != 1) {
	    if (this.bytes < this.bufferSize_) {
		throw new IOException("EOF");
	    }
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Input does not appear to be an Ogg bitstream.");
	    throw new IOException("Input does not appear to be an Ogg bitstream.");
	}
	this.oggStreamState_.init(this.oggPage_.serialno());
	this.vorbisInfo.init();
	this.vorbisComment.init();
	if (this.oggStreamState_.pagein(this.oggPage_) < 0) {
	    // error; stream version mismatch perhaps
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Error reading first page of Ogg bitstream data.");
	    throw new IOException("Error reading first page of Ogg bitstream data.");
	}
	if (this.oggStreamState_.packetout(this.oggPacket_) != 1) {
	    // no page? must not be vorbis
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Error reading initial header packet.");
	    throw new IOException("Error reading initial header packet.");
	}
	if (this.vorbisInfo.synthesis_headerin(this.vorbisComment, this.oggPacket_) < 0) {
	    // error case; not a vorbis header
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "This Ogg bitstream does not contain Vorbis audio data.");
	    throw new IOException("This Ogg bitstream does not contain Vorbis audio data.");
	}
	// int i = 0;
	this.i = 0;
	while (this.i < 2) {
	    while (this.i < 2) {
		var result = this.oggSyncState_.pageout(this.oggPage_);
		if (result == 0) {
		    break;
		} // Need more data
		if (result == 1) {
		    this.oggStreamState_.pagein(this.oggPage_);
		    while (this.i < 2) {
			result = this.oggStreamState_.packetout(this.oggPacket_);
			if (result == 0) {
			    break;
			}
			if (result == -1) {
			    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Corrupt secondary header.  Exiting.");
			    throw new IOException("Corrupt secondary header.  Exiting.");
			}
			this.vorbisInfo.synthesis_headerin(this.vorbisComment, this.oggPacket_);
			this.i++;
		    }
		}
	    }
	    this.index = this.oggSyncState_.buffer(this.bufferSize_);
	    this.buffer = this.oggSyncState_.data;
	    this.bytes = this.readFromStream(this.buffer, this.index, this.bufferSize_);
	    if (this.bytes == -1) {
		break;
	    }
	    if (this.bytes == 0 && this.i < 2) {
		DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "End of file before finding all Vorbis headers!");
		throw new IOException("End of file before finding all Vorbis  headers!");
	    }
	    this.oggSyncState_.wrote(this.bytes);
	}
	final var ptr = this.vorbisComment.user_comments;
	for (final byte[] ptr1 : ptr) {
	    if (ptr1 == null) {
		break;
	    }
	    final var currComment = new String(ptr1, 0, ptr1.length - 1, Charset.forName("US-ASCII")).trim();
	    DecodedVorbisAudioInputStream.LOG.log(Level.FINE, "Comment: {0}", currComment);
	}
	this.convsize = this.bufferSize_ / this.vorbisInfo.channels;
	this.vorbisDspState.synthesis_init(this.vorbisInfo);
	this.vorbisBlock.init(this.vorbisDspState);
	this._pcmf = new float[1][][];
	this._index = new int[this.vorbisInfo.channels];
    }
}
