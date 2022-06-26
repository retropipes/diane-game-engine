/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.objectmodel;

import java.util.Objects;

import com.puttysoftware.diane.storage.FlagStorage;
import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;

class SolidProperties {
    // Private enumeration
    private enum SolidDataTypes {
	EXTERNAL(0),
	INTERNAL(1);

	private int index;

	SolidDataTypes(final int value) {
	    this.index = value;
	}
    }

    // Properties
    private final FlagStorage solidData;
    private static final int SOLID_DATA_TYPES = 2;

    // Constructors
    public SolidProperties() {
	this.solidData = new FlagStorage(SolidProperties.SOLID_DATA_TYPES, Directions.COUNT);
    }

    // Methods
    @Override
    public boolean equals(final Object obj) {
	if (obj == null) {
	    return false;
	}
	if (this.getClass() != obj.getClass()) {
	    return false;
	}
	final SolidProperties other = (SolidProperties) obj;
	if (!Objects.equals(this.solidData, other.solidData)) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 89 * hash + Objects.hashCode(this.solidData);
	return hash;
    }

    public boolean isSolid() {
	boolean result = false;
	for (int dir = 0; dir < Directions.COUNT; dir++) {
	    result = result || this.solidData.getCell(SolidDataTypes.EXTERNAL.index, dir);
	}
	return result;
    }

    public boolean isInternallySolid() {
	boolean result = false;
	for (int dir = 0; dir < Directions.COUNT; dir++) {
	    result = result || this.solidData.getCell(SolidDataTypes.INTERNAL.index, dir);
	}
	return result;
    }

    public boolean isDirectionallySolid(final int dirX, final int dirY) {
	final int dir = DirectionResolver.resolve(dirX, dirY);
	return this.solidData.getCell(SolidDataTypes.EXTERNAL.index, dir);
    }

    public boolean isInternallyDirectionallySolid(final int dirX, final int dirY) {
	final int dir = DirectionResolver.resolve(dirX, dirY);
	return this.solidData.getCell(SolidDataTypes.INTERNAL.index, dir);
    }

    public void setSolid(final boolean value) {
	for (int dir = 0; dir < Directions.COUNT; dir++) {
	    this.solidData.setCell(value, SolidDataTypes.EXTERNAL.index, dir);
	}
    }

    public void setInternallySolid(final boolean value) {
	for (int dir = 0; dir < Directions.COUNT; dir++) {
	    this.solidData.setCell(value, SolidDataTypes.INTERNAL.index, dir);
	}
    }

    public void setDirectionallySolid(final int dir, final boolean value) {
	this.solidData.setCell(value, SolidDataTypes.EXTERNAL.index, dir);
    }

    public void setInternallyDirectionallySolid(final int dir, final boolean value) {
	this.solidData.setCell(value, SolidDataTypes.INTERNAL.index, dir);
    }
}
