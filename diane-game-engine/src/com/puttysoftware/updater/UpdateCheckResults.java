/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.updater;

public final class UpdateCheckResults {
  // Fields
  private final int majorVersion;
  private final int minorVersion;
  private final int bugfixVersion;
  private final int prereleaseVersion;
  private final boolean hasUpdate;

  // Constructors
  public UpdateCheckResults() {
    this.hasUpdate = false;
    this.majorVersion = -1;
    this.minorVersion = -1;
    this.bugfixVersion = -1;
    this.prereleaseVersion = -1;
  }

  public UpdateCheckResults(final int major, final int minor, final int bugfix,
      final int beta) {
    this.hasUpdate = true;
    this.majorVersion = major;
    this.minorVersion = minor;
    this.bugfixVersion = bugfix;
    this.prereleaseVersion = beta;
  }

  public UpdateCheckResults(final boolean update, final int major,
      final int minor, final int bugfix, final int beta) {
    this.hasUpdate = update;
    this.majorVersion = major;
    this.minorVersion = minor;
    this.bugfixVersion = bugfix;
    this.prereleaseVersion = beta;
  }

  // Methods
  /**
   * 
   * @return the has update status
   */
  public boolean hasUpdate() {
    return this.hasUpdate;
  }

  /**
   * @return the major version
   */
  public int getMajorVersion() {
    return this.majorVersion;
  }

  /**
   * @return the minor version
   */
  public int getMinorVersion() {
    return this.minorVersion;
  }

  /**
   * @return the bug fix version
   */
  public int getBugfixVersion() {
    return this.bugfixVersion;
  }

  /**
   * @return the prerelease version
   */
  public int getPrereleaseVersion() {
    return this.prereleaseVersion;
  }
}
