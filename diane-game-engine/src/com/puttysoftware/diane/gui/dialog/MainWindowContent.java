/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.gui.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public final class MainWindowContent {
    private JPanel content;

    MainWindowContent(final Dimension contentSize) {
	super();
	this.content = new JPanel();
	this.content.setPreferredSize(contentSize);
	this.content.setMinimumSize(contentSize);
	this.content.setMaximumSize(contentSize);
	this.content.setSize(contentSize);
    }

    JPanel owner() {
	return this.content;
    }

    public void add(final Component comp) {
	this.content.add(comp);
    }

    public void add(final Component comp, final Object constraints) {
	this.content.add(comp, constraints);
    }

    public void setLayout(final LayoutManager mgr) {
	this.content.setLayout(mgr);
    }

    public void removeAll() {
	this.content.removeAll();
    }
}
