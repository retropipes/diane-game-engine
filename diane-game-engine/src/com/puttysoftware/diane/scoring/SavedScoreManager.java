/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.scoring;

import java.io.IOException;

import com.puttysoftware.diane.fileio.XDataReader;
import com.puttysoftware.diane.fileio.XDataWriter;

public class SavedScoreManager extends ScoreManager {
	// Fields
	private final String scoresFilename;

	// Constructors
	public SavedScoreManager(final int length, final boolean sortOrder, final long startingScore,
			final String customTitle, final String customUnit, final String scoresFile) {
		this(1, length, sortOrder, startingScore, customTitle, new String[] { customUnit }, scoresFile);
	}

	public SavedScoreManager(final int mv, final int length, final boolean sortOrder, final long startingScore,
			final String customTitle, final String[] customUnit, final String scoresFile) {
		super(mv, length, sortOrder, startingScore, customTitle, customUnit);
		this.scoresFilename = scoresFile;
		try {
			this.readScoresFile();
		} catch (final IOException io) {
			// Do nothing
		}
	}

	@Override
	public boolean addScore(final long newScore) {
		final var success = super.addScore(newScore);
		try {
			this.writeScoresFile();
		} catch (final IOException io) {
			// Do nothing
		}
		return success;
	}

	@Override
	public boolean addScore(final long newScore, final String newName) {
		final var success = super.addScore(newScore, newName);
		try {
			this.writeScoresFile();
		} catch (final IOException io) {
			// Do nothing
		}
		return success;
	}

	@Override
	public boolean addScore(final long[] newScore) {
		final var success = super.addScore(newScore);
		try {
			this.writeScoresFile();
		} catch (final IOException io) {
			// Do nothing
		}
		return success;
	}

	private void readScoresFile() throws IOException {
		try (var xdr = new XDataReader(this.scoresFilename, "scores")) { //$NON-NLS-1$
			this.table = SortedScoreTable.readSortedScoreTable(xdr);
			xdr.close();
		} catch (final IOException ioe) {
			throw ioe;
		}
	}

	private void writeScoresFile() throws IOException {
		try (var xdw = new XDataWriter(this.scoresFilename, "scores")) { //$NON-NLS-1$
			this.table.writeSortedScoreTable(xdw);
			xdw.close();
		} catch (final IOException ioe) {
			throw ioe;
		}
	}
}
