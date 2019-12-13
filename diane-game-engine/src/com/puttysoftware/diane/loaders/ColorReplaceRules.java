/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Objects;

import com.puttysoftware.images.BufferedImageIcon;

public final class ColorReplaceRules {
  // Fields
  private final Hashtable<String, ColorReplaceRule> rules;
  private final String cacheName;

  // Constructor
  public ColorReplaceRules(final String name) {
    this.rules = new Hashtable<>();
    this.cacheName = name;
  }

  // Methods
  public void add(final String name, final Color find, final Color replace) {
    final ColorReplaceRule value = new ColorReplaceRule(find, replace);
    this.rules.put(name, value);
  }

  public BufferedImageIcon applyAll(final BufferedImageIcon input) {
    if (input == null) {
      throw new IllegalArgumentException("input == NULL!");
    }
    BufferedImageIcon result = input;
    for (ColorReplaceRule rule : this.rules.values()) {
      result = rule.apply(result);
    }
    return result;
  }

  public void clear() {
    this.rules.clear();
  }

  public String getName() {
    return this.cacheName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.cacheName, this.rules);
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
    return Objects.equals(this.cacheName, other.cacheName)
        && Objects.equals(this.rules, other.rules);
  }
}
