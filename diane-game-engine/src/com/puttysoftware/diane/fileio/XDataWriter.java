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

public class XDataWriter implements DataIOWriter {
	private static final String END_OF_LINE = "\r\n"; //$NON-NLS-1$

	private static String replaceSpecialCharacters(final String s) {
		var r = s;
		r = r.replace("&", "&amp;"); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace("<", "&lt;"); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace(">", "&gt;"); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace("\"", "&quot;"); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace("\'", "&apos;"); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
		return r.replace("\n", "&#xA;"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	// Fields
	private final BufferedWriter fileIO;
	private final String docTag;
	private final File file;

	public XDataWriter(final File filename, final String newDocTag) throws IOException {
		this.fileIO = new BufferedWriter(new FileWriter(filename));
		this.file = filename;
		this.docTag = newDocTag;
		this.writeXHeader();
		this.writeOpeningDocTag();
	}

	public XDataWriter(final OutputStream stream, final String newDocTag) throws IOException {
		this.fileIO = new BufferedWriter(new OutputStreamWriter(stream));
		this.file = null;
		this.docTag = newDocTag;
		this.writeXHeader();
		this.writeOpeningDocTag();
	}

	// Constructors
	public XDataWriter(final String filename, final String newDocTag) throws IOException {
		this.fileIO = new BufferedWriter(new FileWriter(filename));
		this.file = new File(filename);
		this.docTag = newDocTag;
		this.writeXHeader();
		this.writeOpeningDocTag();
	}

	@Override
	public void close() throws DataIOException {
		try {
			this.writeClosingDocTag();
			this.fileIO.close();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	// Methods
	@Override
	public DataMode getDataIOMode() {
		return DataMode.CUSTOM_XML;
	}

	@Override
	public File getFile() {
		return this.file;
	}

	@Override
	public void writeBoolean(final boolean b) throws DataIOException {
		try {
			this.fileIO.write("<" + XDataConstants.BOOLEAN_TAG + ">" //$NON-NLS-1$ //$NON-NLS-2$
					+ Boolean.toString(b) + "</" + XDataConstants.BOOLEAN_TAG + ">" //$NON-NLS-1$ //$NON-NLS-2$
					+ XDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeByte(final byte b) throws DataIOException {
		try {
			this.fileIO.write("<" + XDataConstants.BYTE_TAG + ">" + Byte.toString(b) //$NON-NLS-1$ //$NON-NLS-2$
					+ "</" + XDataConstants.BYTE_TAG + ">" //$NON-NLS-1$ //$NON-NLS-2$
					+ XDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	private void writeClosingDocTag() throws DataIOException {
		try {
			this.fileIO.write("</" + this.docTag + ">"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	public void writeClosingGroup(final String groupName) throws DataIOException {
		try {
			this.fileIO.write("</" + XDataWriter.replaceSpecialCharacters(groupName) + ">" + XDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeDouble(final double d) throws DataIOException {
		try {
			this.fileIO.write("<" + XDataConstants.DOUBLE_TAG + ">" + Double.toString(d) //$NON-NLS-1$ //$NON-NLS-2$
					+ "</" + XDataConstants.DOUBLE_TAG + ">" //$NON-NLS-1$ //$NON-NLS-2$
					+ XDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeInt(final int i) throws DataIOException {
		try {
			this.fileIO.write("<" + XDataConstants.INT_TAG + ">" + Integer.toString(i) //$NON-NLS-1$ //$NON-NLS-2$
					+ "</" + XDataConstants.INT_TAG + ">" //$NON-NLS-1$ //$NON-NLS-2$
					+ XDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeLong(final long l) throws DataIOException {
		try {
			this.fileIO.write("<" + XDataConstants.LONG_TAG + ">" + Long.toString(l) //$NON-NLS-1$ //$NON-NLS-2$
					+ "</" + XDataConstants.LONG_TAG + ">" //$NON-NLS-1$ //$NON-NLS-2$
					+ XDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	private void writeOpeningDocTag() throws DataIOException {
		try {
			this.fileIO.write("<" + this.docTag + ">" + XDataWriter.END_OF_LINE); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	public void writeOpeningGroup(final String groupName) throws DataIOException {
		try {
			this.fileIO.write("<" + XDataWriter.replaceSpecialCharacters(groupName) + ">" + XDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeString(final String s) throws DataIOException {
		try {
			this.fileIO.write("<" + XDataConstants.STRING_TAG + ">" //$NON-NLS-1$ //$NON-NLS-2$
					+ XDataWriter.replaceSpecialCharacters(s) + "</" //$NON-NLS-1$
					+ XDataConstants.STRING_TAG + ">" + XDataWriter.END_OF_LINE); //$NON-NLS-1$
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public void writeUnsignedByte(final int b) throws DataIOException {
		this.writeInt(b);
	}

	private void writeXHeader() throws DataIOException {
		try {
			this.fileIO.write(XDataConstants.X_HEADER + XDataWriter.END_OF_LINE);
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
	}
}
