/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.lang.reflect.Field;

import com.puttysoftware.images.BufferedImageIcon;

class ImageScaler {
  // Fields
  private static boolean SCALE_COMPUTED = false;
  private static int NORMAL_DPI = 96;

  // Methods
  private static void computeImageScale() {
    if (!ImageScaler.SCALE_COMPUTED) {
      if (System.getProperty("os.name").startsWith("Mac OS X")) {
        try {
          final GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
              .getDefaultScreenDevice();
          final Field field = graphicsDevice.getClass().getDeclaredField("scale");
          if (field != null) {
            field.setAccessible(true);
            final Object scale = field.get(graphicsDevice);
            if (scale instanceof Integer) {
              BufferedImageIcon.setScale(((Integer) scale).intValue() * BufferedImageIcon.getScaleMult());
            }
          }
        } catch (NoSuchFieldException | IllegalAccessException e) {
          computeImageScaleFallback();
        }
      } else {
        computeImageScaleFallback();
      }
      ImageScaler.SCALE_COMPUTED = true;
    }
  }

  private static void computeImageScaleFallback() {
    final int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
    BufferedImageIcon.setScale(dpi * BufferedImageIcon.getScaleMult() / ImageScaler.NORMAL_DPI);
  }

  static BufferedImageIcon getScaledImage(final BufferedImageIcon src) {
    ImageScaler.computeImageScale();
    final double scale = BufferedImageIcon.getNormalizedScale();
    if (scale > 1.0) {
      final int owidth = src.getWidth(null);
      final int oheight = src.getHeight(null);
      final int nwidth = (int) (owidth * scale);
      final int nheight = (int) (oheight * scale);
      final BufferedImageIcon dest = new BufferedImageIcon(nwidth, nheight);
      final Graphics2D g2d = dest.createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2d.scale(scale, scale);
      g2d.drawImage(src, 0, 0, null);
      g2d.scale(1, 1);
      g2d.dispose();
      return dest;
    } else {
      return src;
    }
  }
}
