/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane;

public interface ErrorHandler extends Thread.UncaughtExceptionHandler {
    void handleWarning(Throwable t);
}
