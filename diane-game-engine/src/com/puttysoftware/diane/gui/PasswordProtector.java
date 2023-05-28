/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.puttysoftware.diane.fileio.XDataReader;
import com.puttysoftware.diane.fileio.XDataWriter;
import com.puttysoftware.diane.utility.Hash;
import com.puttysoftware.diane.utility.HexBytes;

public class PasswordProtector {
    protected static final int MODE_SET = 1;
    protected static final int MODE_GET = 2;
    protected static final int MODE_GET_RAW = 3;
    // Fields
    private JDialog passwordDialog;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JPanel buttonPanel;
    private JButton okButton, cancelButton;
    private XDataWriter passwordWrite;
    private XDataReader passwordRead;
    private int mode;
    private boolean success = false;
    private final boolean waitingForInput = false;

    // Private constructor
    public PasswordProtector() {
	this.createComponents();
    }

    protected void checkPassword() {
	final var hashedInput = this.hashPassword();
	String check;
	try {
	    check = this.passwordRead.readString();
	} catch (final IOException ioe) {
	    check = ""; //$NON-NLS-1$
	}
	this.success = hashedInput.equals(check);
    }

    private void createComponents() {
	this.passwordDialog = new JDialog();
	this.passwordLabel = new JLabel();
	this.passwordField = new JPasswordField();
	this.passwordField.setEchoChar('X');
	this.buttonPanel = new JPanel();
	this.okButton = new JButton("OK"); //$NON-NLS-1$
	this.okButton.setDefaultCapable(true);
	this.okButton.addActionListener(e -> {
	    final var pp = PasswordProtector.this;
	    if (pp.getMode() == PasswordProtector.MODE_GET) {
		pp.checkPassword();
	    } else if (pp.getMode() == PasswordProtector.MODE_SET) {
		pp.savePassword();
	    } else {
		pp.success();
	    }
	    pp.hideForm();
	});
	this.cancelButton = new JButton("Cancel"); //$NON-NLS-1$
	this.cancelButton.setDefaultCapable(false);
	this.cancelButton.addActionListener(e -> {
	    final var pp = PasswordProtector.this;
	    pp.failure();
	    pp.hideForm();
	});
	this.passwordDialog.getRootPane().setDefaultButton(this.okButton);
	this.buttonPanel.setLayout(new FlowLayout());
	this.buttonPanel.add(this.cancelButton);
	this.buttonPanel.add(this.okButton);
	this.passwordDialog.getContentPane().add(this.passwordLabel, BorderLayout.NORTH);
	this.passwordDialog.getContentPane().add(this.passwordField, BorderLayout.CENTER);
	this.passwordDialog.getContentPane().add(this.buttonPanel, BorderLayout.SOUTH);
	this.passwordDialog.pack();
    }

    protected void failure() {
	this.success = false;
    }

    protected int getMode() {
	return this.mode;
    }

    public String getPasswordHash() {
	if (this.getSuccess()) {
	    return this.hashPassword();
	}
	return null;
    }

    public boolean getSuccess() {
	return this.success;
    }

    private String hashPassword() {
	try {
	    final var pw = this.passwordField.getPassword();
	    final var bytes = Arrays.toString(pw).getBytes("UTF-8"); //$NON-NLS-1$
	    final var hashed = Hash.hash(bytes);
	    Arrays.fill(pw, '\0');
	    for (var x = 0; x < bytes.length; x++) {
		bytes[x] = 0;
	    }
	    return HexBytes.hexBytes(hashed);
	} catch (final UnsupportedEncodingException uee) {
	    return ""; //$NON-NLS-1$
	}
    }

    protected void hideForm() {
	this.passwordDialog.setVisible(false);
    }

    public void promptForPassword() {
	this.passwordDialog.setTitle("Enter Password"); //$NON-NLS-1$
	this.passwordLabel.setText("Type the password below (it will be hidden as you type):"); //$NON-NLS-1$
	this.passwordField.setText(null);
	this.passwordDialog.pack();
	this.mode = PasswordProtector.MODE_GET_RAW;
	this.passwordDialog.setVisible(true);
    }

    public void promptForPassword(final XDataReader passwordFile) {
	this.passwordDialog.setTitle("Enter Password"); //$NON-NLS-1$
	this.passwordLabel.setText("Type the password below (it will be hidden as you type):"); //$NON-NLS-1$
	this.passwordField.setText(null);
	this.passwordDialog.pack();
	this.passwordRead = passwordFile;
	this.mode = PasswordProtector.MODE_GET;
	this.passwordDialog.setVisible(true);
    }

    protected void savePassword() {
	final var hashedPW = this.hashPassword();
	try {
	    this.passwordWrite.writeString(hashedPW);
	    this.success = true;
	} catch (final IOException ioe) {
	    this.success = false;
	}
    }

    public void setPassword(final XDataWriter passwordFile) {
	this.passwordDialog.setTitle("Set Password"); //$NON-NLS-1$
	this.passwordLabel.setText("Type the new password below (it will be hidden as you type):"); //$NON-NLS-1$
	this.passwordField.setText(null);
	this.passwordDialog.pack();
	this.passwordWrite = passwordFile;
	this.mode = PasswordProtector.MODE_SET;
	this.passwordDialog.setVisible(true);
    }

    protected void success() {
	this.success = true;
    }

    public boolean waitingForInput() {
	return this.waitingForInput;
    }
}