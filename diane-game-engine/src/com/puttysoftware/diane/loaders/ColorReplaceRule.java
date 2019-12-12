/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.Objects;

public final class ColorReplaceRule extends ColorShader {
  // Fields
  private final Color findColor;

  // Constructor
  public ColorReplaceRule(final String name, final Color find,
      final Color replace) {
    super(name, new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB),
        replace.getColorComponents(null), (float) 1.0));
    this.findColor = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB),
        find.getColorComponents(null), (float) 1.0);
  }

  // Methods
  @Override
  public Color applyShade(final Color source) {
    if (!source.equals(this.findColor) || source.getAlpha() != 255) {
      return source;
    }
    final ColorSpace linear = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
    final float[] inputColor = source.getColorComponents(linear, null);
    final float[] linearShade = this.getShadeColor().getColorComponents(linear,
        null);
    final float[] outputColor = ColorReplaceRule.doColorMath(inputColor,
        linearShade);
    return ColorReplaceRule.convertFromLinearRGB(outputColor);
  }

  private static float[] doColorMath(final float[] inputColor,
      final float[] inputShade) {
    final float[] outputColor = new float[4];
    for (int c = 0; c < 3; c++) {
      outputColor[c] = inputColor[c] * inputShade[c];
    }
    outputColor[3] = 1.0F;
    return outputColor;
  }

  private static Color convertFromLinearRGB(final float[] colorvalue) {
    final ColorSpace sourceSpace = ColorSpace
        .getInstance(ColorSpace.CS_LINEAR_RGB);
    final float[] colorvalueCIEXYZ = sourceSpace.toCIEXYZ(colorvalue);
    final ColorSpace targetSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
    final float[] colorvalueTarget = targetSpace.fromCIEXYZ(colorvalueCIEXYZ);
    return new Color(targetSpace, colorvalueTarget, (float) 1.0);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.findColor);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof ColorReplaceRule)) {
      return false;
    }
    ColorReplaceRule other = (ColorReplaceRule) obj;
    return Objects.equals(this.findColor, other.findColor);
  }
}
