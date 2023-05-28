/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.storage;

import java.util.Arrays;
import java.util.Objects;

/**
 * Data storage for 16-bit short integers.
 */
public class ShortStorage {
    // Fields
    /**
     * The underlying array where data is stored. Exposed for serialization purposes
     * for use with the protected copy constructor.
     */
    protected final short[] dataStore;
    private final int[] dataShape;
    private final int[] interProd;

    // Constructor
    /**
     * Main constructor.
     *
     * @param shape simulated dimensions for the stored data
     */
    public ShortStorage(final int... shape) {
	this.dataShape = shape;
	this.interProd = new int[this.dataShape.length];
	var product = 1;
	for (var x = 0; x < this.dataShape.length; x++) {
	    this.interProd[x] = product;
	    product *= this.dataShape[x];
	}
	this.dataStore = new short[product];
    }

    // Protected copy constructor
    /**
     * Serialization-related protected copy constructor.
     *
     * @param source the underlying array where stored data came from
     * @param shape  simulated dimensions for the stored data
     */
    public ShortStorage(final short[] source, final int... shape) {
	this.dataShape = shape;
	this.interProd = new int[this.dataShape.length];
	var product = 1;
	for (var x = 0; x < this.dataShape.length; x++) {
	    this.interProd[x] = product;
	    product *= this.dataShape[x];
	}
	this.dataStore = Arrays.copyOf(source, product);
    }

    // Copy constructor
    /**
     * Main copy constructor.
     *
     * @param source the @self to make a copy of
     */
    public ShortStorage(final ShortStorage source) {
	this.dataShape = source.dataShape;
	this.interProd = new int[this.dataShape.length];
	var product = 1;
	for (var x = 0; x < this.dataShape.length; x++) {
	    this.interProd[x] = product;
	    product *= this.dataShape[x];
	}
	this.dataStore = Arrays.copyOf(source.dataStore, product);
    }

    /**
     * Check for equality.
     *
     * @param obj the other object to check
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof final ShortStorage other)
		|| !Arrays.equals(this.dataStore, other.dataStore)) {
	    return false;
	}
	return true;
    }

    /**
     * Fill the storage with the same data everywhere.
     *
     * @param obj the data to fill with
     */
    public final void fill(final short obj) {
	Arrays.fill(this.dataStore, obj);
    }

    /**
     * Get data at a given location in storage.
     *
     * @param loc the location to get data from
     * @return the data at that location
     */
    public final short getCell(final int... loc) {
	final var aloc = this.ravelLocation(loc);
	return this.dataStore[aloc];
    }

    /**
     * Get data directly from the underlying array. To convert a simulated index to
     * a raw index, use ravelLocation().
     *
     * @param rawLoc the index within the array to get data from
     * @return the data at that index
     */
    protected final short getRawCell(final int rawLoc) {
	return this.dataStore[rawLoc];
    }

    /**
     * Get the length of the underlying array.
     *
     * @return the underlying array length
     */
    protected final int getRawLength() {
	return this.dataStore.length;
    }

    /**
     * Get the shape (dimensions) of the storage.
     *
     * @return the shape, as an array of integers
     */
    public final int[] getShape() {
	return this.dataShape;
    }

    /**
     * Hashing support.
     */
    @Override
    public int hashCode() {
	return Objects.hash(Arrays.hashCode(this.dataStore));
    }

    /**
     * Change stored data at a given location.
     *
     * @param obj the new data value
     * @param loc the location to modify
     */
    public final void offsetCell(final short obj, final int... loc) {
	final var aloc = this.ravelLocation(loc);
	this.dataStore[aloc] += obj;
    }

    /**
     * Change stored data directly in the underlying array. To convert a simulated
     * index to a raw index, use ravelLocation().
     *
     * @param obj    the new data value
     * @param rawLoc the index to modify
     */
    protected final void offsetRawCell(final short obj, final int rawLoc) {
	this.dataStore[rawLoc] += obj;
    }

    /**
     * Utility to convert simulated indexes to raw indexes.
     *
     * @param loc a simulated index
     * @return a raw index
     */
    protected final int ravelLocation(final int... loc) {
	var res = 0;
	// Sanity check #1
	if (loc.length != this.interProd.length) {
	    throw new IllegalArgumentException(Integer.toString(loc.length));
	}
	for (var x = 0; x < this.interProd.length; x++) {
	    // Sanity check #2
	    if (loc[x] < 0 || loc[x] >= this.dataShape[x]) {
		throw new ArrayIndexOutOfBoundsException(loc[x]);
	    }
	    res += loc[x] * this.interProd[x];
	}
	return res;
    }

    /**
     * Change stored data at a given location.
     *
     * @param obj the new data value
     * @param loc the location to modify
     */
    public final void setCell(final short obj, final int... loc) {
	final var aloc = this.ravelLocation(loc);
	this.dataStore[aloc] = obj;
    }

    /**
     * Change stored data directly in the underlying array. To convert a simulated
     * index to a raw index, use ravelLocation().
     *
     * @param obj    the new data value
     * @param rawLoc the index to modify
     */
    protected final void setRawCell(final short obj, final int rawLoc) {
	this.dataStore[rawLoc] = obj;
    }
}
