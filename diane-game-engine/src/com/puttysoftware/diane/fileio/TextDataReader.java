package com.puttysoftware.diane.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextDataReader implements DataIOReader {
    // Fields
    private BufferedReader fileIO;
    private final File file;

    // Constructors
    public TextDataReader(final String filename) throws IOException {
	this.fileIO = new BufferedReader(new FileReader(filename));
	this.file = new File(filename);
    }

    public TextDataReader(final File filename) throws IOException {
	this.fileIO = new BufferedReader(new FileReader(filename));
	this.file = filename;
    }

    public TextDataReader(final InputStream stream) throws IOException {
	this.fileIO = new BufferedReader(new InputStreamReader(stream));
	this.file = null;
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.TEXT;
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
    public int readInt() throws DataIOException {
	try {
	    return Integer.parseInt(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public double readDouble() throws DataIOException {
	try {
	    return Double.parseDouble(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public long readLong() throws DataIOException {
	try {
	    return Long.parseLong(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public byte readByte() throws DataIOException {
	try {
	    return Byte.parseByte(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public boolean readBoolean() throws DataIOException {
	try {
	    return Boolean.parseBoolean(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public String readString() throws DataIOException {
	try {
	    return this.fileIO.readLine();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public byte[] readBytes(final int len) throws DataIOException {
	try {
	    final byte[] buf = new byte[len];
	    for (int b = 0; b < len; b++) {
		buf[b] = readByte();
	    }
	    return buf;
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public int readUnsignedByte() throws DataIOException {
	return readInt();
    }

    @Override
    public int readUnsignedShortByteArrayAsInt() throws DataIOException {
	try {
	    final byte[] buf = new byte[Short.BYTES];
	    for (int b = 0; b < Short.BYTES; b++) {
		buf[b] = readByte();
	    }
	    return DataIOUtilities.unsignedShortByteArrayToInt(buf);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public String readWindowsString(final byte[] buflen) throws DataIOException {
	try {
	    final byte[] buf = new byte[buflen.length];
	    for (int b = 0; b < buflen.length; b++) {
		buf[b] = readByte();
	    }
	    return DataIOUtilities.decodeWindowsStringData(buf);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public boolean atEOF() throws DataIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
	return line == null;
    }
}
