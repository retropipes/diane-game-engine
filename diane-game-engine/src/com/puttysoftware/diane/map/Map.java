/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.map;

import com.puttysoftware.diane.objectmodel.ObjectModel;
import com.puttysoftware.diane.storage.ObjectStorage;

public class Map {
    // Properties
    private final ObjectStorage data;

    // Constructors
    public Map(final int... dimensions) {
	this.data = new ObjectStorage(dimensions);
    }

    public void fill(final ObjectModel with) {
	this.data.fill(with);
    }

    public ObjectModel getCell(final int... location) {
	return (ObjectModel) this.data.getCell(location);
    }

    public int getSize(final int dimension) {
	return this.data.getShape()[dimension];
    }

    public void setCell(final ObjectModel o, final int... location) {
	this.data.setCell(o, location);
    }
}
