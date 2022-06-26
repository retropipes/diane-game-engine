package com.puttysoftware.diane.fileio;

import java.io.File;

public interface DataIOWriter extends AutoCloseable {
    // Methods
    DataMode getDataIOMode();

    File getFile();

    @Override
    void close() throws DataIOException;

    void writeBoolean(boolean value) throws DataIOException;

    void writeByte(byte value) throws DataIOException;

    void writeDouble(double value) throws DataIOException;

    void writeInt(int value) throws DataIOException;

    void writeLong(long value) throws DataIOException;

    void writeString(String value) throws DataIOException;

    void writeUnsignedByte(int value) throws DataIOException;
}