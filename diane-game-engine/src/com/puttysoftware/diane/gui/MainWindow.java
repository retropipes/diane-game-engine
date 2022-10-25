/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 *
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;

public final class MainWindow {
    private static MainWindow window;

    public static MainWindow mainWindow() {
	return MainWindow.window;
    }

    public static void createMainWindow(final int width, final int height) {
	if (MainWindow.window == null) {
	    MainWindow.window = new MainWindow(width, height);
	    MainWindow.window.frame.setIconImage(CommonDialogs.icon());
	}
    }

    static JFrame owner() {
	return MainWindow.mainWindow().frame;
    }

    private final JFrame frame;
    private final Dimension contentSize;
    private JComponent content;
    private final LinkedList<JComponent> savedContentStack;
    private final LinkedList<String> savedTitleStack;

    private MainWindow(final int width, final int height) {
	this.frame = new JFrame();
	this.frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.frame.setResizable(false);
	this.contentSize = new Dimension(width, height);
	this.content = this.createContent();
	this.savedContentStack = new LinkedList<>();
	this.savedTitleStack = new LinkedList<>();
	this.frame.setContentPane(this.content);
	this.frame.setVisible(true);
	this.frame.pack();
    }

    public void addKeyListener(final KeyListener l) {
	this.frame.addKeyListener(l);
    }

    public void addWindowListener(final WindowListener l) {
	this.frame.addWindowListener(l);
    }

    public void checkAndSetTitle(final String title) {
	if (!this.savedTitleStack.isEmpty()) {
	    this.frame.setTitle(title);
	}
    }

    public boolean checkContent(final JComponent customContent) {
	return this.content.equals(customContent);
    }

    JComponent content() {
	return this.content;
    }

    public int getHeight() {
	return this.content.getHeight();
    }

    public Dimension getPreferredSize() {
	return this.contentSize;
    }

    public int getWidth() {
	return this.content.getWidth();
    }

    public boolean isEnabled() {
	return this.frame.isEnabled();
    }

    void pack() {
	this.frame.pack();
    }

    public void removeKeyListener(final KeyListener l) {
	this.frame.removeKeyListener(l);
    }

    public void removeTransferHandler() {
	this.frame.setTransferHandler(null);
    }

    public void removeWindowListener(final WindowListener l) {
	this.frame.removeWindowListener(l);
    }

    public void restoreSaved() {
	this.content = this.savedContentStack.pop();
	this.frame.setContentPane(this.content);
	this.frame.setTitle(this.savedTitleStack.pop());
    }

    public void setAndSave(final JComponent customContent, final String title) {
	this.savedContentStack.push(this.content);
	this.savedTitleStack.push(this.frame.getTitle());
	this.content = customContent;
	this.frame.setContentPane(this.content);
	this.frame.setTitle(title);
    }

    public void setAndSave(final ScreenController screen) {
	this.savedContentStack.push(this.content);
	this.savedTitleStack.push(this.frame.getTitle());
	this.content = screen.content();
	this.frame.setContentPane(this.content);
	this.frame.setTitle(screen.title());
    }

    public void setDefaultButton(final JButton defaultButton) {
	this.frame.getRootPane().setDefaultButton(defaultButton);
    }

    public void setDirty(final boolean newDirty) {
	this.frame.getRootPane().putClientProperty("Window.documentModified", Boolean.valueOf(newDirty));
    }

    public void setEnabled(final boolean value) {
	this.frame.setEnabled(value);
    }

    public void setMenus(final JMenuBar menus) {
	this.frame.setJMenuBar(menus);
    }

    public void setTransferHandler(final TransferHandler h) {
	this.frame.setTransferHandler(h);
    }

    public final JComponent createContent() {
	var newContent = new JPanel();
	newContent.setPreferredSize(this.contentSize);
	newContent.setMinimumSize(this.contentSize);
	newContent.setMaximumSize(this.contentSize);
	newContent.setSize(this.contentSize);
	return newContent;
    }
}
