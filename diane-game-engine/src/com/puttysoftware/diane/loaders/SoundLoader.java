/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.asset.DianeSoundIndex;

public class SoundLoader {
    private static final int BUFFER_SIZE = 4096; // 4Kb

    public static void play(final DianeSoundIndex sound) {
	SoundLoader.play(sound.getSoundURL());
    }

    public static void play(final URL sound) {
	new Thread() {
	    @Override
	    public void run() {
		try (var audioInputStream = AudioSystem.getAudioInputStream(sound)) {
		    final var format = audioInputStream.getFormat();
		    final var info = new DataLine.Info(SourceDataLine.class, format);
		    try (var line = AudioSystem.getLine(info); var auline = (SourceDataLine) line) {
			auline.open(format);
			auline.start();
			var nBytesRead = 0;
			final var abData = new byte[SoundLoader.BUFFER_SIZE];
			try {
			    while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0) {
				    auline.write(abData, 0, nBytesRead);
				}
			    }
			} catch (final IOException e) {
			    Diane.handleError(e);
			} finally {
			    auline.drain();
			}
		    } catch (final LineUnavailableException e) {
			Diane.handleError(e);
		    }
		} catch (final UnsupportedAudioFileException | IOException e) {
		    Diane.handleError(e);
		}
	    }
	}.start();
    }

    private SoundLoader() {
	// Do nothing
    }
}