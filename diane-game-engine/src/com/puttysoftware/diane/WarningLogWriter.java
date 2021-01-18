/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class WarningLogWriter {
  // Fields
  private static final String MAC_PREFIX = "HOME"; //$NON-NLS-1$
  private static final String WIN_PREFIX = "LOCALAPPDATA"; //$NON-NLS-1$
  private static final String UNIX_PREFIX = "HOME"; //$NON-NLS-1$
  private static final String MAC_DIR = "/Library/Logs/CrashReporter/"; //$NON-NLS-1$
  private static final String WIN_DIR = "\\PuttySoftware\\Logs\\"; //$NON-NLS-1$
  private static final String UNIX_DIR = "/.config/puttysoftware/logs/"; //$NON-NLS-1$
  private static final String MAC_EXT = ".warning.log"; //$NON-NLS-1$
  private static final String WIN_EXT = ".warning.log"; //$NON-NLS-1$
  private static final String UNIX_EXT = ".warning.log"; //$NON-NLS-1$
  private final Throwable t;
  private final Calendar c;
  private final String p;

  // Constructors
  WarningLogWriter(final Throwable problem, final String programName) {
    this.t = problem;
    this.c = Calendar.getInstance();
    this.p = programName;
  }

  // Methods
  void writeLogInfo() {
    try {
      // Make sure the needed directories exist first
      final File df = this.getLogFile();
      final File parent = new File(df.getParent());
      if (!parent.exists()) {
        final boolean res = parent.mkdirs();
        if (!res) {
          throw new FileNotFoundException("Cannot make directories!"); //$NON-NLS-1$
        }
      }
      // Print to the file
      try (PrintStream s = new PrintStream(new BufferedOutputStream(new FileOutputStream(df)))) {
        this.t.printStackTrace(s);
        s.close();
      }
    } catch (final FileNotFoundException fnf) {
      // Print to standard error, if something went wrong
      this.t.printStackTrace(System.err);
    }
  }

  private static String getLogDirPrefix() {
    final String osName = System.getProperty("os.name"); //$NON-NLS-1$
    if (osName.indexOf("Mac OS X") != -1) { //$NON-NLS-1$
      // Mac OS X
      return System.getenv(WarningLogWriter.MAC_PREFIX);
    } else if (osName.indexOf("Windows") != -1) { //$NON-NLS-1$
      // Windows
      return System.getenv(WarningLogWriter.WIN_PREFIX);
    } else {
      // Other - assume UNIX-like
      return System.getenv(WarningLogWriter.UNIX_PREFIX);
    }
  }

  private static String getLogDirectory() {
    final String osName = System.getProperty("os.name"); //$NON-NLS-1$
    if (osName.indexOf("Mac OS X") != -1) { //$NON-NLS-1$
      // Mac OS X
      return WarningLogWriter.MAC_DIR;
    } else if (osName.indexOf("Windows") != -1) { //$NON-NLS-1$
      // Windows
      return WarningLogWriter.WIN_DIR;
    } else {
      // Other - assume UNIX-like
      return WarningLogWriter.UNIX_DIR;
    }
  }

  private static String getLogFileExtension() {
    final String osName = System.getProperty("os.name"); //$NON-NLS-1$
    if (osName.indexOf("Mac OS X") != -1) { //$NON-NLS-1$
      // Mac OS X
      return WarningLogWriter.MAC_EXT;
    } else if (osName.indexOf("Windows") != -1) { //$NON-NLS-1$
      // Windows
      return WarningLogWriter.WIN_EXT;
    } else {
      // Other - assume UNIX-like
      return WarningLogWriter.UNIX_EXT;
    }
  }

  private String getStampSuffix() {
    final Date time = this.c.getTime();
    final SimpleDateFormat sdf = new SimpleDateFormat("'_'yyyyMMdd'_'HHmmssSSS"); //$NON-NLS-1$
    return sdf.format(time);
  }

  private String getLogFileName() {
    return this.p;
  }

  private File getLogFile() {
    final StringBuilder b = new StringBuilder();
    b.append(WarningLogWriter.getLogDirPrefix());
    b.append(WarningLogWriter.getLogDirectory());
    b.append(this.getLogFileName());
    b.append(this.getStampSuffix());
    b.append(WarningLogWriter.getLogFileExtension());
    return new File(b.toString());
  }
}
