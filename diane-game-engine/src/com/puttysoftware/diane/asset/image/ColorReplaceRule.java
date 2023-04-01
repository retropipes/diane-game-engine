/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.image;

import java.awt.Color;
import java.util.Objects;

final class ColorReplaceRule {
	// Fields
	private final Color findColor;
	private final Color replaceColor;

	// Constructor
	public ColorReplaceRule(final Color find, final Color replace) {
		this.findColor = find;
		this.replaceColor = replace;
	}

	public BufferedImageIcon apply(final BufferedImageIcon input) {
		if (input == null) {
			throw new IllegalArgumentException("input == NULL!");
		}
		final var width = input.getWidth();
		final var height = input.getHeight();
		final var result = new BufferedImageIcon(input);
		for (var x = 0; x < width; x++) {
			for (var y = 0; y < height; y++) {
				final var c = new Color(input.getRGB(x, y), true);
				if (c.equals(this.findColor)) {
					result.setRGB(x, y, this.replaceColor.getRGB());
				}
			}
		}
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof final ColorReplaceRule other)) {
			return false;
		}
		return Objects.equals(this.findColor, other.findColor) && Objects.equals(this.replaceColor, other.replaceColor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.findColor, this.replaceColor);
	}
}
