package com.puttysoftware.diane.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import com.puttysoftware.diane.internal.BufferedImageIcon;

public final class GameImage {
  // Fields
  private final BufferedImageIcon image;

  // Constructors
  /**
   * Creates a GameImage of a given size.
   *
   * @param width
   * @param height
   */
  public GameImage(final int width, final int height) {
    this.image = new BufferedImageIcon(width, height);
  }

  /**
   * Creates a GameImage based on another GameImage object.
   *
   * @param gi
   */
  public GameImage(final GameImage gi) {
    this.image = new BufferedImageIcon(gi.image.getWidth(),
        gi.image.getHeight());
    for (int x = 0; x < gi.image.getWidth(); x++) {
      for (int y = 0; y < gi.image.getHeight(); y++) {
        this.image.setRGB(x, y, gi.image.getRGB(x, y));
      }
    }
  }

  /**
   * Paints the GameImage, using the given Graphics, on the given Component at
   * the given x, y location.
   *
   * @param c
   * @param g
   * @param x
   * @param y
   */
  public void paint(final Component c, final Graphics g, final int x,
      final int y) {
    g.drawImage(this.image, x, y, c);
  }

  /**
   * Creates a square GameImage of a given size and color.
   *
   * @param size
   * @param color
   */
  public GameImage(final int size, final Color color) {
    this.image = new BufferedImageIcon(size, size);
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        this.image.setRGB(x, y, color.getRGB());
      }
    }
  }

  /**
   * @return the width of this GameImage, in pixels
   */
  public int getWidth() {
    return this.image.getWidth();
  }

  /**
   * @return the height of this GameImage, in pixels
   */
  public int getHeight() {
    return this.image.getHeight();
  }

  /**
   * @return the RGB color in this GameImage at the given x, y location.
   */
  public int getRGB(final int x, final int y) {
    return this.image.getRGB(x, y);
  }

  /**
   * Changes the RGB color in this GameImage at the given x, y location to the
   * provided RGB color.
   */
  public void setRGB(final int x, final int y, final int rgb) {
    this.image.setRGB(x, y, rgb);
  }
}
