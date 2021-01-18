/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.objectmodel;

import com.puttysoftware.diane.assets.ImageIndex;
import com.puttysoftware.diane.loaders.ColorReplaceRules;
import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.images.BufferedImageIcon;

public abstract class Appearance {
  private final String cacheName;
  private final ImageIndex whichImage;
  private final ColorShader shading;
  private final ColorReplaceRules replacements;

  public Appearance(final String name, final ImageIndex imageIndex) {
    this.cacheName = name;
    this.whichImage = imageIndex;
    this.shading = null;
    this.replacements = null;
  }

  public Appearance(final String name, final ImageIndex imageIndex, final ColorShader shader) {
    this.cacheName = name;
    this.whichImage = imageIndex;
    this.shading = shader;
    this.replacements = null;
  }

  public Appearance(final String name, final ImageIndex imageIndex, final ColorReplaceRules replaceRules) {
    this.cacheName = name;
    this.whichImage = imageIndex;
    this.shading = null;
    this.replacements = replaceRules;
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

  public final boolean hasReplacementRules() {
    return this.replacements != null;
  }

  public final ColorReplaceRules getReplacementRules() {
    return this.replacements;
  }

  public abstract BufferedImageIcon getImage();
}
