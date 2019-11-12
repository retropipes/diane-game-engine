/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.puttysoftware.diane.gui.GameImage;
import com.puttysoftware.diane.internal.BufferedImageIcon;
import com.puttysoftware.errorlogger.ErrorLogger;

public class ImageLoader {
  static GameImage loadUncached(final String name, final URL url,
      final ErrorLogger errorHandler) {
    try {
      final BufferedImage image = ImageIO.read(url);
      final GameImage icon = new BufferedImageIcon(image).createGameImage();
      return icon;
    } catch (final IOException ie) {
      errorHandler.logError(ie);
      return null;
    }
  }

  public static GameImage load(final String name, final URL url,
      final ErrorLogger errorHandler) {
    return ImageCache.getCachedImage(name, url, errorHandler);
  }

  private static class ImageCache {
    // Fields
    private static ArrayList<ImageCacheEntry> cache;
    private static boolean cacheCreated = false;

    // Methods
    public static GameImage getCachedImage(final String name, final URL url,
        final ErrorLogger errorHandler) {
      if (!ImageCache.cacheCreated) {
        ImageCache.createCache();
      }
      for (ImageCacheEntry entry : ImageCache.cache) {
        if (name.equals(entry.name())) {
          // Found
          return entry.image();
        }
      }
      // Not found: Add to cache
      GameImage newImage = ImageLoader.loadUncached(name, url, errorHandler);
      ImageCacheEntry newEntry = new ImageCacheEntry(newImage, name);
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
    private final GameImage image;
    private final String name;

    // Constructors
    public ImageCacheEntry(final GameImage newImage, final String newName) {
      this.image = newImage;
      this.name = newName;
    }

    // Methods
    public GameImage image() {
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
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof ImageCacheEntry)) {
        return false;
      }
      ImageCacheEntry other = (ImageCacheEntry) obj;
      return Objects.equals(this.name, other.name);
    }
  }
}
