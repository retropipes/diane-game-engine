/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.fileio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataIOFactory {
    private static final String CUSTOM_XML = "customxml";

    public static DataIOReader createReader(final DataMode mode, final File file) throws IOException {
        return switch (mode) {
            case BINARY -> new BinaryDataReader(file);
            case CUSTOM_XML -> new XDataReader(file, DataIOFactory.CUSTOM_XML);
            case GAME_IO -> new GameIODataReader(file.getAbsolutePath());
            case TEXT -> new TextDataReader(file);
            case XML -> new XMLDataReader(file);
            default -> throw new IllegalArgumentException();
        };
    }

    public static DataIOReader createReader(final DataMode mode, final InputStream stream) throws IOException {
        return switch (mode) {
            case BINARY -> new BinaryDataReader(stream);
            case CUSTOM_XML -> new XDataReader(stream, DataIOFactory.CUSTOM_XML);
            case GAME_IO -> throw new IllegalArgumentException(); // Game IO doesn't support streams
            case TEXT -> new TextDataReader(stream);
            case XML -> new XMLDataReader(stream);
            default -> throw new IllegalArgumentException();
        };
    }

    public static DataIOReader createReader(final DataMode mode, final String filename) throws IOException {
        return switch (mode) {
            case BINARY -> new BinaryDataReader(filename);
            case CUSTOM_XML -> new XDataReader(filename, DataIOFactory.CUSTOM_XML);
            case GAME_IO -> new GameIODataReader(filename);
            case TEXT -> new TextDataReader(filename);
            case XML -> new XMLDataReader(filename);
            default -> throw new IllegalArgumentException();
        };
    }

    public static DataIOWriter createWriter(final DataMode mode, final File file) throws IOException {
        return switch (mode) {
            case BINARY -> new BinaryDataWriter(file);
            case CUSTOM_XML -> new XDataWriter(file, DataIOFactory.CUSTOM_XML);
            case GAME_IO -> new GameIODataWriter(file.getAbsolutePath());
            case TEXT -> new TextDataWriter(file);
            case XML -> new XMLDataWriter(file);
            default -> throw new IllegalArgumentException();
        };
    }

    public static DataIOWriter createWriter(final DataMode mode, final OutputStream stream) throws IOException {
        return switch (mode) {
            case BINARY -> new BinaryDataWriter(stream);
            case CUSTOM_XML -> new XDataWriter(stream, DataIOFactory.CUSTOM_XML);
            case GAME_IO -> throw new IllegalArgumentException(); // Game IO doesn't support streams
            case TEXT -> new TextDataWriter(stream);
            case XML -> new XMLDataWriter(stream);
            default -> throw new IllegalArgumentException();
        };
    }

    public static DataIOWriter createWriter(final DataMode mode, final String filename) throws IOException {
        return switch (mode) {
            case BINARY -> new BinaryDataWriter(filename);
            case CUSTOM_XML -> new XDataWriter(filename, DataIOFactory.CUSTOM_XML);
            case GAME_IO -> new GameIODataWriter(filename);
            case TEXT -> new TextDataWriter(filename);
            case XML -> new XMLDataWriter(filename);
            default -> throw new IllegalArgumentException();
        };
    }
}
