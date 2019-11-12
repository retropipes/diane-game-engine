package com.puttysoftware.diane.gui;

import javax.swing.JLabel;

import com.puttysoftware.diane.internal.BufferedImageIcon;

@SuppressWarnings("serial")
public final class GameLabel extends JLabel {
  public GameLabel() {
  }

  public GameLabel(String inText) {
    super(inText);
  }

  public GameLabel(GameImage inImage) {
    super(new BufferedImageIcon(inImage));
  }

  public GameLabel(String inText, int inHorizontalAlignment) {
    super(inText, inHorizontalAlignment);
  }

  public GameLabel(GameImage inImage, int inHorizontalAlignment) {
    super(new BufferedImageIcon(inImage), inHorizontalAlignment);
  }

  public GameLabel(String inText, GameImage inImage,
      int inHorizontalAlignment) {
    super(inText, new BufferedImageIcon(inImage), inHorizontalAlignment);
  }

  public void setIcon(GameImage inImage) {
    super.setIcon(new BufferedImageIcon(inImage));
  }
}
