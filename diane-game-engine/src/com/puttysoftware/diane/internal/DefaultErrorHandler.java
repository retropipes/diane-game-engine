package com.puttysoftware.diane.internal;

import com.puttysoftware.diane.ErrorHandler;

public class DefaultErrorHandler implements ErrorHandler {
    private final String programName;
    private final ErrorLogger logger;

    public DefaultErrorHandler(final String name) {
	this.programName = name;
	this.logger = new ErrorLogger(this.programName);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
	this.logger.logError(e);
    }
}
