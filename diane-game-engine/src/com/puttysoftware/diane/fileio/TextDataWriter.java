/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TextDataWriter implements DataIOWriter {
	private static final String END_OF_LINE = "\n";
	// Fields
	private final BufferedWriter fileIO;
	private final File file;

	public TextDataWriter(final File filename) throws IOException {
		this.fileIO = new BufferedWriter(new FileWriter(filename));
		this.file = filename;
	}

	public TextDataWriter(final OutputStream stream) {
		this.fileIO = new BufferedWriter(new OutputStreamWriter(stream));
		this.file = null;
	}

	// Constructors
	public TextDataWriter(final String filename) throws IOException {
		this.fileIO = new BufferedWriter(new FileWriter(filename));
		this.file = new File(filename);
	}

	@Override
	public void close() throws DataIOException {
		try {
			this.fileIO.close();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
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
	public void writeBoolean(final boolean b) throws DataIOException {
		try {
			this.fileIO.write(Boolean.toString(b) + TextDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeByte(final byte b) throws DataIOException {
		try {
			this.fileIO.write(Byte.toString(b) + TextDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeDouble(final double d) throws DataIOException {
		try {
			this.fileIO.write(Double.toString(d) + TextDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeInt(final int i) throws DataIOException {
		try {
			this.fileIO.write(Integer.toString(i) + TextDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeLong(final long l) throws DataIOException {
		try {
			this.fileIO.write(Long.toString(l) + TextDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeString(final String s) throws DataIOException {
		try {
			this.fileIO.write(s + TextDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeUnsignedByte(final int b) throws DataIOException {
		this.writeInt(b);
	}
}
