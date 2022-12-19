package com.puttysoftware.diane.audio.wav;

import java.net.URL;

public abstract class WAVPlayer {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb

    // Constructor
    protected WAVPlayer() {
	super();
    }

    // Methods
    public abstract void play();

    // Factories
    public static WAVPlayer loadFile(final String file) {
	return new WAVFile(file);
    }

    public static WAVPlayer loadResource(final URL resource) {
	return new WAVResource(resource);
    }

    public static void playFile(final String file) {
	new WAVFile(file).play();
    }

    public static void playResource(final URL resource) {
	new WAVResource(resource).play();
    }
}
