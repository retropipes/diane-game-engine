/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

import com.puttysoftware.images.BufferedImageIcon;

public final class ColorReplaceRules {
  // Fields
  private final ArrayList<ColorReplaceRule> rules;

  // Constructor
  public ColorReplaceRules() {
    this.rules = new ArrayList<>();
  }

  // Methods
  public void add(final Color find, final Color replace) {
    final ColorReplaceRule value = new ColorReplaceRule(find, replace);
    this.rules.add(value);
  }

  public BufferedImageIcon applyAll(final BufferedImageIcon input) {
    if (input == null) {
      throw new IllegalArgumentException("input == NULL!");
    }
    BufferedImageIcon result = input;
    for (ColorReplaceRule rule : this.rules) {
      result = rule.apply(result);
    }
    return result;
  }

  public void clear() {
    this.rules.clear();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.rules);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ColorReplaceRules)) {
      return false;
    }
    ColorReplaceRules other = (ColorReplaceRules) obj;
    return Objects.equals(this.rules, other.rules);
  }
}
