/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.objectmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

final class CustomFlags {
  // Fields
  private final ArrayList<Boolean> flags;

  // Constructor
  public CustomFlags() {
    this.flags = new ArrayList<>();
  }

  // Methods
  public int length() {
    return this.flags.size();
  }

  public boolean add(final int count) {
    if (this.flags.size() != 0) {
      return false;
    }
    this.flags.addAll(Collections.nCopies(count, false));
    return true;
  }

  public void addOne() {
    if (this.flags.size() == 0) {
      this.flags.add(false);
    }
  }

  public void append(final int count) {
    this.flags.addAll(Collections.nCopies(count, false));
  }

  public void appendOne() {
    this.flags.add(false);
  }

  public boolean get(final int index) {
    return this.flags.get(index);
  }

  public boolean toggle(final int index) {
    if (this.flags.size() <= index) {
      return false;
    }
    this.flags.set(index, !this.flags.get(index));
    return true;
  }

  public boolean set(final int index, final boolean value) {
    if (this.flags.size() <= index) {
      return false;
    }
    this.flags.set(index, value);
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.flags);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CustomFlags)) {
      return false;
    }
    final CustomFlags other = (CustomFlags) obj;
    return Objects.equals(this.flags, other.flags);
  }
}
