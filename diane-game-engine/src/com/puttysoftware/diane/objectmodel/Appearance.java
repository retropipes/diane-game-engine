/*  Fantastle Reboot
 * A maze-solving RPG
 * This code is licensed under the terms of the
 * GPLv3, or at your option, any later version.
 */
package com.puttysoftware.diane.objectmodel;

import com.puttysoftware.diane.assets.ImageIndex;
import com.puttysoftware.diane.gui.GameImage;
import com.puttysoftware.diane.loaders.ColorShader;

public abstract class Appearance {
  private final String cacheName;
  private final ImageIndex whichImage;
  private final ColorShader shading;

  public Appearance(final String name, final ImageIndex imageIndex) {
    this.cacheName = name;
    this.whichImage = imageIndex;
    this.shading = null;
  }

  public Appearance(final String name, final ImageIndex imageIndex,
      final ColorShader shader) {
    this.cacheName = name;
    this.whichImage = imageIndex;
    this.shading = shader;
  }

  public final String getCacheName() {
    return this.cacheName;
  }

  protected final ImageIndex getWhichImage() {
    return this.whichImage;
  }

  public final boolean hasShading() {
    return this.shading != null;
  }

  public final ColorShader getShading() {
    return this.shading;
  }

  public abstract GameImage getImage();
}
