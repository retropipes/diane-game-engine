package com.puttysoftware.diane.asset.image;

import java.util.ArrayList;

class CompositeImageCache {
    // Fields
    private static ArrayList<CompositeImageCacheEntry> cache;
    private static boolean cacheCreated = false;

    private static void createCache() {
        if (!CompositeImageCache.cacheCreated) {
    	// Create the cache
    	CompositeImageCache.cache = new ArrayList<>();
    	CompositeImageCache.cacheCreated = true;
        }
    }

    public static BufferedImageIcon getCachedImage(final String name, final BufferedImageIcon... inputs) {
        if (!CompositeImageCache.cacheCreated) {
    	CompositeImageCache.createCache();
        }
        for (final CompositeImageCacheEntry entry : CompositeImageCache.cache) {
    	if (name.equals(entry.name())) {
    	    // Found
    	    return entry.image();
    	}
        }
        // Not found: Add to cache
        final var newImage = ImageCompositor.compositeUncached(inputs);
        final var newEntry = new CompositeImageCacheEntry(newImage, name);
        CompositeImageCache.cache.add(newEntry);
        return newImage;
    }
}