package com.puttysoftware.diane.asset.image;

import java.util.ArrayList;

class ShadedImageCache {
    // Fields
    private static ArrayList<ShadedImageCacheEntry> cache;
    private static boolean cacheCreated = false;

    private static void createCache() {
        if (!ShadedImageCache.cacheCreated) {
    	// Create the cache
    	ShadedImageCache.cache = new ArrayList<>();
    	ShadedImageCache.cacheCreated = true;
        }
    }

    public static BufferedImageIcon getCachedImage(final String name, final BufferedImageIcon input,
    	final ColorShader shade) {
        if (!ShadedImageCache.cacheCreated) {
    	ShadedImageCache.createCache();
        }
        for (final ShadedImageCacheEntry entry : ShadedImageCache.cache) {
    	if (name.equals(entry.name())) {
    	    // Found
    	    return entry.image();
    	}
        }
        // Not found: Add to cache
        final var newImage = ImageShader.shadeUncached(input, shade);
        final var newEntry = new ShadedImageCacheEntry(newImage, name);
        ShadedImageCache.cache.add(newEntry);
        return newImage;
    }
}