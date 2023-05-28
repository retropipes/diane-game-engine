/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter implements AutoCloseable {
    private static final String END_OF_LINE = "\n";
    // Fields
    private final DataMode dataMode;
    private BufferedWriter bw;
    private DataOutputStream dos;
    private final File file;

    public DataWriter(final File filename) throws IOException {
	this.dataMode = DataMode.TEXT;
	this.bw = new BufferedWriter(new FileWriter(filename));
	this.dos = null;
	this.file = filename;
    }

    public DataWriter(final File filename, final DataMode mode) throws IOException {
	this.dataMode = mode;
	if (this.dataMode != DataMode.BINARY) {
	    this.bw = new BufferedWriter(new FileWriter(filename));
	} else {
	    this.dos = new DataOutputStream(new FileOutputStream(filename));
	}
	this.file = filename;
    }

    // Constructors
    public DataWriter(final String filename) throws IOException {
	this.dataMode = DataMode.TEXT;
	this.bw = new BufferedWriter(new FileWriter(filename));
	this.dos = null;
	this.file = new File(filename);
    }

    public DataWriter(final String filename, final DataMode mode) throws IOException {
	this.dataMode = mode;
	if (this.dataMode != DataMode.BINARY) {
	    this.bw = new BufferedWriter(new FileWriter(filename));
	} else {
	    this.dos = new DataOutputStream(new FileOutputStream(filename));
	}
	this.file = new File(filename);
    }

    @Override
    public void close() throws IOException {
	if (this.dataMode != DataMode.BINARY) {
	    this.bw.close();
	} else {
	    this.dos.close();
	}
    }

    public File getFile() {
	return this.file;
    }

    public void writeBoolean(final boolean b) throws IOException {
	if (this.dataMode != DataMode.BINARY) {
	    this.bw.write(Boolean.toString(b) + DataWriter.END_OF_LINE);
	} else {
	    this.dos.writeBoolean(b);
	}
    }

    public void writeByte(final byte b) throws IOException {
	if (this.dataMode == DataMode.TEXT) {
	    this.bw.write(Byte.toString(b) + DataWriter.END_OF_LINE);
	} else {
	    this.dos.writeByte(b);
	}
    }

    public void writeDouble(final double d) throws IOException {
	if (this.dataMode != DataMode.BINARY) {
	    this.bw.write(Double.toString(d) + DataWriter.END_OF_LINE);
	} else {
	    this.dos.writeDouble(d);
	}
    }

    public void writeFloat(final float f) throws IOException {
	if (this.dataMode != DataMode.BINARY) {
	    this.bw.write(Float.toString(f) + DataWriter.END_OF_LINE);
	} else {
	    this.dos.writeFloat(f);
	}
    }

    public void writeInt(final int i) throws IOException {
	if (this.dataMode != DataMode.BINARY) {
	    this.bw.write(Integer.toString(i) + DataWriter.END_OF_LINE);
	} else {
	    this.dos.writeInt(i);
	}
    }

    public void writeLong(final long l) throws IOException {
	if (this.dataMode != DataMode.BINARY) {
	    this.bw.write(Long.toString(l) + DataWriter.END_OF_LINE);
	} else {
	    this.dos.writeLong(l);
	}
    }

    public void writeShort(final short s) throws IOException {
	if (this.dataMode != DataMode.BINARY) {
	    this.bw.write(Short.toString(s) + DataWriter.END_OF_LINE);
	} else {
	    this.dos.writeShort(s);
	}
    }

    public void writeString(final String s) throws IOException {
	if (this.dataMode != DataMode.BINARY) {
	    this.bw.write(s + DataWriter.END_OF_LINE);
	} else {
	    this.dos.writeUTF(s);
	}
    }
}
