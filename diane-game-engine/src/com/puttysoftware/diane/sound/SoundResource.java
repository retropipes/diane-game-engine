/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.sound;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

class SoundResource {
    private final URL soundURL;

    public SoundResource(final URL resURL) {
	this.soundURL = resURL;
    }

    public void play() {
	if (this.soundURL != null) {
	    new Thread() {
		@Override
		public void run() {
		    try (var inputStream = SoundResource.this.soundURL.openStream()) {
			try (var audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
			    final var format = audioInputStream.getFormat();
			    final var info = new DataLine.Info(SourceDataLine.class, format);
			    try (var res = AudioSystem.getLine(info); var line = (SourceDataLine) res) {
				if (line != null) {
				    line.open(format);
				    line.start();
				    var nBytesRead = 0;
				    final var abData = new byte[SoundPlayer.EXTERNAL_BUFFER_SIZE];
				    try {
					while (nBytesRead != -1) {
					    nBytesRead = audioInputStream.read(abData, 0, abData.length);
					    if (nBytesRead >= 0) {
						line.write(abData, 0, nBytesRead);
					    }
					}
				    } catch (final IOException e) {
					throw new SoundException(e);
				    } finally {
					line.drain();
				    }
				}
			    } catch (final LineUnavailableException e) {
				throw new SoundException(e);
			    }
			} catch (final UnsupportedAudioFileException | IOException e) {
			    throw new SoundException(e);
			}
		    } catch (final IOException e) {
			throw new SoundException(e);
		    }
		}
	    }.start();
	}
    }
}
