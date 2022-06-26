/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.gui.dialog;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class MainWindow {
    private static MainWindow window;
    private final JFrame frame;
    private MainWindowContent content;
    private Dimension contentSize;
    private LinkedList<MainWindowContent> savedContentStack;

    private MainWindow(final int width, final int height) {
	super();
	this.frame = new JFrame();
	this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.frame.setResizable(false);
	this.contentSize = new Dimension(width, height);
	this.content = this.createContent();
	this.savedContentStack = new LinkedList<>();
	this.frame.setContentPane(this.content.owner());
	this.frame.setVisible(true);
	this.frame.pack();
    }

    static JFrame owner() {
	return MainWindow.getMainWindow().frame;
    }

    public static void createMainWindow(final int width, final int height) {
	if (MainWindow.window == null) {
	    MainWindow.window = new MainWindow(width, height);
	}
    }

    public final MainWindowContent createContent() {
	return new MainWindowContent(this.contentSize);
    }

    public static MainWindow getMainWindow() {
	return MainWindow.window;
    }

    public void attachAndSave(final MainWindowContent customContent) {
	this.savedContentStack.push(this.content);
	this.content = customContent;
	this.frame.setContentPane(this.content.owner());
    }

    public void restoreSaved() {
	this.setDefaultButton(null);
	this.content = this.savedContentStack.pop();
	this.frame.setContentPane(this.content.owner());
    }

    public void setTitle(final String title) {
	this.frame.setTitle(title);
    }

    public void addKeyListener(final KeyListener l) {
	this.frame.addKeyListener(l);
    }

    public void removeKeyListener(final KeyListener l) {
	this.frame.removeKeyListener(l);
    }

    public void setDefaultButton(final JButton defaultButton) {
	this.frame.getRootPane().setDefaultButton(defaultButton);
    }
}
