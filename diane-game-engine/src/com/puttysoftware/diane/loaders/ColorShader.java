/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.Color;
import java.util.Objects;

public class ColorShader {
  // Fields
  private final Color shadeColor;
  private final String shadeName;

  // Constructor
  public ColorShader(final String name, final Color shade) {
    this.shadeColor = shade;
    this.shadeName = name;
  }

  // Methods
  public String getName() {
    return this.shadeName;
  }

  public Color applyShade(final Color source) {
    if (source.getAlpha() != 255) {
      return source;
    }
    float[] sourceComp = new float[4];
    sourceComp = source.getColorComponents(sourceComp);
    float[] shadeComp = new float[4];
    shadeComp = this.shadeColor.getColorComponents(shadeComp);
    float[] result = ColorShader.doColorMath(sourceComp, shadeComp);
    return new Color(result[0], result[1], result[2], result[3]);
  }

  private static float[] doColorMath(final float[] inputColor, final float[] inputShade) {
    final float[] outputColor = new float[4];
    for (int c = 0; c < 3; c++) {
      outputColor[c] = inputColor[c] * inputShade[c];
    }
    outputColor[3] = 1.0F;
    return outputColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.shadeColor, this.shadeName);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ColorShader)) {
      return false;
    }
    final ColorShader other = (ColorShader) obj;
    return Objects.equals(this.shadeColor, other.shadeColor) && Objects.equals(this.shadeName, other.shadeName);
  }
}
