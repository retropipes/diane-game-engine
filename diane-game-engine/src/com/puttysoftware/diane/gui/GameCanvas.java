package com.puttysoftware.diane.gui;

import javax.swing.JPanel;

public class GameCanvas {
  private JPanel canvas;

  public GameCanvas() {
    this.canvas = new JPanel();
  }

  JPanel getCanvas() {
    return this.canvas;
  }
}
