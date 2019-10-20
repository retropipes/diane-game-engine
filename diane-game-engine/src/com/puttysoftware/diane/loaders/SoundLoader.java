/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.net.URL;

import com.puttysoftware.audio.wav.WAVFactory;
import com.puttysoftware.errorlogger.ErrorLogger;

public class SoundLoader {
  private SoundLoader() {
    // Do nothing
  }

  public static void play(final URL soundURL, final ErrorLogger errorHandler) {
    WAVFactory.playResource(soundURL, errorHandler);
  }
}