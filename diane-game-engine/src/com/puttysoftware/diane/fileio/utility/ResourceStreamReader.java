/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ResourceStreamReader implements AutoCloseable {
    // Fields
    private transient final BufferedReader br;

    // Constructors
    public ResourceStreamReader(final InputStream is) {
	this.br = new BufferedReader(new InputStreamReader(is));
    }

    public ResourceStreamReader(final InputStream is, final String encoding) throws UnsupportedEncodingException {
	this.br = new BufferedReader(new InputStreamReader(is, encoding));
    }

    @Override
    public void close() throws IOException {
	this.br.close();
    }

    public int readInt() throws IOException {
	final var line = this.br.readLine();
	if (line == null) {
	    throw new IOException("Input line == null!"); //$NON-NLS-1$
	}
	return Integer.parseInt(line);
    }

    public String readString() throws IOException {
	return this.br.readLine();
    }
}
