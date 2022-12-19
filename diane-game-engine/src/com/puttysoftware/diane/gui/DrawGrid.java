/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.diane.storage.ObjectStorage;

public class DrawGrid extends ObjectStorage {
    public DrawGrid(final int numSquares) {
	super(numSquares, numSquares);
    }

    public BufferedImageIcon getImageCell(final int row, final int col) {
	return (BufferedImageIcon) this.getCell(row, col);
    }

    public void setImageCell(final BufferedImageIcon bii, final int row, final int col) {
	this.setCell(bii, row, col);
    }
}
