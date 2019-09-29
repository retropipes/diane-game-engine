/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2010 Eric Ahnell

 Any questions should be directed to the author via email at: lasertank@worldwizard.net
 */
package com.puttysoftware.scoremanager;

import java.io.IOException;

import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class SavedScoreManager extends ScoreManager {
    // Fields
    private final String scoresFilename;

    // Constructors
    public SavedScoreManager(final int mv, final int length,
            final boolean sortOrder, final long startingScore,
            final String customTitle, final String[] customUnit,
            final String scoresFile) {
        super(mv, length, sortOrder, startingScore, customTitle, customUnit);
        this.scoresFilename = scoresFile;
        try {
            this.readScoresFile();
        } catch (final IOException io) {
            // Do nothing
        }
    }

    // Methods
    @Override
    public boolean addScore(final long newScore) {
        final boolean success = super.addScore(newScore);
        try {
            this.writeScoresFile();
        } catch (final IOException io) {
            // Do nothing
        }
        return success;
    }

    @Override
    public boolean addScore(final long[] newScore) {
        final boolean success = super.addScore(newScore);
        try {
            this.writeScoresFile();
        } catch (final IOException io) {
            // Do nothing
        }
        return success;
    }

    @Override
    public boolean addScore(final long newScore, final String newName) {
        final boolean success = super.addScore(newScore, newName);
        try {
            this.writeScoresFile();
        } catch (final IOException io) {
            // Do nothing
        }
        return success;
    }

    private void readScoresFile() throws IOException {
        try (XDataReader xdr = new XDataReader(this.scoresFilename, "scores")) { //$NON-NLS-1$
            this.table = SortedScoreTable.readSortedScoreTable(xdr);
            xdr.close();
        } catch (final IOException ioe) {
            throw ioe;
        }
    }

    private void writeScoresFile() throws IOException {
        try (XDataWriter xdw = new XDataWriter(this.scoresFilename, "scores")) { //$NON-NLS-1$
            this.table.writeSortedScoreTable(xdw);
            xdw.close();
        } catch (final IOException ioe) {
            throw ioe;
        }
    }
}
