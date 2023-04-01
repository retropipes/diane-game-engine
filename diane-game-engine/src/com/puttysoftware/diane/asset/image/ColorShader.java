/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.image;

import java.awt.Color;
import java.util.Objects;

public class ColorShader {
    private static float[] doColorMath(final float[] inputColor, final float[] inputShade) {
        final var outputColor = new float[4];
        for (var c = 0; c < 3; c++) {
            outputColor[c] = inputColor[c] * inputShade[c];
        }
        outputColor[3] = 1.0F;
        return outputColor;
    }

    // Fields
    private final Color shadeColor;
    private final String shadeName;

    // Constructor
    public ColorShader(final String name, final Color shade) {
        this.shadeColor = shade;
        this.shadeName = name;
    }

    public Color applyShade(final Color source) {
        if (source.getAlpha() != 255) {
            return source;
        }
        var sourceComp = new float[4];
        sourceComp = source.getColorComponents(sourceComp);
        var shadeComp = new float[4];
        shadeComp = this.shadeColor.getColorComponents(shadeComp);
        final var result = ColorShader.doColorMath(sourceComp, shadeComp);
        return new Color(result[0], result[1], result[2], result[3]);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof final ColorShader other)) {
            return false;
        }
        return Objects.equals(this.shadeColor, other.shadeColor) && Objects.equals(this.shadeName, other.shadeName);
    }

    public String getName() {
        return this.shadeName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.shadeColor, this.shadeName);
    }
}
