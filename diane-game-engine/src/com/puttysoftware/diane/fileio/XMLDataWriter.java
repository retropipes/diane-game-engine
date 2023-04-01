/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XMLDataWriter implements DataIOWriter {
    // Fields
    private final OutputStream outStream;
    private final XMLEncoder fileIO;
    private final File file;

    public XMLDataWriter(final File filename) throws IOException {
        this.outStream = new FileOutputStream(filename);
        this.fileIO = new XMLEncoder(this.outStream);
        this.file = filename;
    }

    public XMLDataWriter(final OutputStream stream) {
        this.outStream = stream;
        this.fileIO = new XMLEncoder(stream);
        this.file = null;
    }

    // Constructors
    public XMLDataWriter(final String filename) throws IOException {
        this.outStream = new FileOutputStream(filename);
        this.fileIO = new XMLEncoder(this.outStream);
        this.file = new File(filename);
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
    public void writeBoolean(final boolean b) throws DataIOException {
        this.fileIO.writeObject(b);
    }

    @Override
    public void writeByte(final byte b) throws DataIOException {
        this.fileIO.writeObject(b);
    }

    @Override
    public void writeDouble(final double d) throws DataIOException {
        this.fileIO.writeObject(d);
    }

    @Override
    public void writeInt(final int i) throws DataIOException {
        this.fileIO.writeObject(i);
    }

    @Override
    public void writeLong(final long l) throws DataIOException {
        this.fileIO.writeObject(l);
    }

    @Override
    public void writeString(final String s) throws DataIOException {
        this.fileIO.writeObject(s);
    }

    @Override
    public void writeUnsignedByte(final int b) throws DataIOException {
        this.writeInt(b);
    }
}
