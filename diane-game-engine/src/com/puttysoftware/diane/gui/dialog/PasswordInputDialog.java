/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
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
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.internal.PrivateErrorString;
import com.puttysoftware.diane.internal.PrivateStrings;

class PasswordInputDialog {
    private static MainWindow dialogFrame;
    private static JComponent dialogPane;
    private static JPasswordField input;
    private static CompletableFuture<char[]> completer = new CompletableFuture<>();

    private static void setValue(final char[] newValue) {
        PasswordInputDialog.completer.complete(newValue);
    }

    /**
     * Set up and show the dialog. The first Component argument determines which
     * frame the dialog depends on; it should be a component in the dialog's
     * controlling frame. The second Component argument should be null if you want
     * the dialog to come up with its left corner in the center of the screen;
     * otherwise, it should be the component on top of which the dialog should
     * appear.
     */
    public static Future<char[]> showDialog(final String text, final String title, final BufferedImageIcon icon) {
        Executors.newSingleThreadExecutor().submit(() -> {
            // Create and initialize the dialog.
            PasswordInputDialog.dialogFrame = MainWindow.mainWindow();
            PasswordInputDialog.dialogPane = PasswordInputDialog.dialogFrame.createContent();
            // Create and initialize the buttons.
            final var cancelButton = new JButton(PrivateStrings.error(PrivateErrorString.CANCEL_BUTTON));
            cancelButton.addActionListener(h -> {
                PasswordInputDialog.setValue(null);
                PasswordInputDialog.dialogFrame.restoreSaved();
            });
            final var setButton = new JButton(PrivateStrings.error(PrivateErrorString.OK_BUTTON));
            setButton.setActionCommand(PrivateStrings.error(PrivateErrorString.OK_BUTTON));
            setButton.addActionListener(h -> {
                PasswordInputDialog.setValue(PasswordInputDialog.input.getPassword());
                PasswordInputDialog.dialogFrame.restoreSaved();
            });
            // main part of the dialog
            PasswordInputDialog.input = new JPasswordField();
            final var iconPane = new JPanel();
            final var iconLabel = new JLabel(icon);
            iconPane.setLayout(new BoxLayout(iconPane, BoxLayout.PAGE_AXIS));
            iconPane.add(iconLabel);
            final var mainPane = new JPanel();
            mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
            final var textLabel = new JTextArea(text);
            textLabel.setEditable(false);
            textLabel.setLineWrap(true);
            textLabel.setWrapStyleWord(true);
            textLabel.setSize(CommonDialogs.DEFAULT_ELEM_WIDTH, CommonDialogs.DEFAULT_ELEM_HEIGHT);
            mainPane.add(textLabel);
            mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
            mainPane.add(PasswordInputDialog.input);
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
            PasswordInputDialog.dialogPane.add(iconPane, BorderLayout.WEST);
            PasswordInputDialog.dialogPane.add(mainPane, BorderLayout.CENTER);
            PasswordInputDialog.dialogPane.add(buttonPane, BorderLayout.SOUTH);
            // Initialize values.
            PasswordInputDialog.dialogFrame.setAndSave(PasswordInputDialog.dialogPane, title);
        });
        return PasswordInputDialog.completer;
    }
}
