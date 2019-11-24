package com.puttysoftware.xio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XDataReader implements AutoCloseable {
  // Fields
  private final BufferedReader br;
  private final String docTag;

  // Constructors
  public XDataReader(final String filename, final String newDocTag)
      throws IOException {
    this.br = new BufferedReader(new FileReader(filename));
    this.docTag = newDocTag;
    this.readXHeader();
    this.readOpeningDocTag();
  }

  public XDataReader(final InputStream stream, final String newDocTag)
      throws IOException {
    this.br = new BufferedReader(new InputStreamReader(stream));
    this.docTag = newDocTag;
    this.readXHeader();
    this.readOpeningDocTag();
  }

  // Methods
  @Override
  public void close() throws IOException {
    this.readClosingDocTag();
    this.br.close();
  }

  public double readDouble() throws IOException {
    final String line = this.br.readLine();
    if (line != null) {
      final String[] split = XDataReader.splitLine(line);
      XDataReader.validateOpeningTag(split[0], XDataConstants.DOUBLE_TAG);
      XDataReader.validateClosingTag(split[2], XDataConstants.DOUBLE_TAG);
      return Double.parseDouble(split[1]);
    }
    throw new IOException("End of file!"); //$NON-NLS-1$
  }

  public int readInt() throws IOException {
    final String line = this.br.readLine();
    if (line != null) {
      final String[] split = XDataReader.splitLine(line);
      XDataReader.validateOpeningTag(split[0], XDataConstants.INT_TAG);
      XDataReader.validateClosingTag(split[2], XDataConstants.INT_TAG);
      return Integer.parseInt(split[1]);
    }
    throw new IOException("End of file!"); //$NON-NLS-1$
  }

  public long readLong() throws IOException {
    final String line = this.br.readLine();
    if (line != null) {
      final String[] split = XDataReader.splitLine(line);
      XDataReader.validateOpeningTag(split[0], XDataConstants.LONG_TAG);
      XDataReader.validateClosingTag(split[2], XDataConstants.LONG_TAG);
      return Long.parseLong(split[1]);
    }
    throw new IOException("End of file!"); //$NON-NLS-1$
  }

  public byte readByte() throws IOException {
    final String line = this.br.readLine();
    if (line != null) {
      final String[] split = XDataReader.splitLine(line);
      XDataReader.validateOpeningTag(split[0], XDataConstants.BYTE_TAG);
      XDataReader.validateClosingTag(split[2], XDataConstants.BYTE_TAG);
      return Byte.parseByte(split[1]);
    }
    throw new IOException("End of file!"); //$NON-NLS-1$
  }

  public boolean readBoolean() throws IOException {
    final String line = this.br.readLine();
    if (line != null) {
      final String[] split = XDataReader.splitLine(line);
      XDataReader.validateOpeningTag(split[0], XDataConstants.BOOLEAN_TAG);
      XDataReader.validateClosingTag(split[2], XDataConstants.BOOLEAN_TAG);
      return Boolean.parseBoolean(split[1]);
    }
    throw new IOException("End of file!"); //$NON-NLS-1$
  }

  public String readString() throws IOException {
    final String line = this.br.readLine();
    if (line != null) {
      final String[] split = XDataReader.splitLine(line);
      XDataReader.validateOpeningTag(split[0], XDataConstants.STRING_TAG);
      XDataReader.validateClosingTag(split[2], XDataConstants.STRING_TAG);
      return XDataReader.replaceSpecialCharacters(split[1]);
    }
    throw new IOException("End of file!"); //$NON-NLS-1$
  }

  private static void validateOpeningTag(final String tag, final String tagType)
      throws IOException {
    if (!tag.equals("<" + tagType + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
      throw new UnexpectedTagException("Expected opening tag of <" //$NON-NLS-1$
          + tagType + ">, found " + tag + "!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  private static void validateClosingTag(final String tag, final String tagType)
      throws IOException {
    if (!tag.equals("</" + tagType + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
      throw new UnexpectedTagException("Expected closing tag of </" //$NON-NLS-1$
          + tagType + ">, found " + tag + "!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  private static String[] splitLine(final String line)
      throws UnexpectedTagException {
    final String[] split = new String[3];
    final int loc0 = line.indexOf('>') + 1;
    final int loc2 = line.indexOf('<', loc0);
    if (loc0 == -1 || loc2 == -1) {
      throw new UnexpectedTagException("Unexpected string found: " //$NON-NLS-1$
          + line + "!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    split[0] = line.substring(0, loc0);
    split[1] = line.substring(loc0, loc2);
    split[2] = line.substring(loc2);
    return split;
  }

  private void readXHeader() throws IOException {
    final String header = this.br.readLine();
    if (header == null) {
      throw new UnexpectedTagException("Corrupt or invalid header!"); //$NON-NLS-1$
    }
    if (!header.equals(XDataConstants.X_HEADER)) {
      throw new UnexpectedTagException("Corrupt or invalid header!"); //$NON-NLS-1$
    }
  }

  private void readOpeningDocTag() throws IOException {
    final String line = this.br.readLine();
    if (line != null && !line.equals("<" + this.docTag + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
      throw new UnexpectedTagException(
          "Opening doc tag does not match: expected <" + this.docTag //$NON-NLS-1$
              + ">, found " + line + "!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  private void readClosingDocTag() throws IOException {
    final String line = this.br.readLine();
    if (line != null && !line.equals("</" + this.docTag + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
      throw new UnexpectedTagException(
          "Closing doc tag does not match: expected </" + this.docTag //$NON-NLS-1$
              + ">, found " + line + "!"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  private static String replaceSpecialCharacters(final String s) {
    String r = s;
    r = r.replace("&amp;", "&"); //$NON-NLS-1$ //$NON-NLS-2$
    r = r.replace("&lt;", "<"); //$NON-NLS-1$ //$NON-NLS-2$
    r = r.replace("&gt;", ">"); //$NON-NLS-1$ //$NON-NLS-2$
    r = r.replace("&quot;", "\""); //$NON-NLS-1$ //$NON-NLS-2$
    r = r.replace("&apos;", "\'"); //$NON-NLS-1$ //$NON-NLS-2$
    return r.replace("&#xA;", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
  }
}
