/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.io.File;

public interface DataIOWriter extends AutoCloseable {
    @Override
    void close() throws DataIOException;

    // Methods
    DataMode getDataIOMode();

    File getFile();

    void writeBoolean(boolean value) throws DataIOException;

    void writeByte(byte value) throws DataIOException;

    void writeDouble(double value) throws DataIOException;

    void writeInt(int value) throws DataIOException;

    void writeLong(long value) throws DataIOException;

    void writeString(String value) throws DataIOException;

    void writeUnsignedByte(int value) throws DataIOException;
}