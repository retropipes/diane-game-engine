package com.puttysoftware.diane.loaders;

import java.awt.Color;
import java.awt.color.ColorSpace;

public class GameColor {
  private final Color color;

  public GameColor(int inRgba) {
    this.color = new Color(inRgba, true);
  }

  public GameColor(int inR, int inG, int inB, int inA) {
    this.color = new Color(inR, inG, inB, inA);
  }

  GameColor(final ColorSpace space, float[] entries, float data) {
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

  float[] getColorComponents(ColorSpace space) {
    return this.color.getColorComponents(space, null);
  }
}
