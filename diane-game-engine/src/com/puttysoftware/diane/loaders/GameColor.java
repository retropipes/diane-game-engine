package com.puttysoftware.diane.loaders;

import java.awt.Color;
import java.awt.color.ColorSpace;

public class GameColor {
  private final Color color;

  public GameColor(final int inRgba) {
    this.color = new Color(inRgba, true);
  }

  public GameColor(final int inRgba, final boolean ignored) {
    this.color = new Color(inRgba, true);
  }

  public GameColor(final int inR, final int inG, final int inB) {
    this.color = new Color(inR, inG, inB, 255);
  }

  public GameColor(final int inR, final int inG, final int inB, final int inA) {
    this.color = new Color(inR, inG, inB, inA);
  }

  GameColor(final ColorSpace space, final float[] entries, final float data) {
    this.color = new Color(space, entries, data);
  }

  public int getRed() {
    return this.color.getRed();
  }

  public int getGreen() {
    return this.color.getGreen();
  }

  public int getBlue() {
    return this.color.getBlue();
  }

  public int getAlpha() {
    return this.color.getAlpha();
  }

  public int getRGB() {
    return this.color.getRGB();
  }

  float[] getColorComponents(final ColorSpace space) {
    return this.color.getColorComponents(space, null);
  }
}
