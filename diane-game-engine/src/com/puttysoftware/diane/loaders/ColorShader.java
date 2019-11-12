/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.color.ColorSpace;
import java.util.Objects;

public class ColorShader {
  // Fields
  private final GameColor shadeColor;
  private final String shadeName;

  // Constructor
  public ColorShader(final String name, final GameColor shade) {
    this.shadeColor = new GameColor(ColorSpace.getInstance(ColorSpace.CS_sRGB),
        shade.getColorComponents(null), (float) 1.0);
    this.shadeName = name;
  }

  // Methods
  public String getName() {
    return this.shadeName;
  }

  public GameColor applyShade(final GameColor source) {
    if (source.getAlpha() != 255) {
      return source;
    }
    final ColorSpace linear = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
    final float[] inputColor = source.getColorComponents(linear);
    final float[] linearShade = this.shadeColor.getColorComponents(linear);
    final float[] outputColor = ColorShader.doColorMath(inputColor,
        linearShade);
    return ColorShader.convertFromLinearRGB(outputColor);
  }

  private static float[] doColorMath(final float[] inputColor,
      final float[] linearShade) {
    final float[] outputColor = new float[3];
    for (int c = 0; c < 3; c++) {
      outputColor[c] = inputColor[c] * (1 - linearShade[c]);
    }
    return outputColor;
  }

  private static GameColor convertFromLinearRGB(final float[] colorvalue) {
    final ColorSpace sourceSpace = ColorSpace
        .getInstance(ColorSpace.CS_LINEAR_RGB);
    final float[] colorvalueCIEXYZ = sourceSpace.toCIEXYZ(colorvalue);
    final ColorSpace targetSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
    final float[] colorvalueTarget = targetSpace.fromCIEXYZ(colorvalueCIEXYZ);
    return new GameColor(targetSpace, colorvalueTarget, (float) 1.0);
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
    return Objects.equals(this.shadeColor, other.shadeColor)
        && Objects.equals(this.shadeName, other.shadeName);
  }
}
