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
import com.puttysoftware.diane.locale.Strings;
import com.puttysoftware.diane.locale.Translations;

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
	    dialogFrame = MainWindow.getMainWindow();
	    dialogPane = dialogFrame.createContent();
	    // Create and initialize the buttons.
	    final JButton setButton = new JButton(Translations.load(Strings.OK_BUTTON));
	    setButton.setActionCommand(Translations.load(Strings.OK_BUTTON));
	    setButton.addActionListener(h -> {
		completer.complete(null);
		dialogFrame.restoreSaved();
	    });
	    // main part of the dialog
	    final JPanel iconPane = new JPanel();
	    final JLabel iconLabel = new JLabel(icon);
	    iconPane.setLayout(new BoxLayout(iconPane, BoxLayout.PAGE_AXIS));
	    iconPane.add(iconLabel);
	    final JPanel mainPane = new JPanel();
	    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
	    final JLabel textLabel = new JLabel(text);
	    mainPane.add(textLabel);
	    mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    // Lay out the buttons from left to right.
	    final JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    buttonPane.add(Box.createHorizontalGlue());
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    buttonPane.add(setButton);
	    // Put everything together, using the content pane's BorderLayout.
	    dialogPane.add(iconPane, BorderLayout.WEST);
	    dialogPane.add(mainPane, BorderLayout.CENTER);
	    dialogPane.add(buttonPane, BorderLayout.SOUTH);
	    dialogFrame.attachAndSave(dialogPane);
	});
	return completer;
    }
}
