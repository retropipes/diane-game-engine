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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.github.trilarion.sound.sampled.spi.TAudioFileReader;
import com.github.trilarion.sound.util.SoundException;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Packet;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Page;
import com.github.trilarion.sound.vorbis.jcraft.jogg.StreamState;
import com.github.trilarion.sound.vorbis.jcraft.jogg.SyncState;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.Comment;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.Info;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.VorbisFile;
import com.github.trilarion.sound.vorbis.sampled.VorbisAudioFileFormat;

/**
 * This class implements the AudioFileReader class and provides an Ogg Vorbis
 * file reader for use with the Java Sound Service Provider Interface.
 */
public class VorbisAudioFileReader extends TAudioFileReader {
    private static final Logger LOG = Logger.getLogger(VorbisAudioFileReader.class.getName());
    /**
     *
     */
    public static final AudioFormat.Encoding VORBISENC = new AudioFormat.Encoding("VORBISENC");
    /**
     *
     */
    public static final AudioFileFormat.Type OGG_AUDIOFILEFORMAT_TYPE = new AudioFileFormat.Type("OGG", "ogg");
    private static final int INITAL_READ_LENGTH = 64000;
    private static final int MARK_LIMIT = VorbisAudioFileReader.INITAL_READ_LENGTH + 1;
    private SyncState oggSyncState_ = null;
    private StreamState oggStreamState_ = null;
    private Page oggPage_ = null;
    private Packet oggPacket_ = null;
    private Info vorbisInfo = null;
    private Comment vorbisComment = null;
    private final int bufferMultiple_ = 4;
    private byte[] buffer = null;
    private int bytes = 0;
    private int index = 0;
    private InputStream oggBitStream_ = null;

    /**
     *
     */
    public VorbisAudioFileReader() {
	super(VorbisAudioFileReader.MARK_LIMIT, true);
    }

