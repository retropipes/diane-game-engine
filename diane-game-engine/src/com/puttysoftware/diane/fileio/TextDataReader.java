/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextDataReader implements DataIOReader {
    // Fields
    private final BufferedReader fileIO;
    private final File file;

    public TextDataReader(final File filename) throws IOException {
        this.fileIO = new BufferedReader(new FileReader(filename));
        this.file = filename;
    }

    public TextDataReader(final InputStream stream) {
        this.fileIO = new BufferedReader(new InputStreamReader(stream));
        this.file = null;
    }

    // Constructors
    public TextDataReader(final String filename) throws IOException {
        this.fileIO = new BufferedReader(new FileReader(filename));
        this.file = new File(filename);
    }

    @Override
    public boolean atEOF() throws DataIOException {
        var line = "";
        try {
            line = this.fileIO.readLine();
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
        return line == null;
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
        return DataMode.TEXT;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public boolean readBoolean() throws DataIOException {
        try {
            return Boolean.parseBoolean(this.fileIO.readLine());
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    @Override
    public byte readByte() throws DataIOException {
        try {
            return Byte.parseByte(this.fileIO.readLine());
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
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
        try {
            return Double.parseDouble(this.fileIO.readLine());
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    @Override
    public int readInt() throws DataIOException {
        try {
            return Integer.parseInt(this.fileIO.readLine());
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    @Override
    public long readLong() throws DataIOException {
        try {
            return Long.parseLong(this.fileIO.readLine());
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    @Override
    public String readString() throws DataIOException {
        try {
            return this.fileIO.readLine();
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
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
