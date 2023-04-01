/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

public class ImageCompositor {
	private static class ImageCache {
		// Fields
		private static ArrayList<ImageCacheEntry> cache;
		private static boolean cacheCreated = false;

		private static void createCache() {
			if (!ImageCache.cacheCreated) {
				// Create the cache
				ImageCache.cache = new ArrayList<>();
				ImageCache.cacheCreated = true;
			}
		}

		// Methods
		public static BufferedImageIcon getCachedImage(final String name, final BufferedImageIcon... inputs) {
			if (!ImageCache.cacheCreated) {
				ImageCache.createCache();
			}
			for (final ImageCacheEntry entry : ImageCache.cache) {
				if (name.equals(entry.name())) {
					// Found
					return entry.image();
				}
			}
			// Not found: Add to cache
			final var newImage = ImageCompositor.compositeUncached(name, inputs);
			final var newEntry = new ImageCacheEntry(newImage, name);
			ImageCache.cache.add(newEntry);
			return newImage;
		}
	}

	private static class ImageCacheEntry {
		// Fields
		private final BufferedImageIcon image;
		private final String name;

		// Constructors
		public ImageCacheEntry(final BufferedImageIcon newImage, final String newName) {
			this.image = newImage;
			this.name = newName;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof final ImageCacheEntry other)) {
				return false;
			}
			return Objects.equals(this.name, other.name);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.name);
		}

		// Methods
		public BufferedImageIcon image() {
			return this.image;
		}

		public String name() {
			return this.name;
		}
	}

	public static BufferedImageIcon composite(final String name, final BufferedImageIcon... inputs) {
		return ImageCache.getCachedImage(name, inputs);
	}

	private static BufferedImageIcon compositeTwo(final BufferedImageIcon input1, final BufferedImageIcon input2) {
		if (input1 != null && input2 != null) {
			final var width = input1.getWidth();
			final var height = input1.getHeight();
			final var width2 = input2.getWidth();
			final var height2 = input2.getHeight();
			if (width == width2 && height == height2) {
				final var result = new BufferedImageIcon(input2);
				for (var x = 0; x < width; x++) {
					for (var y = 0; y < height; y++) {
						final var pixel = input2.getRGB(x, y);
						final var c = new Color(pixel, true);
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
		} else {
			return input2;
		}
	}

	static BufferedImageIcon compositeUncached(final String name, final BufferedImageIcon... inputs) {
		if (inputs != null) {
			if (inputs.length >= 2) {
				var result = ImageCompositor.compositeTwo(inputs[0], inputs[1]);
				final var beyond2 = inputs.length;
				for (var i = 2; i < beyond2; i++) {
					final var tempResult = ImageCompositor.compositeTwo(result, inputs[i]);
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
