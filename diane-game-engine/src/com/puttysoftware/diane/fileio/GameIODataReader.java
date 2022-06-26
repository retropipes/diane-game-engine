package com.puttysoftware.diane.fileio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GameIODataReader implements DataIOReader {
    // Fields
    private final RandomAccessFile fileIO;
    private final File file;

    // Constructors
    public GameIODataReader(final String filename) throws IOException {
	this.fileIO = new RandomAccessFile(filename, "r");
	this.file = new File(filename);
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.GAME_IO;
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
    public boolean readBoolean() throws DataIOException {
	try {
	    return this.fileIO.readBoolean();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public byte readByte() throws DataIOException {
	try {
	    return this.fileIO.readByte();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public byte[] readBytes(final int len) throws DataIOException {
	try {
	    final byte[] buf = new byte[len];
	    this.fileIO.read(buf);
	    return buf;
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public double readDouble() throws DataIOException {
	try {
	    return this.fileIO.readDouble();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public int readInt() throws DataIOException {
	try {
	    return this.fileIO.readInt();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public long readLong() throws DataIOException {
	try {
	    return this.fileIO.readLong();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public String readString() throws DataIOException {
	try {
	    return this.fileIO.readUTF();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public int readUnsignedByte() throws DataIOException {
	try {
	    return this.fileIO.readUnsignedByte();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public int readUnsignedShortByteArrayAsInt() throws DataIOException {
	try {
	    final byte[] buf = new byte[Short.BYTES];
	    this.fileIO.read(buf);
	    return DataIOUtilities.unsignedShortByteArrayToInt(buf);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public String readWindowsString(final byte[] buflen) throws DataIOException {
	try {
	    this.fileIO.read(buflen);
	    return DataIOUtilities.decodeWindowsStringData(buflen);
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }

    @Override
    public boolean atEOF() throws DataIOException {
	try {
	    return this.fileIO.getFilePointer() == this.fileIO.length();
	} catch (IOException e) {
	    throw new DataIOException(e);
	}
    }
}
