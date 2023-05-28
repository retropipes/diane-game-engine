/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainContentFactory {
    private static int CONTENT_WIDTH = 800;
    private static int CONTENT_HEIGHT = 600;

    public static JPanel content() {
	final var result = new JPanel();
	result.setPreferredSize(new Dimension(MainContentFactory.CONTENT_WIDTH, MainContentFactory.CONTENT_HEIGHT));
	return result;
    }

    public static JScrollPane scrollingContent(final JComponent view) {
	final var result = new JScrollPane(view);
	result.setPreferredSize(new Dimension(MainContentFactory.CONTENT_WIDTH, MainContentFactory.CONTENT_HEIGHT));
	return result;
    }

    public static void setContentSize(final int w, final int h) {
	MainContentFactory.CONTENT_WIDTH = w;
	MainContentFactory.CONTENT_HEIGHT = h;
    }

    private MainContentFactory() {
	// do nothing
    }
}
