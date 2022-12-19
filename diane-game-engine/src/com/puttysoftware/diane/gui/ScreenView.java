/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.lang.ref.WeakReference;

import javax.swing.JPanel;

public abstract class ScreenView {
    // Fields
    protected final MainWindow theFrame;
    protected JPanel thePanel;
    protected WeakReference<ScreenController> controllerReference;

    // Constructors
    protected ScreenView() {
	this.theFrame = MainWindow.mainWindow();
    }

    JPanel content() {
	return this.thePanel;
    }

    protected final void hideScreen(final WeakReference<ScreenController> controllerRef) {
	this.controllerReference = controllerRef;
	this.theFrame.removeWindowListener(this.controllerReference.get());
	this.theFrame.restoreSaved();
    }

    protected abstract void populateMainPanel(final ScreenModel model);

    final void setUpView(final ScreenModel model) {
	this.thePanel = MainContentFactory.content();
	this.populateMainPanel(model);
	this.thePanel.setOpaque(true);
    }

    // Methods
    final void showScreen(final ScreenModel model, final WeakReference<ScreenController> controllerRef) {
	this.controllerReference = controllerRef;
	this.theFrame.setAndSave(this.thePanel, model.getTitle());
	this.theFrame.addWindowListener(this.controllerReference.get());
	this.theFrame.pack();
    }
}
