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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.puttysoftware.diane.asset.BufferedImageIcon;
import com.puttysoftware.diane.locale.ErrorString;
import com.puttysoftware.diane.locale.PrivateStrings;

class TextInputDialog {
    private static MainWindow dialogFrame;
    private static MainWindowContent dialogPane;
    private static JTextField input;
    private static CompletableFuture<String> completer = new CompletableFuture<>();

    private static void setValue(final String newValue) {
	TextInputDialog.completer.complete(newValue);
    }

    /**
     * Set up and show the dialog. The first Component argument determines which
     * frame the dialog depends on; it should be a component in the dialog's
     * controlling frame. The second Component argument should be null if you want
     * the dialog to come up with its left corner in the center of the screen;
     * otherwise, it should be the component on top of which the dialog should
     * appear.
     */
    public static Future<String> showDialog(final String text, final String title, final BufferedImageIcon icon,
	    final String initialValue) {
	Executors.newSingleThreadExecutor().submit(() -> {
	    // Create and initialize the dialog.
	    TextInputDialog.dialogFrame = MainWindow.getMainWindow();
	    TextInputDialog.dialogPane = TextInputDialog.dialogFrame.createContent();
	    // Create and initialize the buttons.
	    final var cancelButton = new JButton(PrivateStrings.error(ErrorString.CANCEL_BUTTON));
	    cancelButton.addActionListener(h -> {
		TextInputDialog.setValue(null);
		TextInputDialog.dialogFrame.restoreSaved();
	    });
	    final var setButton = new JButton(PrivateStrings.error(ErrorString.OK_BUTTON));
	    setButton.setActionCommand(PrivateStrings.error(ErrorString.OK_BUTTON));
	    setButton.addActionListener(h -> {
		TextInputDialog.setValue(TextInputDialog.input.getText());
		TextInputDialog.dialogFrame.restoreSaved();
	    });
	    // main part of the dialog
	    TextInputDialog.input = new JTextField(initialValue);
	    final var iconPane = new JPanel();
	    final var iconLabel = new JLabel(icon);
	    iconPane.setLayout(new BoxLayout(iconPane, BoxLayout.PAGE_AXIS));
	    iconPane.add(iconLabel);
	    final var mainPane = new JPanel();
	    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
	    final var textLabel = new JLabel(text);
	    mainPane.add(textLabel);
	    mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
	    mainPane.add(TextInputDialog.input);
	    mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    // Lay out the buttons from left to right.
	    final var buttonPane = new JPanel();
	    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    buttonPane.add(Box.createHorizontalGlue());
	    buttonPane.add(cancelButton);
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    buttonPane.add(setButton);
	    // Put everything together, using the content pane's BorderLayout.
	    TextInputDialog.dialogPane.add(iconPane, BorderLayout.WEST);
	    TextInputDialog.dialogPane.add(mainPane, BorderLayout.CENTER);
	    TextInputDialog.dialogPane.add(buttonPane, BorderLayout.SOUTH);
	    // Initialize values.
	    TextInputDialog.setValue(initialValue);
	    TextInputDialog.dialogFrame.attachAndSave(TextInputDialog.dialogPane);
	});
	return TextInputDialog.completer;
    }
}
