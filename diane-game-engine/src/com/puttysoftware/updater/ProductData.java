/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ProductData {
  // Fields
  private final URL updateURL;
  private final URL newVersionURL;
  private final String versionString;
  private final int majorVersion;
  private final int minorVersion;
  private final int bugfixVersion;
  private final int codeVersion;
  private final int prereleaseVersion;
  public static final int CODE_ALPHA = 1;
  public static final int CODE_BETA = 2;
  public static final int CODE_STABLE = 4;

  // Constructor
  public ProductData(final String update, final String newVersion,
      final int major, final int minor, final int bugfix, final int code,
      final int beta) throws MalformedURLException {
    String rt_url;
    String rt_v;
    if (code == ProductData.CODE_ALPHA) {
      if (beta > 0) {
        rt_v = "-alpha" + beta;
      } else {
        rt_v = "-alpha";
      }
      rt_url = "alpha_"; //$NON-NLS-1$
    } else if (code == ProductData.CODE_BETA) {
      if (beta > 0) {
        rt_v = "-beta" + beta;
      } else {
        rt_v = "-beta";
      }
      rt_url = "beta_"; //$NON-NLS-1$
    } else {
      rt_url = "stable_"; //$NON-NLS-1$
      rt_v = "";
    }
    this.versionString = major + "." + minor + "." + bugfix + rt_v;
    final String updatetxt = "version.txt"; //$NON-NLS-1$
    this.updateURL = new URL(update + rt_url + updatetxt);
    this.newVersionURL = new URL(newVersion);
    this.majorVersion = major;
    this.minorVersion = minor;
    this.bugfixVersion = bugfix;
    this.codeVersion = code;
    this.prereleaseVersion = beta;
  }

  // Methods
  /**
   * @return the updateURL
   */
  public URL getUpdateURL() {
    return this.updateURL;
  }

  /**
   * @return the newVersionURL
   */
  public URL getNewVersionURL() {
    return this.newVersionURL;
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
   * @return the bugfix version
   */
  public int getBugfixVersion() {
    return this.bugfixVersion;
  }

  /**
   * @return the code version
   */
  public int getCodeVersion() {
    return this.codeVersion;
  }

  /**
   * @return the prerelease version
   */
  public int getPrereleaseVersion() {
    return this.prereleaseVersion;
  }

  /**
   * @return the version as a string
   */
  public String getVersionString() {
    return this.versionString;
  }

  /**
   * Performs a check for updates.
   *
   * @return true if an update is available; false otherwise
   */
  public UpdateCheckResults checkForUpdates() throws IOException {
    int newMajor = this.majorVersion;
    int newMinor = this.minorVersion;
    int newBugfix = this.bugfixVersion;
    int newPrerelease = this.prereleaseVersion;
    try (
        InputStreamReader isr = new InputStreamReader(
            this.updateURL.openStream());
        BufferedReader br = new BufferedReader(isr)) {
      newMajor = Integer.parseInt(br.readLine());
      newMinor = Integer.parseInt(br.readLine());
      newBugfix = Integer.parseInt(br.readLine());
      newPrerelease = Integer.parseInt(br.readLine());
    }
    UpdateCheckResults hasUpdate = new UpdateCheckResults(newMajor, newMinor,
        newBugfix, newPrerelease);
    if (newMajor > this.majorVersion) {
      return hasUpdate;
    } else if (newMajor == this.majorVersion && newMinor > this.minorVersion) {
      return hasUpdate;
    } else if (newMajor == this.majorVersion && newMinor == this.minorVersion
        && newBugfix > this.bugfixVersion) {
      return hasUpdate;
    } else if (newMajor == this.majorVersion && newMinor == this.minorVersion
        && newBugfix == this.bugfixVersion
        && newPrerelease > this.prereleaseVersion) {
      return hasUpdate;
    }
    return new UpdateCheckResults();
  }
}
