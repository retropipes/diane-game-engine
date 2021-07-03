/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.assets.ImageIndex;
import com.puttysoftware.images.BufferedImageIcon;

public class ImageLoader {
  static BufferedImageIcon loadUncached(final String name, final URL url) {
    try {
      final BufferedImage image = ImageIO.read(url);
      final BufferedImageIcon icon = new BufferedImageIcon(image);
      return icon;
    } catch (final IOException ie) {
      Diane.handleError(ie);
      return null;
    }
  }

  public static BufferedImageIcon load(final ImageIndex image, final URL url) {
    return ImageCache.getCachedImage(image.getName(), url);
  }

  private static class ImageCache {
    // Fields
    private static ArrayList<ImageCacheEntry> cache;
    private static boolean cacheCreated = false;

    // Methods
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
      final BufferedImageIcon newImage = ImageScaler.getScaledImage(ImageLoader.loadUncached(name, url));
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
