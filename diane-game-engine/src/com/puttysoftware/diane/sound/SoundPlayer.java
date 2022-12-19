/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.sound;

import com.puttysoftware.diane.asset.DianeSoundIndex;

public final class SoundPlayer {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb

    // Methods
    public static void play(final DianeSoundIndex index) {
	new SoundResource(index.getSoundURL()).play();
    }

    // Constructor
    protected SoundPlayer() {
    }
}
