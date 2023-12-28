package com.puttysoftware.diane.asset.image;

import java.util.Objects;

class ShadedImageCacheEntry {
    // Fields
    private final BufferedImageIcon image;
    private final String name;

    // Constructors
    public ShadedImageCacheEntry(final BufferedImageIcon newImage, final String newName) {
        this.image = newImage;
        this.name = newName;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
    	return true;
        }
        if (!(obj instanceof final ShadedImageCacheEntry other)) {
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