    /**
     * Return the AudioFileFormat from the given file.
     *
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    @Override
    public AudioFileFormat getAudioFileFormat(final File file) throws UnsupportedAudioFileException, IOException {
	VorbisAudioFileReader.LOG.log(Level.FINE, "getAudioFileFormat(File file)");
	try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
	    inputStream.mark(VorbisAudioFileReader.MARK_LIMIT);
	    this.getAudioFileFormat(inputStream);
	    inputStream.reset();
	    // Get Vorbis file info such as length in seconds.
	    final var vf = new VorbisFile(file.getAbsolutePath());
	    return this.getAudioFileFormat(inputStream, (int) file.length(), Math.round(vf.time_total(-1) * 1000));
	} catch (final SoundException e) {
	    throw new IOException(e.getMessage());
	}
    }

    /**
     * Return the AudioFileFormat from the given InputStream.
     *
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    @Override
    public AudioFileFormat getAudioFileFormat(InputStream inputStream)
	    throws UnsupportedAudioFileException, IOException {
	VorbisAudioFileReader.LOG.log(Level.FINE, "getAudioFileFormat(InputStream inputStream)");
	try {
	    if (!inputStream.markSupported()) {
		inputStream = new BufferedInputStream(inputStream);
	    }
	    inputStream.mark(VorbisAudioFileReader.MARK_LIMIT);
	    return this.getAudioFileFormat(inputStream, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED);
	} finally {
	    inputStream.reset();
	}
    }

    /**
     * Return the AudioFileFormat from the given InputStream, length in bytes and
     * length in milliseconds.
     *
     * @param bitStream
     * @param totalms
     * @param mediaLength
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    protected AudioFileFormat getAudioFileFormat(final InputStream bitStream, final int mediaLength, int totalms)
	    throws UnsupportedAudioFileException, IOException {
	final Map<String, Object> aff_properties = new HashMap<>();
	final Map<String, Object> af_properties = new HashMap<>();
	if (totalms == AudioSystem.NOT_SPECIFIED) {
	    totalms = 0;
	}
	if (totalms > 0) {
	    aff_properties.put("duration", (long) totalms * 1000);
	}
	this.oggBitStream_ = bitStream;
	// init jorbis
	this.oggSyncState_ = new SyncState();
	this.oggStreamState_ = new StreamState();
	this.oggPage_ = new Page();
	this.oggPacket_ = new Packet();
	this.vorbisInfo = new Info();
	this.vorbisComment = new Comment();
	this.buffer = null;
	this.bytes = 0;
	this.oggSyncState_.init();
	this.index = 0;
	try {
	    this.readHeaders(aff_properties);
	} catch (final IOException ioe) {
	    VorbisAudioFileReader.LOG.log(Level.FINE, ioe.getMessage());
	    throw new UnsupportedAudioFileException(ioe.getMessage());
	}
	var dmp = this.vorbisInfo.toString();
	VorbisAudioFileReader.LOG.log(Level.FINE, dmp);
	final var ind = dmp.lastIndexOf("bitrate:");
	var minbitrate = -1;
	var nominalbitrate = -1;
	var maxbitrate = -1;
	if (ind != -1) {
	    dmp = dmp.substring(ind + 8);
	    final var st = new StringTokenizer(dmp, ",");
	    if (st.hasMoreTokens()) {
		minbitrate = Integer.parseInt(st.nextToken());
	    }
	    if (st.hasMoreTokens()) {
		nominalbitrate = Integer.parseInt(st.nextToken());
	    }
	    if (st.hasMoreTokens()) {
		maxbitrate = Integer.parseInt(st.nextToken());
	    }
	}
	if (nominalbitrate > 0) {
	    af_properties.put("bitrate", nominalbitrate);
	}
	af_properties.put("vbr", true);
	if (minbitrate > 0) {
	    aff_properties.put("ogg.bitrate.min.bps", minbitrate);
	}
	if (maxbitrate > 0) {
	    aff_properties.put("ogg.bitrate.max.bps", maxbitrate);
	}
	if (nominalbitrate > 0) {
	    aff_properties.put("ogg.bitrate.nominal.bps", nominalbitrate);
	}
	if (this.vorbisInfo.channels > 0) {
	    aff_properties.put("ogg.channels", this.vorbisInfo.channels);
	}
	if (this.vorbisInfo.rate > 0) {
	    aff_properties.put("ogg.frequency.hz", this.vorbisInfo.rate);
	}
	if (mediaLength > 0) {
	    aff_properties.put("ogg.length.bytes", mediaLength);
	}
	aff_properties.put("ogg.version", this.vorbisInfo.version);
	// AudioFormat.Encoding encoding = VorbisEncoding.VORBISENC;
	// AudioFormat format = new VorbisAudioFormat(encoding, vorbisInfo.rate,
	// AudioSystem.NOT_SPECIFIED, vorbisInfo.channels,
	// AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED,
	// true,af_properties);
	// Patch from MS to ensure more SPI compatibility ...
	float frameRate = -1;
	if (nominalbitrate > 0) {
	    frameRate = nominalbitrate / 8;
	} else if (minbitrate > 0) {
	    frameRate = minbitrate / 8;
	}
	// New Patch from MS:
	final var format = new AudioFormat(VorbisAudioFileReader.VORBISENC, this.vorbisInfo.rate,
		AudioSystem.NOT_SPECIFIED, this.vorbisInfo.channels, 1, frameRate, false, af_properties);
	// Patch end
	return new VorbisAudioFileFormat(VorbisAudioFileReader.OGG_AUDIOFILEFORMAT_TYPE, format,
		AudioSystem.NOT_SPECIFIED, mediaLength, aff_properties);
    }

    /**
     * Return the AudioFileFormat from the given InputStream and length in bytes.
     *
     * @param medialength
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    @Override
    public AudioFileFormat getAudioFileFormat(final InputStream inputStream, final long medialength)
	    throws UnsupportedAudioFileException, IOException {
	return this.getAudioFileFormat(inputStream, (int) medialength, AudioSystem.NOT_SPECIFIED);
    }

    /**
     * Return the AudioFileFormat from the given URL.
     *
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    @Override
    public AudioFileFormat getAudioFileFormat(final URL url) throws UnsupportedAudioFileException, IOException {
	VorbisAudioFileReader.LOG.log(Level.FINE, "getAudioFileFormat(URL url)");
	try (var inputStream = url.openStream()) {
	    return this.getAudioFileFormat(inputStream);
	}
    }

    /**
     * Return the AudioInputStream from the given File.
     *
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    @Override
    public AudioInputStream getAudioInputStream(final File file) throws UnsupportedAudioFileException, IOException {
	VorbisAudioFileReader.LOG.log(Level.FINE, "getAudioInputStream(File file)");
	final InputStream inputStream = new FileInputStream(file);
	try {
	    return this.getAudioInputStream(inputStream);
	} catch (UnsupportedAudioFileException | IOException e) {
	    inputStream.close();
	    throw e;
	}
    }

    /**
     * Return the AudioInputStream from the given InputStream.
     *
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    @Override
    public AudioInputStream getAudioInputStream(final InputStream inputStream)
	    throws UnsupportedAudioFileException, IOException {
	VorbisAudioFileReader.LOG.log(Level.FINE, "getAudioInputStream(InputStream inputStream)");
	return this.getAudioInputStream(inputStream, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED);
    }

    /**
     * Return the AudioInputStream from the given InputStream.
     *
     * @param inputStream
     * @param totalms
     * @param medialength
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    public AudioInputStream getAudioInputStream(InputStream inputStream, final int medialength, final int totalms)
	    throws UnsupportedAudioFileException, IOException {
	VorbisAudioFileReader.LOG.log(Level.FINE,
		"getAudioInputStream(InputStream inputStreamint medialength, int totalms)");
	try {
	    if (!inputStream.markSupported()) {
		inputStream = new BufferedInputStream(inputStream);
	    }
	    inputStream.mark(VorbisAudioFileReader.MARK_LIMIT);
	    final var audioFileFormat = this.getAudioFileFormat(inputStream, medialength, totalms);
	    inputStream.reset();
	    return new AudioInputStream(inputStream, audioFileFormat.getFormat(), audioFileFormat.getFrameLength());
	} catch (UnsupportedAudioFileException | IOException e) {
	    inputStream.reset();
	    throw e;
	}
    }

    /**
     * Return the AudioInputStream from the given URL.
     *
     * @return
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @throws java.io.IOException
     */
    @Override
    public AudioInputStream getAudioInputStream(final URL url) throws UnsupportedAudioFileException, IOException {
	VorbisAudioFileReader.LOG.log(Level.FINE, "getAudioInputStream(URL url)");
	final var inputStream = url.openStream();
	try {
	    return this.getAudioInputStream(inputStream);
	} catch (UnsupportedAudioFileException | IOException e) {
	    if (inputStream != null) {
		inputStream.close();
	    }
	    throw e;
	}
    }

