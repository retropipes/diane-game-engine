/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.puttysoftware.diane.asset.music.DianeMusicIndex;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;

public abstract class Screen extends WindowAdapter {
    // Fields
    protected final MainWindow theFrame;
    protected JPanel theContent;
    private String value;
    private Thread valueTask;
    private boolean viewReady;
    private String title;
    private DianeMusicIndex music;
    private JButton defaultButton;

    // Constructors
    protected Screen() {
	this.theFrame = MainWindow.mainWindow();
	this.viewReady = false;
    }

    JPanel content() {
	this.checkView();
	return this.theContent;
    }

    protected void hideScreenHook() {
    }

    protected abstract void populateMainPanel();

    final void setUpView() {
	this.theContent = MainContentFactory.content();
	this.populateMainPanel();
	this.theContent.setOpaque(true);
    }

    protected void showScreenHook() {
    }

    private void checkView() {
	if (!this.viewReady) {
	    this.setUpView();
	    this.viewReady = true;
	}
    }

    public final void hideScreen() {
	this.checkView();
	this.hideScreenHook();
	this.theFrame.removeWindowListener(this);
	this.theFrame.restoreSaved();
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

    public final String showValueScreen() {
	this.valueTask = new Thread() {
	    @Override
	    public void run() {
		Screen.this.checkView();
		Screen.this.showScreen();
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
	CommonDialogs.showDialog(msg);
    }

    public void setTitle(final String newTitle) {
	this.title = newTitle;
    }

    public String title() {
	return this.title;
    }

    public DianeMusicIndex music() {
	return this.music;
    }

    public JButton defaultButton() {
	return this.defaultButton;
    }
    
    public void updateDirtyWindow(final boolean appDirty) {
	this.theFrame.setDirty(appDirty);
    }

    public void pack() {
	this.theFrame.pack();
    }

    public void addWindowListener(final WindowListener l) {
	this.theFrame.addWindowListener(l);
    }

    public void addWindowFocusListener(final WindowFocusListener l) {
	this.theFrame.addWindowFocusListener(l);
    }

    public void addKeyListener(final KeyListener l) {
	this.theFrame.addKeyListener(l);
    }

    public void removeWindowListener(final WindowListener l) {
	this.theFrame.removeWindowListener(l);
    }

    public void removeWindowFocusListener(final WindowFocusListener l) {
	this.theFrame.removeWindowFocusListener(l);
    }

    public void removeKeyListener(final KeyListener l) {
	this.theFrame.removeKeyListener(l);
    }
}
