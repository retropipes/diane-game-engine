/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.diane.asset.ogg;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

class OggLoopPlayThread {
    private final AudioInputStream stream;
    private AudioInputStream decodedStream;
    private AudioFormat format;
    private AudioFormat decodedFormat;
    private boolean stop;

    public OggLoopPlayThread(final AudioInputStream ais) {
	this.stream = ais;
	this.stop = false;
    }

    public void play() {
	try {
	    // Get AudioInputStream from given file.
	    this.decodedStream = null;
	    if (this.stream != null) {
		this.format = this.stream.getFormat();
		this.decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.format.getSampleRate(), 16,
			this.format.getChannels(), this.format.getChannels() * 2, this.format.getSampleRate(), false);
		// Get AudioInputStream that will be decoded by underlying
		// VorbisSPI
		this.decodedStream = AudioSystem.getAudioInputStream(this.decodedFormat, this.stream);
	    }
	} catch (final Exception e) {
	    // Do nothing
	}
	final var info = new DataLine.Info(SourceDataLine.class, this.decodedFormat);
	try (var res = AudioSystem.getLine(info); var line = (SourceDataLine) res) {
	    if (line != null) {
		line.open(this.decodedFormat);
		try {
		    final var data = new byte[16];
		    // Start
		    line.start();
		    while (!this.stop) {
			if (this.stop) {
			    return;
			}
			var nBytesRead = 0;
			while (nBytesRead != -1 && !this.stop) {
			    nBytesRead = this.decodedStream.read(data, 0, data.length);
			    if (this.stop) {
				return;
			    }
			    if (nBytesRead != -1) {
				line.write(data, 0, nBytesRead);
			    }
			    if (this.stop) {
				return;
			    }
			}
			if (this.stop) {
			    return;
			}
			// Reset
			this.stream.reset();
		    }
		    // Stop
		    line.stop();
		} catch (final IOException io) {
		    // Do nothing
		} finally {
		    // Stop
		    line.stop();
		}
	    }
	} catch (final LineUnavailableException lue) {
	    // Do nothing
	}
    }

    public void stopPlaying() {
	this.stop = true;
    }
}
