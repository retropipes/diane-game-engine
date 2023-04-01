/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.internal;

import com.puttysoftware.diane.asset.image.DianeImageIndex;
import com.puttysoftware.diane.asset.music.DianeMusicIndex;
import com.puttysoftware.diane.asset.sound.DianeSoundIndex;

public class DefaultAssets {
    private DefaultAssets() {
        // Unused
    }

    public static final DianeImageIndex NO_IMAGE = null;
    public static final DianeSoundIndex NO_SOUND = null;
    public static final DianeMusicIndex NO_MUSIC = null;
}
