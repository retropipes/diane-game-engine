/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;

import com.puttysoftware.diane.asset.music.DianeMusicIndex;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;

public abstract class ContentScreen extends WindowAdapter {
    // Fields
    protected final MainWindow theFrame;
    protected MainContent theContent;
    private String value;
    private Thread valueTask;
    private boolean viewReady;
    private String title;
    private DianeMusicIndex music;
    private JButton defaultButton;

    // Constructors
    protected ContentScreen() {
	this.theFrame = MainWindow.mainWindow();
	this.viewReady = false;
    }

    public void addKeyListener(final KeyListener l) {
	this.theFrame.addKeyListener(l);
    }

    public void addWindowFocusListener(final WindowFocusListener l) {
	this.theFrame.addWindowFocusListener(l);
    }

    public void addWindowListener(final WindowListener l) {
	this.theFrame.addWindowListener(l);
    }

    private void checkView() {
	if (!this.viewReady) {
	    this.setUpView();
	    this.viewReady = true;
	}
    }

    MainContent content() {
	this.checkView();
	return this.theContent;
    }

    public JButton defaultButton() {
	return this.defaultButton;
    }

    public final void hideScreen() {
	this.checkView();
	this.hideScreenHook();
	this.theFrame.removeWindowListener(this);
	this.theFrame.restoreSaved();
    }

    protected void hideScreenHook() {
    }

    public DianeMusicIndex music() {
	return this.music;
    }

    public void pack() {
	this.theFrame.pack();
    }

    protected abstract void populateMainPanel();

    public void removeKeyListener(final KeyListener l) {
	this.theFrame.removeKeyListener(l);
    }

    public void removeWindowFocusListener(final WindowFocusListener l) {
	this.theFrame.removeWindowFocusListener(l);
    }

    public void removeWindowListener(final WindowListener l) {
	this.theFrame.removeWindowListener(l);
    }

    public void setDefaultButton(final JButton newDefaultButton) {
	this.defaultButton = newDefaultButton;
    }

    public void setMusic(final DianeMusicIndex newMusic) {
	this.music = newMusic;
    }

    public void setTitle(final String newTitle) {
	this.title = newTitle;
    }

    final void setUpView() {
	this.theContent = MainContentFactory.mainContent();
	this.populateMainPanel();
	this.theContent.setOpaque(true);
    }

    public synchronized final void setValue(final String v) {
	this.value = v;
	this.valueTask.notifyAll();
    }

    public final void showScreen() {
	this.checkView();
	this.theFrame.setAndSave(this.theContent, this.title);
	this.theFrame.addWindowListener(this);
	this.showScreenHook();
	this.theFrame.pack();
    }

    protected void showScreenHook() {
    }

    public final String showValueScreen() {
	this.valueTask = new Thread() {
	    @Override
	    public void run() {
		ContentScreen.this.checkView();
		ContentScreen.this.showScreen();
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

    public void statusMessage(final String msg) {
	CommonDialogs.showDialogLater(msg);
    }

    public String title() {
	return this.title;
    }

    public void updateDirtyWindow(final boolean appDirty) {
	this.theFrame.setDirty(appDirty);
    }
}
