/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane;

public class Diane {
  private Diane() {
    super();
  }

  private static ErrorHandler errHandler;

  public static void installErrorHandler(final ErrorHandler handler) {
    // Install error handler
    Diane.errHandler = handler;
    Thread.setDefaultUncaughtExceptionHandler(Diane.errHandler);
  }

  public static void handleError(final Throwable t) {
    Diane.errHandler.uncaughtException(Thread.currentThread(), t);
  }
}
