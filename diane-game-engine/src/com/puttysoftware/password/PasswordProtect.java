package com.puttysoftware.password;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.puttysoftware.diane.gui.MainContent;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class PasswordProtect {
  // Fields
  private static MainWindow passwordFrame;
  private static MainContent passwordContent;
  private static JLabel passwordLabel;
  private static JPasswordField passwordField;
  private static JPanel buttonPanel;
  private static JButton okButton, cancelButton;
  private static XDataWriter passwordWrite;
  private static XDataReader passwordRead;
  private static int mode;
  private static boolean waitingForInput = false;
  private static boolean success = false;
  protected static final int MODE_SET = 1;
  protected static final int MODE_GET = 2;
  protected static final int MODE_GET_RAW = 3;

  // Private constructor
  private PasswordProtect() {
    // Do nothing
  }

  // Methods
  public static void setPassword(final XDataWriter passwordFile) {
    PasswordProtect.createComponents();
    PasswordProtect.setUpFrame();
    PasswordProtect.passwordFrame.setTitle("Set Password"); //$NON-NLS-1$
    PasswordProtect.passwordLabel.setText(
        "Type the new password below (it will be hidden as you type):"); //$NON-NLS-1$
    PasswordProtect.passwordField.setText(null);
    PasswordProtect.passwordFrame.pack();
    PasswordProtect.passwordWrite = passwordFile;
    PasswordProtect.mode = PasswordProtect.MODE_SET;
    PasswordProtect.waitingForInput = true;
  }

  public static void promptForPassword(final XDataReader passwordFile) {
    PasswordProtect.createComponents();
    PasswordProtect.setUpFrame();
    PasswordProtect.passwordFrame.setTitle("Enter Password"); //$NON-NLS-1$
    PasswordProtect.passwordLabel
        .setText("Type the password below (it will be hidden as you type):"); //$NON-NLS-1$
    PasswordProtect.passwordField.setText(null);
    PasswordProtect.passwordFrame.pack();
    PasswordProtect.passwordRead = passwordFile;
    PasswordProtect.mode = PasswordProtect.MODE_GET;
    PasswordProtect.waitingForInput = true;
  }

  public static void promptForPassword() {
    PasswordProtect.createComponents();
    PasswordProtect.setUpFrame();
    PasswordProtect.passwordFrame.setTitle("Enter Password"); //$NON-NLS-1$
    PasswordProtect.passwordLabel
        .setText("Type the password below (it will be hidden as you type):"); //$NON-NLS-1$
    PasswordProtect.passwordField.setText(null);
    PasswordProtect.passwordFrame.pack();
    PasswordProtect.mode = PasswordProtect.MODE_GET_RAW;
    PasswordProtect.waitingForInput = true;
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
    return PasswordProtect.waitingForInput;
  }

  private static void setUpFrame() {
    PasswordProtect.passwordFrame = MainWindow.getOutputFrame();
    PasswordProtect.passwordFrame.setDefaultButton(PasswordProtect.okButton);
    PasswordProtect.passwordFrame.setContentPane(passwordContent);
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
    final String hashedPW = PasswordProtect.hashPassword();
    try {
      PasswordProtect.passwordWrite.writeString(hashedPW);
      PasswordProtect.success = true;
    } catch (final IOException ioe) {
      PasswordProtect.success = false;
    }
  }

  protected static void hideForm() {
    PasswordProtect.passwordFrame.setDefaultButton(null);
  }

  protected static void checkPassword() {
    final String hashedInput = PasswordProtect.hashPassword();
    String check;
    try {
      check = PasswordProtect.passwordRead.readString();
    } catch (final IOException ioe) {
      check = ""; //$NON-NLS-1$
    }
    PasswordProtect.success = hashedInput.equals(check);
  }

  private static String hashPassword() {
    try {
      final char[] pw = PasswordProtect.passwordField.getPassword();
      final byte[] bytes = Arrays.toString(pw).getBytes("UTF-8"); //$NON-NLS-1$
      final byte[] hashed = Hash.hash(bytes);
      for (int x = 0; x < pw.length; x++) {
        pw[x] = '\0';
      }
      for (int x = 0; x < bytes.length; x++) {
        bytes[x] = 0;
      }
      return HexBytes.hexBytes(hashed);
    } catch (final UnsupportedEncodingException uee) {
      return ""; //$NON-NLS-1$
    }
  }

  private static void createComponents() {
    PasswordProtect.passwordContent = new MainContent();
    PasswordProtect.passwordLabel = new JLabel();
    PasswordProtect.passwordField = new JPasswordField();
    PasswordProtect.passwordField.setEchoChar('X');
    PasswordProtect.buttonPanel = new JPanel();
    PasswordProtect.okButton = new JButton("OK"); //$NON-NLS-1$
    PasswordProtect.okButton.setDefaultCapable(true);
    PasswordProtect.okButton.addActionListener(e -> {
      PasswordProtect.waitingForInput = false;
      if (PasswordProtect.getMode() == PasswordProtect.MODE_GET) {
        PasswordProtect.checkPassword();
      } else if (PasswordProtect.getMode() == PasswordProtect.MODE_SET) {
        PasswordProtect.savePassword();
      } else {
        PasswordProtect.success();
      }
      PasswordProtect.hideForm();
    });
    PasswordProtect.cancelButton = new JButton("Cancel"); //$NON-NLS-1$
    PasswordProtect.cancelButton.setDefaultCapable(false);
    PasswordProtect.cancelButton.addActionListener(e -> {
      PasswordProtect.waitingForInput = false;
      PasswordProtect.failure();
      PasswordProtect.hideForm();
    });
    PasswordProtect.buttonPanel.setLayout(new FlowLayout());
    PasswordProtect.buttonPanel.add(PasswordProtect.cancelButton);
    PasswordProtect.buttonPanel.add(PasswordProtect.okButton);
    PasswordProtect.passwordContent.add(PasswordProtect.passwordLabel,
        BorderLayout.NORTH);
    PasswordProtect.passwordContent.add(PasswordProtect.passwordField,
        BorderLayout.CENTER);
    PasswordProtect.passwordContent.add(PasswordProtect.buttonPanel,
        BorderLayout.SOUTH);
  }
}