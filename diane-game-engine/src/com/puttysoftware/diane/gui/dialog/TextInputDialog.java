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
import com.puttysoftware.diane.locale.Strings;
import com.puttysoftware.diane.locale.Translations;

class TextInputDialog {
    private static MainWindow dialogFrame;
    private static MainWindowContent dialogPane;
    private static JTextField input;
    private static CompletableFuture<String> completer = new CompletableFuture<>();

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
	    dialogFrame = MainWindow.getMainWindow();
	    dialogPane = dialogFrame.createContent();
	    // Create and initialize the buttons.
	    final JButton cancelButton = new JButton(Translations.load(Strings.CANCEL_BUTTON));
	    cancelButton.addActionListener(h -> {
		TextInputDialog.setValue(null);
		dialogFrame.restoreSaved();
	    });
	    final JButton setButton = new JButton(Translations.load(Strings.OK_BUTTON));
	    setButton.setActionCommand(Translations.load(Strings.OK_BUTTON));
	    setButton.addActionListener(h -> {
		TextInputDialog.setValue(TextInputDialog.input.getText());
		dialogFrame.restoreSaved();
	    });
	    // main part of the dialog
	    TextInputDialog.input = new JTextField(initialValue);
	    final JPanel iconPane = new JPanel();
	    final JLabel iconLabel = new JLabel(icon);
	    iconPane.setLayout(new BoxLayout(iconPane, BoxLayout.PAGE_AXIS));
	    iconPane.add(iconLabel);
	    final JPanel mainPane = new JPanel();
	    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
	    final JLabel textLabel = new JLabel(text);
	    mainPane.add(textLabel);
	    mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
	    mainPane.add(input);
	    mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    // Lay out the buttons from left to right.
	    final JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    buttonPane.add(Box.createHorizontalGlue());
	    buttonPane.add(cancelButton);
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    buttonPane.add(setButton);
	    // Put everything together, using the content pane's BorderLayout.
	    dialogPane.add(iconPane, BorderLayout.WEST);
	    dialogPane.add(mainPane, BorderLayout.CENTER);
	    dialogPane.add(buttonPane, BorderLayout.SOUTH);
	    // Initialize values.
	    TextInputDialog.setValue(initialValue);
	    dialogFrame.attachAndSave(dialogPane);
	});
	return completer;
    }

    private static void setValue(final String newValue) {
	completer.complete(newValue);
    }
}
