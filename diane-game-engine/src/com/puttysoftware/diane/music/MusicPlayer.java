package com.puttysoftware.diane.music;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import com.puttysoftware.diane.asset.DianeMusicIndex;

public class MusicPlayer {
    private static final int SAMPLE_RATE = 41000;
    private Module module;
    IBXM ibxm;
    volatile boolean playing;
    private int interpolation;
    private Thread playThread;

    public MusicPlayer() {
	// Do nothing
    }

    synchronized int getAudio(final int[] mixBuf) {
	return this.ibxm.getAudio(mixBuf);
    }

    public boolean isPlaying() {
	return this.playThread != null && this.playThread.isAlive();
    }

    public synchronized static MusicPlayer load(final DianeMusicIndex index) throws IOException {
	final var source = index.getMusicURL();
	try (var inputStream = source.openStream()) {
	    final var moduleData = inputStream.readAllBytes();
	    var offset = 0;
	    while (offset < moduleData.length) {
		final var len = inputStream.read(moduleData, offset, moduleData.length - offset);
		if (len < 0) {
		    inputStream.close();
		    throw new IOException("Unexpected end of file."); //$NON-NLS-1$
		}
		offset += len;
	    }
	    inputStream.close();
	    MusicPlayer mp = new MusicPlayer();
	    mp.module = new Module(moduleData);
	    mp.ibxm = new IBXM(mp.module, MusicPlayer.SAMPLE_RATE);
	    mp.ibxm.setInterpolation(mp.interpolation);
	    return mp;
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    public synchronized void play() {
	if (this.ibxm != null) {
	    this.playing = true;
	    this.playThread = new Thread(() -> {
		final var mixBuf = new int[MusicPlayer.this.ibxm.getMixBufferLength()];
		final var outBuf = new byte[mixBuf.length * 4];
		final var audioFormat = new AudioFormat(MusicPlayer.SAMPLE_RATE, 16, 2, true, true);
		try (var audioLine = AudioSystem.getSourceDataLine(audioFormat)) {
		    audioLine.open();
		    audioLine.start();
		    while (MusicPlayer.this.playing) {
			final var count = MusicPlayer.this.getAudio(mixBuf);
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
	    this.playThread.start();
	}
    }

    public synchronized void stopPlaying() {
	this.playing = false;
	try {
	    if (this.playThread != null) {
		this.playThread.join();
	    }
	} catch (final InterruptedException e) {
	}
    }
}
