/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 *
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

public final class ScreenModel {
    // Fields
    private final String title;

    // Constructor
    public ScreenModel(final String theTitle) {
	this.title = theTitle;
    }

    // Methods
    public String getTitle() {
	return this.title;
    }
}
