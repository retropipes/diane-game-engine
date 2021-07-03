/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.assets.SoundIndex;

public class SoundLoader {
  private SoundLoader() {
    // Do nothing
  }

  private static final int BUFFER_SIZE = 4096; // 4Kb

  public static void play(final SoundIndex sound) {
    new Thread() {
      @Override
      public void run() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound.getSoundURL())) {
          final AudioFormat format = audioInputStream.getFormat();
          final DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
          try (Line line = AudioSystem.getLine(info); SourceDataLine auline = (SourceDataLine) line) {
            auline.open(format);
            auline.start();
            int nBytesRead = 0;
            final byte[] abData = new byte[SoundLoader.BUFFER_SIZE];
            try {
              while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                  auline.write(abData, 0, nBytesRead);
                }
              }
            } catch (final IOException e) {
              Diane.handleError(e);
            } finally {
              auline.drain();
            }
          } catch (final LineUnavailableException e) {
            Diane.handleError(e);
          }
        } catch (final UnsupportedAudioFileException e) {
          Diane.handleError(e);
        } catch (final IOException e) {
          Diane.handleError(e);
        }
      }
    }.start();
  }
}