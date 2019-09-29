package com.puttysoftware.storage;

/**
 * Simple Object subclass supporting clone().
 */
public class CloneableObject implements Cloneable {
    /**
     * Default constructor.
     * Since this object has no state, all this does is initialize the superclass.
     */
    public CloneableObject() {
	super();
    }

    /**
     * Create a copy of this @self.
     * @return a copy of the current instance
     * @throws CloneNotSupportedException when copying fails
     */
    @Override
    public CloneableObject clone() throws CloneNotSupportedException {
	return (CloneableObject) super.clone();
    }
}