    /**
     * Reads from the oggBitStream_ a specified number of Bytes(bufferSize_) worth
     * starting at index and puts them in the specified buffer[].
     *
     * @return the number of bytes read or -1 if error.
     */
    private int readFromStream(final byte[] buffer, final int index, final int bufferSize_) {
	int bytes;
	try {
	    bytes = this.oggBitStream_.read(buffer, index, bufferSize_);
	} catch (final Exception e) {
	    VorbisAudioFileReader.LOG.log(Level.FINE, "Cannot Read Selected Song");
	    bytes = -1;
	}
	return bytes;
    }

    /**
     * Reads headers and comments.
     */
    private void readHeaders(final Map<String, Object> aff_properties) throws IOException {
	VorbisAudioFileReader.LOG.log(Level.FINE, "readHeaders(");
	final var bufferSize_ = this.bufferMultiple_ * 256 * 2;
	this.index = this.oggSyncState_.buffer(bufferSize_);
	this.buffer = this.oggSyncState_.data;
	this.bytes = this.readFromStream(this.buffer, this.index, bufferSize_);
	if (this.bytes == -1) {
	    VorbisAudioFileReader.LOG.log(Level.FINE, "Cannot get any data from selected Ogg bitstream.");
	    throw new IOException("Cannot get any data from selected Ogg bitstream.");
	}
	this.oggSyncState_.wrote(this.bytes);
	if (this.oggSyncState_.pageout(this.oggPage_) != 1) {
	    if (this.bytes < bufferSize_) {
		throw new IOException("EOF");
	    }
	    VorbisAudioFileReader.LOG.log(Level.FINE, "Input does not appear to be an Ogg bitstream.");
	    throw new IOException("Input does not appear to be an Ogg bitstream.");
	}
	this.oggStreamState_.init(this.oggPage_.serialno());
	this.vorbisInfo.init();
	this.vorbisComment.init();
	aff_properties.put("ogg.serial", this.oggPage_.serialno());
	if (this.oggStreamState_.pagein(this.oggPage_) < 0) {
	    // error; stream version mismatch perhaps
	    VorbisAudioFileReader.LOG.log(Level.FINE, "Error reading first page of Ogg bitstream data.");
	    throw new IOException("Error reading first page of Ogg bitstream data.");
	}
	if (this.oggStreamState_.packetout(this.oggPacket_) != 1) {
	    // no page? must not be vorbis
	    VorbisAudioFileReader.LOG.log(Level.FINE, "Error reading initial header packet.");
	    throw new IOException("Error reading initial header packet.");
	}
	if (this.vorbisInfo.synthesis_headerin(this.vorbisComment, this.oggPacket_) < 0) {
	    // error case; not a vorbis header
	    VorbisAudioFileReader.LOG.log(Level.FINE, "This Ogg bitstream does not contain Vorbis audio data.");
	    throw new IOException("This Ogg bitstream does not contain Vorbis audio data.");
	}
	var i = 0;
	while (i < 2) {
	    while (i < 2) {
		var result = this.oggSyncState_.pageout(this.oggPage_);
		if (result == 0) {
		    break;
		} // Need more data
		if (result == 1) {
		    this.oggStreamState_.pagein(this.oggPage_);
		    while (i < 2) {
			result = this.oggStreamState_.packetout(this.oggPacket_);
			if (result == 0) {
			    break;
			}
			if (result == -1) {
			    VorbisAudioFileReader.LOG.log(Level.FINE, "Corrupt secondary header.  Exiting.");
			    throw new IOException("Corrupt secondary header.  Exiting.");
			}
			this.vorbisInfo.synthesis_headerin(this.vorbisComment, this.oggPacket_);
			i++;
		    }
		}
	    }
	    this.index = this.oggSyncState_.buffer(bufferSize_);
	    this.buffer = this.oggSyncState_.data;
	    this.bytes = this.readFromStream(this.buffer, this.index, bufferSize_);
	    if (this.bytes == -1) {
		break;
	    }
	    if (this.bytes == 0 && i < 2) {
		VorbisAudioFileReader.LOG.log(Level.FINE, "End of file before finding all Vorbis headers!");
		throw new IOException("End of file before finding all Vorbis  headers!");
	    }
	    this.oggSyncState_.wrote(this.bytes);
	}
	// Read Ogg Vorbis comments.
	final var ptr = this.vorbisComment.user_comments;
	var c = 0;
	for (final byte[] ptr1 : ptr) {
	    if (ptr1 == null) {
		break;
	    }
	    final var currComment = new String(ptr1, 0, ptr1.length - 1, StandardCharsets.UTF_8).trim();
	    VorbisAudioFileReader.LOG.log(Level.FINE, currComment);
	    if (currComment.toLowerCase(Locale.ENGLISH).startsWith("artist")) {
		aff_properties.put("author", currComment.substring(7));
	    } else if (currComment.toLowerCase(Locale.ENGLISH).startsWith("title")) {
		aff_properties.put("title", currComment.substring(6));
	    } else if (currComment.toLowerCase(Locale.ENGLISH).startsWith("album")) {
		aff_properties.put("album", currComment.substring(6));
	    } else if (currComment.toLowerCase(Locale.ENGLISH).startsWith("date")) {
		aff_properties.put("date", currComment.substring(5));
	    } else if (currComment.toLowerCase(Locale.ENGLISH).startsWith("copyright")) {
		aff_properties.put("copyright", currComment.substring(10));
	    } else if (currComment.toLowerCase(Locale.ENGLISH).startsWith("comment")) {
		aff_properties.put("comment", currComment.substring(8));
	    } else if (currComment.toLowerCase(Locale.ENGLISH).startsWith("genre")) {
		aff_properties.put("ogg.comment.genre", currComment.substring(6));
	    } else if (currComment.toLowerCase(Locale.ENGLISH).startsWith("tracknumber")) {
		aff_properties.put("ogg.comment.track", currComment.substring(12));
	    } else {
		c++;
		aff_properties.put("ogg.comment.ext." + c, currComment);
	    }
	    aff_properties.put("ogg.comment.encodedby", new String(this.vorbisComment.vendor, 0,
		    this.vorbisComment.vendor.length - 1, Charset.forName("US-ASCII")));
	}
    }
}
