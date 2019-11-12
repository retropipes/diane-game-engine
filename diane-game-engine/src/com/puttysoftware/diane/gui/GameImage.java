package com.puttysoftware.diane.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.diane.internal.BufferedImageIcon;

public final class GameImage extends BufferedImageIcon {
  // Constructors
  /**
   * Creates a GameImage of a given size.
   *
   * @param width
   * @param height
   */
  public GameImage(final int width, final int height) {
    super(width, height);
  }

  /**
   * Creates a GameImage based on another GameImage object.
   *
   * @param gi
   */
  public GameImage(final GameImage gi) {
    super(gi.getWidth(), gi.getHeight());
    for (int x = 0; x < gi.getWidth(); x++) {
      for (int y = 0; y < gi.getHeight(); y++) {
        this.setRGB(x, y, gi.getRGB(x, y));
      }
    }
  }

  /**
   * Creates a GameImage based on a BufferedImage object.
   *
   * @param gi
   */
  private GameImage(final BufferedImage bi) {
    super(bi.getWidth(), bi.getHeight());
    for (int x = 0; x < bi.getWidth(); x++) {
      for (int y = 0; y < bi.getHeight(); y++) {
        this.setRGB(x, y, bi.getRGB(x, y));
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
    g.drawImage(this, x, y, c);
  }

  /**
   * Creates a square GameImage of a given size and color.
   *
   * @param size
   * @param color
   */
  public GameImage(final int size, final Color color) {
    super(size, size);
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        this.setRGB(x, y, color.getRGB());
      }
    }
  }

  public static GameImage read(final URL input) throws IOException {
    return new GameImage(ImageIO.read(input));
  }
}
