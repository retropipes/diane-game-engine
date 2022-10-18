/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.ack.internal;

import java.awt.Color;

import com.puttysoftware.diane.asset.BufferedImageIcon;

class ImageCompositor {
    private static BufferedImageIcon compositeTwo(final BufferedImageIcon input1, final BufferedImageIcon input2) {
	if (input1 != null && input2 != null) {
	    final int width = input1.getWidth();
	    final int height = input1.getHeight();
	    final int width2 = input2.getWidth();
	    final int height2 = input2.getHeight();
	    if (width == width2 && height == height2) {
		final BufferedImageIcon result = new BufferedImageIcon(input2);
		for (int x = 0; x < width; x++) {
		    for (int y = 0; y < height; y++) {
			final int pixel = input2.getRGB(x, y);
			final Color c = new Color(pixel, true);
			if (c.getAlpha() == 0) {
			    result.setRGB(x, y, input1.getRGB(x, y));
			}
		    }
		}
		return result;
	    }
	}
	if (input1 != null) {
	    return input1;
	} else if (input2 != null) {
	    return input2;
	} else {
	    return null;
	}
    }

    static BufferedImageIcon composite(final BufferedImageIcon... inputs) {
	if (inputs != null) {
	    if (inputs.length >= 2) {
		BufferedImageIcon result = ImageCompositor.compositeTwo(inputs[0], inputs[1]);
		final int beyond2 = inputs.length;
		for (int i = 2; i < beyond2; i++) {
		    final BufferedImageIcon tempResult = ImageCompositor.compositeTwo(result, inputs[i]);
		    if (tempResult != null) {
			result = tempResult;
		    }
		}
		return result;
	    } else if (inputs[0] != null) {
		return inputs[0];
	    }
	}
	return null;
    }
}
