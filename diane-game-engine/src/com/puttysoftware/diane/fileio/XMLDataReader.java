/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLDataReader implements DataIOReader {
    // Fields
    private final InputStream inStream;
    private final XMLDecoder fileIO;
    private final File file;

    public XMLDataReader(final File filename) throws IOException {
        this.inStream = new FileInputStream(filename);
        this.fileIO = new XMLDecoder(this.inStream);
        this.file = filename;
    }

    public XMLDataReader(final InputStream stream) {
        this.inStream = stream;
        this.fileIO = new XMLDecoder(stream);
        this.file = null;
    }

    // Constructors
    public XMLDataReader(final String filename) throws IOException {
        this.inStream = new FileInputStream(filename);
        this.fileIO = new XMLDecoder(this.inStream);
        this.file = new File(filename);
    }

    @Override
    public boolean atEOF() throws DataIOException {
        try {
            this.fileIO.readObject();
            return false;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }

    @Override
    public void close() throws DataIOException {
        this.fileIO.close();
    }

    @Override
    public DataMode getDataIOMode() {
        return DataMode.XML;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public boolean readBoolean() throws DataIOException {
        return (boolean) this.fileIO.readObject();
    }

    @Override
    public byte readByte() throws DataIOException {
        return (byte) this.fileIO.readObject();
    }

    @Override
    public byte[] readBytes(final int len) throws DataIOException {
        try {
            final var buf = new byte[len];
            for (var b = 0; b < len; b++) {
                buf[b] = this.readByte();
            }
            return buf;
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    @Override
    public double readDouble() throws DataIOException {
        return (double) this.fileIO.readObject();
    }

    @Override
    public int readInt() throws DataIOException {
        return (int) this.fileIO.readObject();
    }

    @Override
    public long readLong() throws DataIOException {
        return (long) this.fileIO.readObject();
    }

    @Override
    public String readString() throws DataIOException {
        return (String) this.fileIO.readObject();
    }

    @Override
    public int readUnsignedByte() throws DataIOException {
        return this.readInt();
    }

    @Override
    public int readUnsignedShortByteArrayAsInt() throws DataIOException {
        try {
            final var buf = new byte[Short.BYTES];
            for (var b = 0; b < Short.BYTES; b++) {
                buf[b] = this.readByte();
            }
            return DataIOUtilities.unsignedShortByteArrayToInt(buf);
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    @Override
    public String readWindowsString(final byte[] buflen) throws DataIOException {
        try {
            final var buf = new byte[buflen.length];
            for (var b = 0; b < buflen.length; b++) {
                buf[b] = this.readByte();
            }
            return DataIOUtilities.decodeWindowsStringData(buf);
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }
}
