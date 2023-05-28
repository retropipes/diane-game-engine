/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.internal;

public final class ErrorLogger {
    // Fields
    private final String name;

    // Constructor
    public ErrorLogger(final String programName) {
	this.name = programName;
    }

    public void logError(final Throwable t) {
	final var elw = new ErrorLogWriter(t, this.name);
	elw.writeErrorInfo();
	System.exit(1);
    }

    public void logWarning(final Throwable t) {
	final var wlw = new WarningLogWriter(t, this.name);
	wlw.writeLogInfo();
    }
}
