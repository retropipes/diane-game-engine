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

import com.puttysoftware.diane.asset.BufferedImageIcon;
import com.puttysoftware.diane.locale.ErrorString;
import com.puttysoftware.diane.locale.PrivateStrings;

class GeneralDialog {
    private static MainWindow dialogFrame;
    private static MainWindowContent dialogPane;
    private static CompletableFuture<Void> completer = new CompletableFuture<>();

    /**
     * Set up and show the dialog.
     */
    public static Future<Void> showDialog(final String text, final String title, final BufferedImageIcon icon) {
	Executors.newSingleThreadExecutor().submit(() -> {
	    // Create and initialize the dialog.
	    GeneralDialog.dialogFrame = MainWindow.getMainWindow();
	    GeneralDialog.dialogPane = GeneralDialog.dialogFrame.createContent();
	    // Create and initialize the buttons.
	    final var setButton = new JButton(PrivateStrings.error(ErrorString.OK_BUTTON));
	    setButton.setActionCommand(PrivateStrings.error(ErrorString.OK_BUTTON));
	    setButton.addActionListener(h -> {
		GeneralDialog.completer.complete(null);
		GeneralDialog.dialogFrame.restoreSaved();
	    });
	    // main part of the dialog
	    final var iconPane = new JPanel();
	    final var iconLabel = new JLabel(icon);
	    iconPane.setLayout(new BoxLayout(iconPane, BoxLayout.PAGE_AXIS));
	    iconPane.add(iconLabel);
	    final var mainPane = new JPanel();
	    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
	    final var textLabel = new JLabel(text);
	    mainPane.add(textLabel);
	    mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    // Lay out the buttons from left to right.
	    final var buttonPane = new JPanel();
	    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    buttonPane.add(Box.createHorizontalGlue());
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    buttonPane.add(setButton);
	    // Put everything together, using the content pane's BorderLayout.
	    GeneralDialog.dialogPane.add(iconPane, BorderLayout.WEST);
	    GeneralDialog.dialogPane.add(mainPane, BorderLayout.CENTER);
	    GeneralDialog.dialogPane.add(buttonPane, BorderLayout.SOUTH);
	    GeneralDialog.dialogFrame.attachAndSave(GeneralDialog.dialogPane);
	});
	return GeneralDialog.completer;
    }
}
