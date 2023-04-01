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

public class XDataReader implements DataIOReader {
	private static String replaceSpecialCharacters(final String s) {
		var r = s;
		r = r.replace("&amp;", "&"); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace("&lt;", "<"); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace("&gt;", ">"); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace("&quot;", "\""); //$NON-NLS-1$ //$NON-NLS-2$
		r = r.replace("&apos;", "\'"); //$NON-NLS-1$ //$NON-NLS-2$
		return r.replace("&#xA;", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static String[] splitLine(final String line) throws DataIOException {
		final var split = new String[3];
		final var loc0 = line.indexOf('>') + 1;
		final var loc2 = line.indexOf('<', loc0);
		if (loc0 == -1 || loc2 == -1) {
			throw new DataIOException("Unexpected string found: " //$NON-NLS-1$
					+ line + "!"); //$NON-NLS-1$
		}
		split[0] = line.substring(0, loc0);
		split[1] = line.substring(loc0, loc2);
		split[2] = line.substring(loc2);
		return split;
	}

	private static void validateClosingTag(final String tag, final String tagType) throws DataIOException {
		if (!tag.equals("</" + tagType + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
			throw new DataIOException("Expected closing tag of </" //$NON-NLS-1$
					+ tagType + ">, found " + tag + "!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private static void validateOpeningTag(final String tag, final String tagType) throws DataIOException {
		if (!tag.equals("<" + tagType + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
			throw new DataIOException("Expected opening tag of <" //$NON-NLS-1$
					+ tagType + ">, found " + tag + "!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	// Fields
	private final BufferedReader fileIO;
	private final File file;
	private final String docTag;

	public XDataReader(final File filename, final String newDocTag) throws IOException {
		this.fileIO = new BufferedReader(new FileReader(filename));
		this.file = filename;
		this.docTag = newDocTag;
		this.readXHeader();
		this.readOpeningDocTag();
	}

	public XDataReader(final InputStream stream, final String newDocTag) throws IOException {
		this.fileIO = new BufferedReader(new InputStreamReader(stream));
		this.file = null;
		this.docTag = newDocTag;
		this.readXHeader();
		this.readOpeningDocTag();
	}

	// Constructors
	public XDataReader(final String filename, final String newDocTag) throws IOException {
		this.fileIO = new BufferedReader(new FileReader(filename));
		this.file = new File(filename);
		this.docTag = newDocTag;
		this.readXHeader();
		this.readOpeningDocTag();
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
			this.readClosingDocTag();
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
	public boolean readBoolean() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null) {
			final var split = XDataReader.splitLine(line);
			XDataReader.validateOpeningTag(split[0], XDataConstants.BOOLEAN_TAG);
			XDataReader.validateClosingTag(split[2], XDataConstants.BOOLEAN_TAG);
			return Boolean.parseBoolean(split[1]);
		}
		throw new DataIOException("End of file!"); //$NON-NLS-1$
	}

	@Override
	public byte readByte() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null) {
			final var split = XDataReader.splitLine(line);
			XDataReader.validateOpeningTag(split[0], XDataConstants.BYTE_TAG);
			XDataReader.validateClosingTag(split[2], XDataConstants.BYTE_TAG);
			return Byte.parseByte(split[1]);
		}
		throw new DataIOException("End of file!"); //$NON-NLS-1$
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

	private void readClosingDocTag() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null && !line.equals("</" + this.docTag + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
			throw new DataIOException("Closing doc tag does not match: expected </" + this.docTag //$NON-NLS-1$
					+ ">, found " + line + "!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void readClosingGroup(final String groupName) throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null) {
			XDataReader.validateClosingTag(XDataReader.replaceSpecialCharacters(line), groupName);
		} else {
			throw new DataIOException("End of file!");
		}
	}

	@Override
	public double readDouble() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null) {
			final var split = XDataReader.splitLine(line);
			XDataReader.validateOpeningTag(split[0], XDataConstants.DOUBLE_TAG);
			XDataReader.validateClosingTag(split[2], XDataConstants.DOUBLE_TAG);
			return Double.parseDouble(split[1]);
		}
		throw new DataIOException("End of file!"); //$NON-NLS-1$
	}

	@Override
	public int readInt() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null) {
			final var split = XDataReader.splitLine(line);
			XDataReader.validateOpeningTag(split[0], XDataConstants.INT_TAG);
			XDataReader.validateClosingTag(split[2], XDataConstants.INT_TAG);
			return Integer.parseInt(split[1]);
		}
		throw new DataIOException("End of file!"); //$NON-NLS-1$
	}

	@Override
	public long readLong() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null) {
			final var split = XDataReader.splitLine(line);
			XDataReader.validateOpeningTag(split[0], XDataConstants.LONG_TAG);
			XDataReader.validateClosingTag(split[2], XDataConstants.LONG_TAG);
			return Long.parseLong(split[1]);
		}
		throw new DataIOException("End of file!"); //$NON-NLS-1$
	}

	private void readOpeningDocTag() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null && !line.equals("<" + this.docTag + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
			throw new DataIOException("Opening doc tag does not match: expected <" + this.docTag //$NON-NLS-1$
					+ ">, found " + line + "!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void readOpeningGroup(final String groupName) throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null) {
			XDataReader.validateOpeningTag(XDataReader.replaceSpecialCharacters(line), groupName);
		} else {
			throw new DataIOException("End of file!");
		}
	}

	@Override
	public String readString() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line != null) {
			final var split = XDataReader.splitLine(line);
			XDataReader.validateOpeningTag(split[0], XDataConstants.STRING_TAG);
			XDataReader.validateClosingTag(split[2], XDataConstants.STRING_TAG);
			return XDataReader.replaceSpecialCharacters(split[1]);
		}
		throw new DataIOException("End of file!"); //$NON-NLS-1$
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

	private void readXHeader() throws DataIOException {
		var line = "";
		try {
			line = this.fileIO.readLine();
		} catch (final IOException e) {
			throw new DataIOException(e);
		}
		if (line == null || !line.equals(XDataConstants.X_HEADER)) {
			throw new DataIOException("Corrupt or invalid header!"); //$NON-NLS-1$
		}
	}
}
