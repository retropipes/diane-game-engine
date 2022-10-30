/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 *
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.awt.event.WindowAdapter;
import java.lang.ref.WeakReference;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.puttysoftware.diane.asset.DianeMusicIndex;

public abstract class ScreenController extends WindowAdapter {
    // Fields
    private final ScreenModel model;
    private final ScreenView view;
    private String value;
    private Thread valueTask;
    private boolean viewReady;

    // Constructors
    protected ScreenController(final ScreenModel theModel, final ScreenView theView) {
	this.model = theModel;
	this.view = theView;
	this.viewReady = false;
    }

    // Methods
    private void checkView() {
	if (this.model == null || this.view == null) {
	    throw new IllegalStateException();
	}
	if (!this.viewReady) {
	    this.view.setUpView(this.model);
	    this.viewReady = true;
	}
    }

    JPanel content() {
	this.checkView();
	return this.view.content();
    }

    protected final void hideScreen() {
	this.checkView();
	this.view.hideScreen(new WeakReference<>(this));
    }

    public synchronized final void setValue(final String v) {
	this.value = v;
	this.valueTask.notifyAll();
    }

    public final void showScreen() {
	this.checkView();
	this.view.showScreen(this.model, new WeakReference<>(this));
    }

    public final String showValueScreen() {
	this.valueTask = new Thread() {
	    @Override
	    public void run() {
		ScreenController.this.checkView();
		ScreenController.this.view.showScreen(ScreenController.this.model,
			new WeakReference<>(ScreenController.this));
	    }
	};
	this.valueTask.start();
	try {
	    this.valueTask.join();
	} catch (final InterruptedException e) {
	    return null;
	}
	return this.value;
    }

    String title() {
	if (this.model == null) {
	    throw new IllegalStateException();
	}
	return this.model.getTitle();
    }

    DianeMusicIndex music() {
	if (this.model == null) {
	    throw new IllegalStateException();
	}
	return this.model.getMusic();
    }

    JButton defaultButton() {
	if (this.model == null) {
	    throw new IllegalStateException();
	}
	return this.model.getDefaultButton();
    }
}
