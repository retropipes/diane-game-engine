/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.diane.asset.ogg;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class OggLoopFile extends DianeOggPlayer {
    private final String filename;
    private OggLoopPlayThread player;

    public OggLoopFile(final String Oggfile) {
	this.filename = Oggfile;
    }

    @Override
    public boolean isPlaying() {
	return this.player != null && this.isAlive();
    }

    @Override
    public void run() {
	if (this.filename != null) {
	    final var soundFile = new File(this.filename);
	    if (!soundFile.exists()) {
		return;
	    }
	    try (var ais = AudioSystem.getAudioInputStream(soundFile)) {
		this.player = new OggLoopPlayThread(ais);
		this.player.play();
	    } catch (final UnsupportedAudioFileException | IOException e1) {
	    }
	}
    }

    @Override
    protected void stopPlayer() {
	if (this.player != null) {
	    this.player.stopPlaying();
	}
    }
}
