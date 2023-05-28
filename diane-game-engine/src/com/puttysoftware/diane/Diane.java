/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane;

import com.puttysoftware.diane.internal.DefaultErrorHandler;

public class Diane {
    private static ErrorHandler errHandler;

    public static void handleError(final Throwable t) {
	Diane.errHandler.uncaughtException(Thread.currentThread(), t);
    }

    public static void handleWarning(final Throwable t) {
	Diane.errHandler.handleWarning(t);
    }

    public static void installCustomErrorHandler(final ErrorHandler handler) {
	// Install custom error handler
	Diane.errHandler = handler;
	Thread.setDefaultUncaughtExceptionHandler(Diane.errHandler);
    }

    public static void installDefaultErrorHandler(final String programName) {
	// Install default error handler
	Diane.errHandler = new DefaultErrorHandler(programName);
	Thread.setDefaultUncaughtExceptionHandler(Diane.errHandler);
    }

    private Diane() {
    }
}
