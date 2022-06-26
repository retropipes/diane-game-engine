package com.puttysoftware.diane.fileio;

import java.io.File;

public interface DataIOReader extends AutoCloseable {
    // Methods
    DataMode getDataIOMode();

    File getFile();

    @Override
    void close() throws DataIOException;

    boolean readBoolean() throws DataIOException;

    byte readByte() throws DataIOException;

    byte[] readBytes(int len) throws DataIOException;

    double readDouble() throws DataIOException;

    int readInt() throws DataIOException;

    long readLong() throws DataIOException;

    String readString() throws DataIOException;

    int readUnsignedByte() throws DataIOException;

    int readUnsignedShortByteArrayAsInt() throws DataIOException;

    String readWindowsString(byte[] buflen) throws DataIOException;

    boolean atEOF() throws DataIOException;
}