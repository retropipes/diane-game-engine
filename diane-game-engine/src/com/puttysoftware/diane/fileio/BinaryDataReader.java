/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryDataReader implements DataIOReader {
    // Fields
    private final DataInputStream fileIO;
    private final File file;

    public BinaryDataReader(final File filename) throws IOException {
	this.fileIO = new DataInputStream(new FileInputStream(filename));
	this.file = filename;
    }

    public BinaryDataReader(final InputStream stream) {
	this.fileIO = new DataInputStream(stream);
	this.file = null;
    }

    // Constructors
    public BinaryDataReader(final String filename) throws IOException {
	this.fileIO = new DataInputStream(new FileInputStream(filename));
	this.file = new File(filename);
    }

    @Override
    public boolean atEOF() throws DataIOException {
	try {
	    final var buf = new byte[1];
	    return this.fileIO.read(buf) == -1;
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public void close() throws DataIOException {
	try {
	    this.fileIO.close();
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public DataMode getDataIOMode() {
	return DataMode.BINARY;
    }

    @Override
    public File getFile() {
	return this.file;
    }

    @Override
    public boolean readBoolean() throws DataIOException {
	try {
	    return this.fileIO.readBoolean();
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public byte readByte() throws DataIOException {
	try {
	    return this.fileIO.readByte();
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public byte[] readBytes(final int len) throws DataIOException {
	try {
	    final var buf = new byte[len];
	    this.fileIO.read(buf);
	    return buf;
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public double readDouble() throws DataIOException {
	try {
	    return this.fileIO.readDouble();
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public int readInt() throws DataIOException {
	try {
	    return this.fileIO.readInt();
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public long readLong() throws DataIOException {
	try {
	    return this.fileIO.readLong();
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public String readString() throws DataIOException {
	try {
	    return this.fileIO.readUTF();
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public int readUnsignedByte() throws DataIOException {
	try {
	    return this.fileIO.readUnsignedByte();
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public int readUnsignedShortByteArrayAsInt() throws DataIOException {
	try {
	    final var buf = new byte[Short.BYTES];
	    this.fileIO.read(buf);
	    return DataIOUtilities.unsignedShortByteArrayToInt(buf);
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public String readWindowsString(final byte[] buflen) throws DataIOException {
	try {
	    this.fileIO.read(buflen);
	    return DataIOUtilities.decodeWindowsStringData(buflen);
	} catch (final IOException e) {
	    throw new DataIOException(e);
	}
    }
}
