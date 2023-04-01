/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.image;

import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.lang.reflect.InaccessibleObjectException;

class ImageScaler {
	// Fields
	private static boolean SCALE_COMPUTED = false;
	private static int NORMAL_DPI = 96;

	// Methods
	private static void computeImageScale() {
		if (!ImageScaler.SCALE_COMPUTED) {
			if (System.getProperty("os.name").startsWith("Mac OS X")) {
				try {
					final var graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
							.getDefaultScreenDevice();
					final var field = graphicsDevice.getClass().getDeclaredField("scale");
					if (field != null) {
						field.setAccessible(true);
						final var scale = field.get(graphicsDevice);
						if (scale instanceof Integer) {
							BufferedImageIcon.setScale(((Integer) scale).intValue() * BufferedImageIcon.getScaleMult());
						}
					}
				} catch (NoSuchFieldException | IllegalAccessException | InaccessibleObjectException e) {
					ImageScaler.computeImageScaleFallback();
				}
			} else {
				ImageScaler.computeImageScaleFallback();
			}
			ImageScaler.SCALE_COMPUTED = true;
		}
	}

	private static void computeImageScaleFallback() {
		final var dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		BufferedImageIcon.setScale(dpi * BufferedImageIcon.getScaleMult() / ImageScaler.NORMAL_DPI);
	}

	static BufferedImageIcon getScaledImage(final BufferedImageIcon src) {
		ImageScaler.computeImageScale();
		final var scale = BufferedImageIcon.getNormalizedScale();
		if (scale > 1.0) {
			final var owidth = src.getWidth(null);
			final var oheight = src.getHeight(null);
			final var nwidth = (int) (owidth * scale);
			final var nheight = (int) (oheight * scale);
			final var dest = new BufferedImageIcon(nwidth, nheight);
			final var g2d = dest.createGraphics();
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
