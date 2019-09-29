/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.errorlogger.ErrorLogger;
import com.puttysoftware.help.GraphicalHelpViewer;
import com.puttysoftware.images.BufferedImageIcon;

public class ImageLoader {
    public static final int MAX_WINDOW_SIZE = 700;

    static BufferedImageIcon loadUncached(final String name,
            final String imgpath, final Class<?> resourceLoader,
            final ErrorLogger errorHandler) {
        try {
            final URL url = resourceLoader.getResource(imgpath + name);
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            return icon;
        } catch (final IOException ie) {
            errorHandler.logError(ie);
            return null;
        }
    }

    public static BufferedImageIcon load(final String name,
            final String imgpath, final Class<?> resourceLoader,
            final ErrorLogger errorHandler) {
        return ImageCache.getCachedImage(name, imgpath, resourceLoader,
                errorHandler);
    }

    public static void viewCache() {
        if (!ImageCache.cacheCreated) {
            ImageCache.createCache();
        }
        final GraphicalHelpViewer cv = new GraphicalHelpViewer(
                ImageCache.images(), ImageCache.names());
        final JFrame viewFrame = new JFrame("Image Cache Viewer");
        viewFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        viewFrame.setLayout(new FlowLayout());
        viewFrame.add(cv.getHelp());
        cv.setHelpSize(ImageLoader.MAX_WINDOW_SIZE,
                ImageLoader.MAX_WINDOW_SIZE);
        viewFrame.pack();
        viewFrame.setResizable(false);
        viewFrame.setVisible(true);
    }

    public static void recreateCache() {
        ImageCache.cacheCreated = false;
        ImageCache.createCache();
    }

    private static class ImageCache {
        // Fields
        private static ArrayList<ImageCacheEntry> cache;
        private static boolean cacheCreated = false;

        // Methods
        public static BufferedImageIcon getCachedImage(final String name,
                final String imgpath, final Class<?> resourceLoader,
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
            BufferedImageIcon newImage = ImageLoader.loadUncached(name, imgpath,
                    resourceLoader, errorHandler);
            ImageCacheEntry newEntry = new ImageCacheEntry(newImage, name);
            ImageCache.cache.add(newEntry);
            return newImage;
        }

        public static BufferedImageIcon[] images() {
            int limit = ImageCache.cache.size();
            BufferedImageIcon[] result = new BufferedImageIcon[limit];
            for (int x = 0; x < limit; x++) {
                result[x] = ImageCache.cache.get(x).image();
            }
            return result;
        }

        public static String[] names() {
            int limit = ImageCache.cache.size();
            String[] result = new String[limit];
            for (int x = 0; x < limit; x++) {
                result[x] = ImageCache.cache.get(x).name();
            }
            return result;
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
        public ImageCacheEntry(final BufferedImageIcon newImage,
                final String newName) {
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
