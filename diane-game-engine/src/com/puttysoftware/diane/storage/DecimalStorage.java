/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.storage;

import java.util.Arrays;
import java.util.Objects;

/**
 * Data storage for doubles.
 */
public class DecimalStorage {
    // Fields
    /**
     * The underlying array where data is stored. Exposed for serialization purposes
     * for use with the protected copy constructor.
     */
    protected final double[] dataStore;
    private final int[] dataShape;
    private final int[] interProd;

    // Copy constructor
    /**
     * Main copy constructor.
     *
     * @param source the @self to make a copy of
     */
    public DecimalStorage(final DecimalStorage source) {
	this.dataShape = source.dataShape;
	this.interProd = new int[this.dataShape.length];
	var product = 1;
	for (var x = 0; x < this.dataShape.length; x++) {
	    this.interProd[x] = product;
	    product *= this.dataShape[x];
	}
	this.dataStore = Arrays.copyOf(source.dataStore, product);
    }

    // Protected copy constructor
    /**
     * Serialization-related protected copy constructor.
     *
     * @param source the underlying array where stored data came from
     * @param shape  simulated dimensions for the stored data
     */
    protected DecimalStorage(final double[] source, final int... shape) {
	this.dataShape = shape;
	this.interProd = new int[this.dataShape.length];
	var product = 1;
	for (var x = 0; x < this.dataShape.length; x++) {
	    this.interProd[x] = product;
	    product *= this.dataShape[x];
	}
	this.dataStore = Arrays.copyOf(source, product);
    }

    // Constructor
    /**
     * Main constructor.
     *
     * @param shape simulated dimensions for the stored data
     */
    public DecimalStorage(final int... shape) {
	this.dataShape = shape;
	this.interProd = new int[this.dataShape.length];
	var product = 1;
	for (var x = 0; x < this.dataShape.length; x++) {
	    this.interProd[x] = product;
	    product *= this.dataShape[x];
	}
	this.dataStore = new double[product];
    }

    // Methods
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
	if (obj == null || !(obj instanceof final DecimalStorage other)
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
    public final void fill(final double obj) {
	Arrays.fill(this.dataStore, obj);
    }

    /**
     * Get data at a given location in storage.
     *
     * @param loc the location to get data from
     * @return the data at that location
     */
    public final double getCell(final int... loc) {
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
    protected final double getRawCell(final int rawLoc) {
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
    public final void setCell(final double obj, final int... loc) {
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
    protected final void setRawCell(final double obj, final int rawLoc) {
	this.dataStore[rawLoc] = obj;
    }
}
