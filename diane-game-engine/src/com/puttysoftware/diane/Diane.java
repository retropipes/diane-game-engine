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
