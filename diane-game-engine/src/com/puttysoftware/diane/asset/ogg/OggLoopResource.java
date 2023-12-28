/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.diane.asset.ogg;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class OggLoopResource extends DianeOggPlayer {
    private final URL soundURL;
    private OggLoopPlayThread player;

    public OggLoopResource(final URL resURL) {
	this.soundURL = resURL;
    }

    @Override
    public boolean isPlaying() {
	return this.player != null && this.isAlive();
    }

    @Override
    public void run() {
	try (var ais = AudioSystem.getAudioInputStream(this.soundURL)) {
	    this.player = new OggLoopPlayThread(ais);
	    this.player.play();
	} catch (final UnsupportedAudioFileException | IOException e1) {
	}
    }

    @Override
    public void stopPlayer() {
	if (this.player != null) {
	    this.player.stopPlaying();
	}
    }
}
