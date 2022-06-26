/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.fileio;

import java.io.IOException;

public class DataIOException extends IOException {
    private static final long serialVersionUID = 23250505322336L;

    public DataIOException() {
	super();
    }

    public DataIOException(String message, Throwable cause) {
	super(message, cause);
    }

    public DataIOException(String message) {
	super(message);
    }

    public DataIOException(Throwable cause) {
	super(cause);
    }
}
