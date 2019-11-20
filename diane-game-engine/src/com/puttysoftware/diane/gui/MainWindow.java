package com.puttysoftware.diane.gui;

import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class MainWindow {
  private static MainWindow window;
  private JFrame frame;
  private GameCanvas content;

  private MainWindow() {
    super();
    this.frame = new JFrame();
    this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.frame.setResizable(false);
    this.content = new GameCanvas();
    this.frame.setContentPane(this.content.getCanvas());
    this.frame.setVisible(true);
  }

  public static MainWindow getOutputFrame() {
    if (MainWindow.window == null) {
      MainWindow.window = new MainWindow();
    }
    return MainWindow.window;
  }

  public void attachCanvas() {
    this.frame.setContentPane(this.content.getCanvas());
  }

  public void attachCustomCanvas(final GameCanvas customCanvas) {
    this.frame.setContentPane(customCanvas.getCanvas());
  }

  public void setTitle(final String title) {
    this.frame.setTitle(title);
  }

  public void pack() {
    this.frame.pack();
  }

  public void addWindowListener(final WindowListener l) {
    this.frame.addWindowListener(l);
  }

  public void removeWindowListener(final WindowListener l) {
    this.frame.removeWindowListener(l);
  }

  public void addKeyListener(final KeyListener l) {
    this.frame.addKeyListener(l);
  }

  public void removeKeyListener(final KeyListener l) {
    this.frame.removeKeyListener(l);
  }

  public void setContentPane(final Container contentPane) {
    this.frame.setContentPane(contentPane);
  }

  public void setDefaultButton(final JButton defaultButton) {
    this.frame.getRootPane().setDefaultButton(defaultButton);
  }
}
