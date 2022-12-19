/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.asset.music;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

public class MusicPlayer {
    private static final int SAMPLE_RATE = 41000;
    private static Module module;
    static IBXM ibxm;
    volatile static boolean playing;
    private static int interpolation;
    private static Thread playThread;

    public MusicPlayer() {
	// Do nothing
    }

    synchronized static int getAudio(final int[] mixBuf) {
	return ibxm.getAudio(mixBuf);
    }

    public static boolean isPlaying() {
	return playThread != null && playThread.isAlive();
    }

    public synchronized static void play(final DianeMusicIndex index) throws IOException {
	if (MusicPlayer.isPlaying()) {
	    MusicPlayer.stopPlaying();
	}
	final var source = index.getURL();
	try (var inputStream = source.openStream()) {
	    final var moduleData = inputStream.readAllBytes();
	    MusicPlayer.module = new Module(moduleData);
	    MusicPlayer.ibxm = new IBXM(MusicPlayer.module, MusicPlayer.SAMPLE_RATE);
	    MusicPlayer.ibxm.setInterpolation(MusicPlayer.interpolation);
	    MusicPlayer.playing = true;
	    MusicPlayer.playThread = new Thread(() -> {
		final var mixBuf = new int[MusicPlayer.ibxm.getMixBufferLength()];
		final var outBuf = new byte[mixBuf.length * 4];
		final var audioFormat = new AudioFormat(MusicPlayer.SAMPLE_RATE, 16, 2, true, true);
		try (var audioLine = AudioSystem.getSourceDataLine(audioFormat)) {
		    audioLine.open();
		    audioLine.start();
		    while (MusicPlayer.playing) {
			final var count = MusicPlayer.getAudio(mixBuf);
			var outIdx = 0;
			for (int mixIdx = 0, mixEnd = count * 2; mixIdx < mixEnd; mixIdx++) {
			    var ampl = mixBuf[mixIdx];
			    if (ampl > 32767) {
				ampl = 32767;
			    }
			    if (ampl < -32768) {
				ampl = -32768;
			    }
			    outBuf[outIdx] = (byte) (ampl >> 8);
			    outIdx++;
			    outBuf[outIdx] = (byte) ampl;
			    outIdx++;
			}
			audioLine.write(outBuf, 0, outIdx);
		    }
		    audioLine.drain();
		} catch (final Exception e) {
		    // Ignore
		}
	    });
	    MusicPlayer.playThread.start();
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    public synchronized static void stopPlaying() {
	playing = false;
	try {
	    if (playThread != null) {
		playThread.join();
	    }
	} catch (final InterruptedException e) {
	}
    }
}
