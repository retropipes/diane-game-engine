/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.image;

import java.awt.Color;

public class ImageShader {
    public static BufferedImageIcon shade(final String name, final BufferedImageIcon input, final ColorShader shade) {
	return ShadedImageCache.getCachedImage(name, input, shade);
    }

    static BufferedImageIcon shadeUncached(final BufferedImageIcon input, final ColorShader shade) {
	if (input == null) {
	    return null;
	}
	final var width = input.getWidth();
	final var height = input.getHeight();
	final var result = new BufferedImageIcon(input);
	for (var x = 0; x < width; x++) {
	    for (var y = 0; y < height; y++) {
		final var c = new Color(input.getRGB(x, y), true);
		result.setRGB(x, y, shade.applyShade(c).getRGB());
	    }
	}
	return result;
    }
}
