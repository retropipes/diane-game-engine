/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.diane.asset.ogg;

import java.net.URL;

import com.puttysoftware.diane.internal.PrivateOggString;
import com.puttysoftware.diane.internal.PrivateStrings;

public abstract class DianeOggPlayer extends Thread {
    // Constants
    private static DianeOggPlayer ACTIVE_MEDIA;

    // Factories
    public static DianeOggPlayer loadLoopedFile(final String file) {
	DianeOggPlayer.stopPlaying();
	DianeOggPlayer.ACTIVE_MEDIA = new OggLoopFile(file);
	return DianeOggPlayer.ACTIVE_MEDIA;
    }

    public static DianeOggPlayer loadLoopedResource(final URL resource) {
	DianeOggPlayer.stopPlaying();
	DianeOggPlayer.ACTIVE_MEDIA = new OggLoopResource(resource);
	return DianeOggPlayer.ACTIVE_MEDIA;
    }

    public static DianeOggPlayer loadFile(final String file) {
	DianeOggPlayer.stopPlaying();
	DianeOggPlayer.ACTIVE_MEDIA = new OggFile(file);
	return DianeOggPlayer.ACTIVE_MEDIA;
    }

    public static DianeOggPlayer loadResource(final URL resource) {
	DianeOggPlayer.stopPlaying();
	DianeOggPlayer.ACTIVE_MEDIA = new OggResource(resource);
	return DianeOggPlayer.ACTIVE_MEDIA;
    }

    public static void stopPlaying() {
	if (DianeOggPlayer.ACTIVE_MEDIA != null) {
	    DianeOggPlayer.ACTIVE_MEDIA.stopPlayer();
	}
    }

    // Constructor
    protected DianeOggPlayer() {
	super(PrivateStrings.ogg(PrivateOggString.MEDIA_PLAYER_NAME));
    }

    public abstract boolean isPlaying();

    public void play() {
	this.start();
    }

    protected abstract void stopPlayer();
}
