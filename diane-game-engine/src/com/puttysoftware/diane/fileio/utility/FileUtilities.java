/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileUtilities {
    public static void copyFile(final File sourceLocation, final File targetLocation) throws IOException {
	try (InputStream in = new FileInputStream(sourceLocation);
		OutputStream out = new FileOutputStream(targetLocation)) {
	    // Copy the bits from instream to outstream
	    final var buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    public static void copyRAMFile(final InputStream in, final File targetLocation) throws IOException {
	try (OutputStream out = new FileOutputStream(targetLocation)) {
	    // Copy the bits from instream to outstream
	    final var buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	    in.close();
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    public static boolean moveFile(final File sourceLocation, final File targetLocation) throws IOException {
	try (InputStream in = new FileInputStream(sourceLocation);
		OutputStream out = new FileOutputStream(targetLocation)) {
	    // Copy the bits from instream to outstream
	    final var buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	    return sourceLocation.delete();
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    private FileUtilities() {
	// Do nothing
    }
}
