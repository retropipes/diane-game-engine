/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio.utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipUtilities {
    public static void unzipDirectory(final File zip, final File extractTo) throws IOException {
	try (var archive = new ZipFile(zip)) {
	    final Enumeration<? extends ZipEntry> e = archive.entries();
	    while (e.hasMoreElements()) {
		final ZipEntry entry = e.nextElement();
		final var file = new File(extractTo, entry.getName());
		if (entry.isDirectory() && !file.exists()) {
		    final var res = file.mkdirs();
		    if (!res) {
			throw new IOException("Couldn't make folders!"); //$NON-NLS-1$
		    }
		} else {
		    if (!file.getParentFile().exists()) {
			final var res = file.getParentFile().mkdirs();
			if (!res) {
			    throw new IOException("Couldn't make folders!"); //$NON-NLS-1$
			}
		    }
		    try (var in = archive.getInputStream(entry);
			    var out = new BufferedOutputStream(new FileOutputStream(file))) {
			final var buffer = new byte[8192];
			int read;
			while (-1 != (read = in.read(buffer))) {
			    out.write(buffer, 0, read);
			}
		    } catch (final IOException ioe) {
			throw ioe;
		    }
		}
	    }
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    private static void zip(final File directory, final File base, final ZipOutputStream zos) throws IOException {
	final var files = directory.listFiles();
	final var buffer = new byte[8192];
	var read = 0;
	final var n = files.length;
	for (var i = 0; i < n; i++) {
	    if (files[i].isDirectory()) {
		ZipUtilities.zip(files[i], base, zos);
	    } else {
		try (var in = new FileInputStream(files[i])) {
		    final var entry = new ZipEntry(files[i].getPath().substring(base.getPath().length() + 1));
		    zos.putNextEntry(entry);
		    while (-1 != (read = in.read(buffer))) {
			zos.write(buffer, 0, read);
		    }
		} catch (final IOException ioe) {
		    throw ioe;
		}
	    }
	}
    }

    public static void zipDirectory(final File directory, final File zip) throws IOException {
	try (var zos = new ZipOutputStream(new FileOutputStream(zip))) {
	    ZipUtilities.zip(directory, directory, zos);
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    private ZipUtilities() {
	// Do nothing
    }
}
