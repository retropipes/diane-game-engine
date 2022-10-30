/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 *
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import javax.swing.JButton;

import com.puttysoftware.diane.asset.DianeMusicIndex;

public final class ScreenModel {
    // Fields
    private final String title;
    private final DianeMusicIndex music;
    private final JButton defaultButton;

    // Constructors
    public ScreenModel(final String theTitle) {
	this.title = theTitle;
	this.music = null;
	this.defaultButton = null;
    }
    
    public ScreenModel(final String theTitle, final DianeMusicIndex theMusic, final JButton theDefaultButton) {
	this.title = theTitle;
	this.music = theMusic;
	this.defaultButton = theDefaultButton;
    }

    // Methods
    public String getTitle() {
	return this.title;
    }
    
    public DianeMusicIndex getMusic() {
	return this.music;
    }
    
    public JButton getDefaultButton() {
	return this.defaultButton;
    }
}
