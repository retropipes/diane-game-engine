/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

import com.puttysoftware.diane.gui.GameImage;

public class ImageCompositor {
  private static GameImage compositeTwo(final GameImage input1,
      final GameImage input2) {
    if (input1 != null && input2 != null) {
      final int width = input1.getWidth();
      final int height = input1.getHeight();
      final int width2 = input2.getWidth();
      final int height2 = input2.getHeight();
      if (width == width2 && height == height2) {
        final GameImage result = new GameImage(input2);
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

  static GameImage compositeUncached(final String name,
      final GameImage... inputs) {
    if (inputs != null) {
      if (inputs.length >= 2) {
        GameImage result = ImageCompositor.compositeTwo(inputs[0], inputs[1]);
        final int beyond2 = inputs.length;
        for (int i = 2; i < beyond2; i++) {
          final GameImage tempResult = ImageCompositor.compositeTwo(result,
              inputs[i]);
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

  public static GameImage composite(final String name,
      final GameImage... inputs) {
    return ImageCache.getCachedImage(name, inputs);
  }

  private static class ImageCache {
    // Fields
    private static ArrayList<ImageCacheEntry> cache;
    private static boolean cacheCreated = false;

    // Methods
    public static GameImage getCachedImage(final String name,
        final GameImage... inputs) {
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
      final GameImage newImage = ImageCompositor.compositeUncached(name,
          inputs);
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
