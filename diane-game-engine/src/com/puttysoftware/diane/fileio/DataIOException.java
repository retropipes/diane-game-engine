/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.io.IOException;

public class DataIOException extends IOException {
    private static final long serialVersionUID = 23250505322336L;

    public DataIOException() {
    }

    public DataIOException(final String message) {
        super(message);
    }

    public DataIOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DataIOException(final Throwable cause) {
        super(cause);
    }
}
