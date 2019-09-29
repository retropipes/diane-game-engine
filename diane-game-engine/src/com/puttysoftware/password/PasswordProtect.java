package com.puttysoftware.password;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class PasswordProtect {
    // Fields
    private static JFrame passwordFrame;
    private static JLabel passwordLabel;
    private static JPasswordField passwordField;
    private static JPanel buttonPanel;
    private static JButton okButton, cancelButton;
    private static XDataWriter passwordWrite;
    private static XDataReader passwordRead;
    private static int mode;
    private static boolean success = false;
    protected static final int MODE_SET = 1;
    protected static final int MODE_GET = 2;
    protected static final int MODE_GET_RAW = 3;

    // Private constructor
    private PasswordProtect() {
        // Do nothing
    }

    // Methods
    public static void setPassword(XDataWriter passwordFile) {
        PasswordProtect.createComponents();
        PasswordProtect.passwordFrame.setTitle("Set Password"); //$NON-NLS-1$
        PasswordProtect.passwordLabel.setText(
                "Type the new password below (it will be hidden as you type):"); //$NON-NLS-1$
        PasswordProtect.passwordField.setText(null);
        PasswordProtect.passwordFrame.pack();
        PasswordProtect.passwordWrite = passwordFile;
        PasswordProtect.mode = PasswordProtect.MODE_SET;
        PasswordProtect.passwordFrame.setVisible(true);
    }

    public static void promptForPassword(XDataReader passwordFile) {
        PasswordProtect.createComponents();
        PasswordProtect.passwordFrame.setTitle("Enter Password"); //$NON-NLS-1$
        PasswordProtect.passwordLabel.setText(
                "Type the password below (it will be hidden as you type):"); //$NON-NLS-1$
        PasswordProtect.passwordField.setText(null);
        PasswordProtect.passwordFrame.pack();
        PasswordProtect.passwordRead = passwordFile;
        PasswordProtect.mode = PasswordProtect.MODE_GET;
        PasswordProtect.passwordFrame.setVisible(true);
    }

    public static void promptForPassword() {
        PasswordProtect.createComponents();
        PasswordProtect.passwordFrame.setTitle("Enter Password"); //$NON-NLS-1$
        PasswordProtect.passwordLabel.setText(
                "Type the password below (it will be hidden as you type):"); //$NON-NLS-1$
        PasswordProtect.passwordField.setText(null);
        PasswordProtect.passwordFrame.pack();
        PasswordProtect.mode = PasswordProtect.MODE_GET_RAW;
        PasswordProtect.passwordFrame.setVisible(true);
    }

    public static boolean getSuccess() {
        return PasswordProtect.success;
    }

    public static String getPasswordHash() {
        if (PasswordProtect.getSuccess()) {
            return PasswordProtect.hashPassword();
        }
        return null;
    }

    public static boolean waitingForInput() {
        if (PasswordProtect.passwordFrame != null) {
            return PasswordProtect.passwordFrame.isVisible();
        }
        return false;
    }

    protected static int getMode() {
        return PasswordProtect.mode;
    }

    protected static void failure() {
        PasswordProtect.success = false;
    }

    protected static void success() {
        PasswordProtect.success = true;
    }

    protected static void savePassword() {
        String hashedPW = PasswordProtect.hashPassword();
        try {
            PasswordProtect.passwordWrite.writeString(hashedPW);
            PasswordProtect.success = true;
        } catch (IOException ioe) {
            PasswordProtect.success = false;
        }
    }

    protected static void hideForm() {
        PasswordProtect.passwordFrame.setVisible(false);
    }

    protected static void checkPassword() {
        String hashedInput = PasswordProtect.hashPassword();
        String check;
        try {
            check = PasswordProtect.passwordRead.readString();
        } catch (IOException ioe) {
            check = ""; //$NON-NLS-1$
        }
        PasswordProtect.success = hashedInput.equals(check);
    }

    private static String hashPassword() {
        try {
            char[] pw = PasswordProtect.passwordField.getPassword();
            byte[] bytes = Arrays.toString(pw).getBytes("UTF-8"); //$NON-NLS-1$
            byte[] hashed = Hash.hash(bytes);
            for (int x = 0; x < pw.length; x++) {
                pw[x] = '\0';
            }
            for (int x = 0; x < bytes.length; x++) {
                bytes[x] = 0;
            }
            return HexBytes.hexBytes(hashed);
        } catch (UnsupportedEncodingException uee) {
            return ""; //$NON-NLS-1$
        }
    }

    private static void createComponents() {
        if (PasswordProtect.passwordFrame == null) {
            PasswordProtect.passwordFrame = new JFrame();
            PasswordProtect.passwordLabel = new JLabel();
            PasswordProtect.passwordField = new JPasswordField();
            PasswordProtect.passwordField.setEchoChar('X');
            PasswordProtect.buttonPanel = new JPanel();
            PasswordProtect.okButton = new JButton("OK"); //$NON-NLS-1$
            PasswordProtect.okButton.setDefaultCapable(true);
            PasswordProtect.okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (PasswordProtect.getMode() == PasswordProtect.MODE_GET) {
                        PasswordProtect.checkPassword();
                    } else if (PasswordProtect
                            .getMode() == PasswordProtect.MODE_SET) {
                        PasswordProtect.savePassword();
                    } else {
                        PasswordProtect.success();
                    }
                    PasswordProtect.hideForm();
                }
            });
            PasswordProtect.cancelButton = new JButton("Cancel"); //$NON-NLS-1$
            PasswordProtect.cancelButton.setDefaultCapable(false);
            PasswordProtect.cancelButton
                    .addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            PasswordProtect.failure();
                            PasswordProtect.hideForm();
                        }
                    });
            PasswordProtect.passwordFrame.getRootPane()
                    .setDefaultButton(PasswordProtect.okButton);
            PasswordProtect.buttonPanel.setLayout(new FlowLayout());
            PasswordProtect.buttonPanel.add(PasswordProtect.cancelButton);
            PasswordProtect.buttonPanel.add(PasswordProtect.okButton);
            PasswordProtect.passwordFrame.getContentPane()
                    .add(PasswordProtect.passwordLabel, BorderLayout.NORTH);
            PasswordProtect.passwordFrame.getContentPane()
                    .add(PasswordProtect.passwordField, BorderLayout.CENTER);
            PasswordProtect.passwordFrame.getContentPane()
                    .add(PasswordProtect.buttonPanel, BorderLayout.SOUTH);
            PasswordProtect.passwordFrame.pack();
        }
    }
}