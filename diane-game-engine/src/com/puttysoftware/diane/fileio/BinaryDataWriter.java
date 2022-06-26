package com.puttysoftware.diane.fileio;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryDataWriter implements DataIOWriter {
    // Fields
    private DataOutputStream fileIO;
    private final File file;

    // Constructors
    public BinaryDataWriter(final String filename) throws IOException {
	this.fileIO = new DataOutputStream(new FileOutputStream(filename));
	this.file = new File(filename);
    }

    public BinaryDataWriter(final File filename) throws IOException {
	this.fileIO = new DataOutputStream(new FileOutputStream(filename));
	this.file = filename;
    }

    public BinaryDataWriter(final OutputStream stream) throws DataIOException {
	this.fileIO = new DataOutputStream(stream);
	this.file = null;
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.BINARY;
    }

    @Override
    public File getFile() {
	return this.file;
    }

    @Override
    public void close() throws DataIOException {
	try {
	    this.fileIO.close();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public void writeInt(final int i) throws DataIOException {
	try {
	    this.fileIO.writeInt(i);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public void writeDouble(final double d) throws DataIOException {
	try {
	    this.fileIO.writeDouble(d);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public void writeLong(final long l) throws DataIOException {
	try {
	    this.fileIO.writeLong(l);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public void writeByte(final byte b) throws DataIOException {
	try {
	    this.fileIO.writeByte(b);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public void writeBoolean(final boolean b) throws DataIOException {
	try {
	    this.fileIO.writeBoolean(b);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public void writeString(final String s) throws DataIOException {
	try {
	    this.fileIO.writeUTF(s);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public void writeUnsignedByte(final int b) throws DataIOException {
	this.writeInt(b);
    }
}
