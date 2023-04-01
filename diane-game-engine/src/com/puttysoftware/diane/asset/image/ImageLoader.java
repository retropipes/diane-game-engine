/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.image;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.puttysoftware.diane.Diane;

public class ImageLoader {
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

		public static BufferedImageIcon getCachedImage(final String name, final URL url) {
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
			final var newImage = ImageScaler.getScaledImage(ImageLoader.loadUncached(name, url));
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

		public BufferedImageIcon image() {
			return this.image;
		}

		public String name() {
			return this.name;
		}
	}

	public static BufferedImageIcon load(final DianeImageIndex image, final URL url) {
		return ImageCache.getCachedImage(image.getName(), url);
	}

	public static BufferedImageIcon load(final String name, final URL url) {
		return ImageCache.getCachedImage(name, url);
	}

	static BufferedImageIcon loadUncached(final String name, final URL url) {
		try {
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			Diane.handleError(ie);
			return null;
		}
	}
}
