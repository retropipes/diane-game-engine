/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.music;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

public class DianeMusicPlayer {
	private static final int SAMPLE_RATE = 41000;
	private static Module module;
	static IBXM ibxm;
	volatile static boolean playing;
	private static int interpolation;
	private static Thread playThread;

	public DianeMusicPlayer() {
		// Do nothing
	}

	synchronized static int getAudio(final int[] mixBuf) {
		return ibxm.getAudio(mixBuf);
	}

	public static boolean isPlaying() {
		return playThread != null && playThread.isAlive();
	}

	public synchronized static void play(DianeMusicIndex index) throws IOException {
		if (DianeMusicPlayer.isPlaying()) {
			DianeMusicPlayer.stopPlaying();
		}
		var source = index.getURL();
		try (var inputStream = source.openStream()) {
			final var moduleData = inputStream.readAllBytes();
			DianeMusicPlayer.module = new Module(moduleData);
			DianeMusicPlayer.ibxm = new IBXM(DianeMusicPlayer.module, DianeMusicPlayer.SAMPLE_RATE);
			DianeMusicPlayer.ibxm.setInterpolation(DianeMusicPlayer.interpolation);
			DianeMusicPlayer.playing = true;
			DianeMusicPlayer.playThread = new Thread(() -> {
				final var mixBuf = new int[DianeMusicPlayer.ibxm.getMixBufferLength()];
				final var outBuf = new byte[mixBuf.length * 4];
				final var audioFormat = new AudioFormat(DianeMusicPlayer.SAMPLE_RATE, 16, 2, true, true);
				try (var audioLine = AudioSystem.getSourceDataLine(audioFormat)) {
					audioLine.open();
					audioLine.start();
					while (DianeMusicPlayer.playing) {
						final var count = DianeMusicPlayer.getAudio(mixBuf);
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
			DianeMusicPlayer.playThread.start();
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
