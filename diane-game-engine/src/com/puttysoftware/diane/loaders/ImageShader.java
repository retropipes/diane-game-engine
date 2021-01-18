/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

import com.puttysoftware.images.BufferedImageIcon;

public class ImageShader {
  static BufferedImageIcon shadeUncached(final String name, final BufferedImageIcon input, final ColorShader shade) {
    if (input != null) {
      final int width = input.getWidth();
      final int height = input.getHeight();
      final BufferedImageIcon result = new BufferedImageIcon(input);
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          final Color c = new Color(input.getRGB(x, y), true);
          result.setRGB(x, y, shade.applyShade(c).getRGB());
        }
      }
      return result;
    } else {
      return null;
    }
  }

  public static BufferedImageIcon shade(final String name, final BufferedImageIcon input, final ColorShader shade) {
    return ImageCache.getCachedImage(name, input, shade);
  }

  private static class ImageCache {
    // Fields
    private static ArrayList<ImageCacheEntry> cache;
    private static boolean cacheCreated = false;

    // Methods
    public static BufferedImageIcon getCachedImage(final String name, final BufferedImageIcon input,
        final ColorShader shade) {
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
      final BufferedImageIcon newImage = ImageShader.shadeUncached(name, input, shade);
      final ImageCacheEntry newEntry = new ImageCacheEntry(newImage, name);
      ImageCache.cache.add(newEntry);
      return newImage;
    }

    private static void createCache() {
      if (!ImageCache.cacheCreated) {
        // Create the cache
        ImageCache.cache = new ArrayList<>();
        ImageCache.cacheCreated = true;
      }
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

    // Methods
    public BufferedImageIcon image() {
      return this.image;
    }

    public String name() {
      return this.name;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.name);
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof ImageCacheEntry)) {
        return false;
      }
      final ImageCacheEntry other = (ImageCacheEntry) obj;
      return Objects.equals(this.name, other.name);
    }
  }
}
