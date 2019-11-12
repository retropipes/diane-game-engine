package com.puttysoftware.errorlogger;

public final class ErrorLogger {
  // Fields
  private final String name;

  // Constructor
  public ErrorLogger(final String programName) {
    this.name = programName.replaceAll(" ", "");
  }

  // Methods
  public void logError(final Throwable t) {
    final ErrorWriter ew = new ErrorWriter(t, this.name);
    ew.writeErrorInfo();
    System.exit(1);
  }

  public void logWarning(final RuntimeException t) {
    final WarningWriter ww = new WarningWriter(t, this.name);
    ww.writeWarningInfo();
  }
}
