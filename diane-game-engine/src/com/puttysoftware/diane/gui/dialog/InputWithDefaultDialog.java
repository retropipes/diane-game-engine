/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.puttysoftware.diane.asset.BufferedImageIcon;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.locale.ErrorString;
import com.puttysoftware.diane.locale.PrivateStrings;

class InputWithDefaultDialog {
    private static MainWindow dialogFrame;
    private static JComponent dialogPane;
    private static CompletableFuture<Integer> completer;

    private static void initializeDialog(final String text, final String title, final BufferedImageIcon icon,
	    final String[] possibleValues, final String defaultButtonName) {
	// Create and initialize the dialog.
	InputWithDefaultDialog.dialogFrame = MainWindow.mainWindow();
	InputWithDefaultDialog.dialogPane = InputWithDefaultDialog.dialogFrame.createContent();
	// main part of the dialog
	final var iconPane = new JPanel();
	final var iconLabel = new JLabel(icon);
	iconPane.setLayout(new BoxLayout(iconPane, BoxLayout.PAGE_AXIS));
	iconPane.add(iconLabel);
	final var mainPane = new JPanel();
	mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
	final var textLabel = new JLabel(text);
	mainPane.add(textLabel);
	mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
	mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	// Lay out the buttons from left to right.
	final var buttonPane = new JPanel();
	buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	buttonPane.add(Box.createHorizontalGlue());
	// Create and initialize the buttons.
	JButton defaultButton = null;
	for (var i = 0; i < possibleValues.length; i++) {
	    final var button = new JButton(possibleValues[i]);
	    button.setActionCommand(Integer.toString(i));
	    button.addActionListener(h -> {
		InputWithDefaultDialog.setValue(Integer.parseInt(h.getActionCommand()));
		InputWithDefaultDialog.dialogFrame.restoreSaved();
	    });
	    buttonPane.add(button);
	    if (possibleValues[i] == defaultButtonName) {
		defaultButton = button;
	    }
	    if (i != possibleValues.length - 1) {
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    }
	}
	// Put everything together, using the content pane's BorderLayout.
	InputWithDefaultDialog.dialogPane.add(iconPane, BorderLayout.WEST);
	InputWithDefaultDialog.dialogPane.add(mainPane, BorderLayout.CENTER);
	InputWithDefaultDialog.dialogPane.add(buttonPane, BorderLayout.SOUTH);
	InputWithDefaultDialog.dialogFrame.setAndSave(InputWithDefaultDialog.dialogPane, title, defaultButton);
    }

    private static void setValue(final int newValue) {
	InputWithDefaultDialog.completer.complete(newValue);
    }

    /**
     * Set up and show the dialog. The first Component argument determines which
     * frame the dialog depends on; it should be a component in the dialog's
     * controlling frame. The second Component argument should be null if you want
     * the dialog to come up with its left corner in the center of the screen;
     * otherwise, it should be the component on top of which the dialog should
     * appear.
     */
    public static Future<Integer> showConfirmDialog(final String text, final String title, final BufferedImageIcon icon,
	    final String defaultButton) {
	InputWithDefaultDialog.completer = new CompletableFuture<>();
	Executors.newSingleThreadExecutor().submit(() -> {
	    final String[] possibleValues = { "Yes", "No" };
	    InputWithDefaultDialog.initializeDialog(text, title, icon, possibleValues, defaultButton);
	});
	return InputWithDefaultDialog.completer;
    }

    public static Future<Integer> showDialog(final String text, final String title, final BufferedImageIcon icon,
	    final String[] possibleValues, final String defaultButton) {
	InputWithDefaultDialog.completer = new CompletableFuture<>();
	Executors.newSingleThreadExecutor().submit(() -> {
	    InputWithDefaultDialog.initializeDialog(text, title, icon, possibleValues, defaultButton);
	});
	return InputWithDefaultDialog.completer;
    }

    public static Future<Integer> showYNCConfirmDialog(final String text, final String title,
	    final BufferedImageIcon icon, final String defaultButton) {
	InputWithDefaultDialog.completer = new CompletableFuture<>();
	Executors.newSingleThreadExecutor().submit(() -> {
	    final String[] possibleValues = { "Yes", "No", PrivateStrings.error(ErrorString.CANCEL_BUTTON) };
	    InputWithDefaultDialog.initializeDialog(text, title, icon, possibleValues, defaultButton);
	});
	return InputWithDefaultDialog.completer;
    }
}
