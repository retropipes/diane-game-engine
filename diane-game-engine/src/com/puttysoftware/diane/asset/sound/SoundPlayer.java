/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.asset.sound;

public final class SoundPlayer {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb

    // Methods
    public static void play(final DianeSoundIndex index) {
	new SoundResource(index.getURL()).play();
    }

    // Constructor
    protected SoundPlayer() {
    }
}
