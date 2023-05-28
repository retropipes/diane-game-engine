/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.asset.music;

class Pattern {
    public int numRows;
    public byte[] data;

    public Pattern(final int numChannels, final int newNumRows) {
	this.numRows = newNumRows;
	this.data = new byte[numChannels * newNumRows * 5];
    }

    public void getNote(final int index, final Note note) {
	final var offset = index * 5;
	note.key = this.data[offset] & 0xFF;
	note.instrument = this.data[offset + 1] & 0xFF;
	note.volume = this.data[offset + 2] & 0xFF;
	note.effect = this.data[offset + 3] & 0xFF;
	note.param = this.data[offset + 4] & 0xFF;
    }

    public void toStringBuffer(final StringBuffer out) {
	final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	final var channels = this.data.length / (this.numRows * 5);
	var data_offset = 0;
	for (var row = 0; row < this.numRows; row++) {
	    for (var channel = 0; channel < channels; channel++) {
		for (var n = 0; n < 5; n++) {
		    final int b = this.data[data_offset];
		    data_offset++;
		    if (b == 0) {
			out.append("--"); //$NON-NLS-1$
		    } else {
			out.append(hex[b >> 4 & 0xF]);
			out.append(hex[b & 0xF]);
		    }
		}
		out.append(' ');
	    }
	    out.append('\n');
	}
    }
}
