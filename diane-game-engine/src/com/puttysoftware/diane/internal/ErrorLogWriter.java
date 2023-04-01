/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.internal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class ErrorLogWriter {
    // Fields
    private static final String MAC_PREFIX = "HOME"; //$NON-NLS-1$
    private static final String WIN_PREFIX = "LOCALAPPDATA"; //$NON-NLS-1$
    private static final String UNIX_PREFIX = "HOME"; //$NON-NLS-1$
    private static final String MAC_DIR = "/Library/Logs/CrashReporter/"; //$NON-NLS-1$
    private static final String WIN_DIR = "\\PuttySoftware\\Logs\\"; //$NON-NLS-1$
    private static final String UNIX_DIR = "/.config/puttysoftware/logs/"; //$NON-NLS-1$
    private static final String MAC_EXT = ".error.log"; //$NON-NLS-1$
    private static final String WIN_EXT = ".error.log"; //$NON-NLS-1$
    private static final String UNIX_EXT = ".error.log"; //$NON-NLS-1$

    private static String getErrorDirectory() {
        final var osName = System.getProperty("os.name"); //$NON-NLS-1$
        if (osName.indexOf("Mac OS X") != -1) { //$NON-NLS-1$
            // Mac OS X
            return ErrorLogWriter.MAC_DIR;
        } else if (osName.indexOf("Windows") != -1) { //$NON-NLS-1$
            // Windows
            return ErrorLogWriter.WIN_DIR;
        } else {
            // Other - assume UNIX-like
            return ErrorLogWriter.UNIX_DIR;
        }
    }

    private static String getErrorDirPrefix() {
        final var osName = System.getProperty("os.name"); //$NON-NLS-1$
        if (osName.indexOf("Mac OS X") != -1) { //$NON-NLS-1$
            // Mac OS X
            return System.getenv(ErrorLogWriter.MAC_PREFIX);
        } else if (osName.indexOf("Windows") != -1) { //$NON-NLS-1$
            // Windows
            return System.getenv(ErrorLogWriter.WIN_PREFIX);
        } else {
            // Other - assume UNIX-like
            return System.getenv(ErrorLogWriter.UNIX_PREFIX);
        }
    }

    private static String getErrorFileExtension() {
        final var osName = System.getProperty("os.name"); //$NON-NLS-1$
        if (osName.indexOf("Mac OS X") != -1) { //$NON-NLS-1$
            // Mac OS X
            return ErrorLogWriter.MAC_EXT;
        } else if (osName.indexOf("Windows") != -1) { //$NON-NLS-1$
            // Windows
            return ErrorLogWriter.WIN_EXT;
        } else {
            // Other - assume UNIX-like
            return ErrorLogWriter.UNIX_EXT;
        }
    }

    private final Throwable t;
    private final Calendar c;
    private final String p;

    // Constructors
    ErrorLogWriter(final Throwable problem, final String programName) {
        this.t = problem;
        this.c = Calendar.getInstance();
        this.p = programName;
    }

    private File getErrorFile() {
        final var b = new StringBuilder();
        b.append(ErrorLogWriter.getErrorDirPrefix());
        b.append(ErrorLogWriter.getErrorDirectory());
        b.append(this.getErrorFileName());
        b.append(this.getStampSuffix());
        b.append(ErrorLogWriter.getErrorFileExtension());
        return new File(b.toString());
    }

    private String getErrorFileName() {
        return this.p;
    }

    private String getStampSuffix() {
        final var time = this.c.getTime();
        final var sdf = new SimpleDateFormat("'_'yyyyMMdd'_'HHmmssSSS"); //$NON-NLS-1$
        return sdf.format(time);
    }

    void writeErrorInfo() {
        try {
            // Make sure the needed directories exist first
            final var df = this.getErrorFile();
            final var parent = new File(df.getParent());
            if (!parent.exists()) {
                final var res = parent.mkdirs();
                if (!res) {
                    throw new FileNotFoundException("Cannot make directories!"); //$NON-NLS-1$
                }
            }
            // Print to the file
            try (var s = new PrintStream(new BufferedOutputStream(new FileOutputStream(df)))) {
                this.t.printStackTrace(s);
                s.close();
            }
        } catch (final FileNotFoundException fnf) {
            // Print to standard error, if something went wrong
            this.t.printStackTrace(System.err);
        }
    }
}
