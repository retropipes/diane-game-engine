/*
 * Copyright (C) 2000 ymnk<ymnk@jcraft.com>
 *               2015 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.trilarion.sound.vorbis.jcraft.jogg;

/**
 * This has two layers to place more of the multi-serialno and paging control in
 * the application's hands. First, we expose a data buffer using
 * ogg_decode_buffer(). The app either copies into the buffer, or passes it
 * directly to read(), etc. We then call ogg_decode_wrote() to tell how many
 * bytes we just added.
 *
 * Pages are returned (pointers into the buffer in ogg_sync_state) by
 * ogg_decode_stream(). The page is then submitted to ogg_decode_page() along
 * with the appropriate ogg_stream_state* (ie, matching serialno). We then get
 * raw packets out calling ogg_stream_packet() with a ogg_stream_state. See the
 * 'frame-prog.txt' docs for details and example code.
 */
public class SyncState {
    /**
     *
     */
    public byte[] data;
    int storage;
    int fill;
    int returned;
    int unsynced;
    int headerbytes;
    int bodybytes;
    // sync the stream. This is meant to be useful for finding page
    // boundaries.
    //
    // return values for this:
    // -n) skipped n bytes
    // 0) page not ready; more data (no bytes skipped)
    // n) page synced at current location; page length n bytes
    private final Page pageseek = new Page();
    private final byte[] chksum = new byte[4];

    /**
     *
     * @param size
     * @return
     */
    public int buffer(final int size) {
	// first, clear out any space that has been previously returned
	if (this.returned != 0) {
	    this.fill -= this.returned;
	    if (this.fill > 0) {
		System.arraycopy(this.data, this.returned, this.data, 0, this.fill);
	    }
	    this.returned = 0;
	}
	if (size > this.storage - this.fill) {
	    // We need to extend the internal buffer
	    final var newsize = size + this.fill + 4096; // an extra page to be nice
	    if (this.data != null) {
		final var foo = new byte[newsize];
		System.arraycopy(this.data, 0, foo, 0, this.data.length);
		this.data = foo;
	    } else {
		this.data = new byte[newsize];
	    }
	    this.storage = newsize;
	}
	return this.fill;
    }

    /**
     *
     * @return
     */
    public int clear() {
	this.data = null;
	return 0;
    }

    /**
     *
     * @return
     */
    public int getBufferOffset() {
	return this.fill;
    }

    /**
     *
     * @return
     */
    public int getDataOffset() {
	return this.returned;
    }

    /**
     *
     */
    public void init() {
    }

    // sync the stream and get a page. Keep trying until we find a page.
    // Supress 'sync errors' after reporting the first.
    //
    // return values:
    // -1) recapture (hole in data)
    // 0) need more data
    // 1) page returned
    //
    // Returns pointers into buffered data; invalidated by next call to
    // _stream, _clear, _init, or _buffer
    /**
     *
     * @param og
     * @return
     */
    public int pageout(final Page og) {
	// all we need to do is verify a page at the head of the stream
	// buffer. If it doesn't verify, we look for the next potential
	// frame
	while (true) {
	    final var ret = this.pageseek(og);
	    if (ret > 0) {
		// have a page
		return 1;
	    }
	    if (ret == 0) {
		// need more data
		return 0;
	    }
	    // head did not start a synced page... skipped some bytes
	    if (this.unsynced == 0) {
		this.unsynced = 1;
		return -1;
	    }
	    // loop. keep looking
	}
    }

    /**
     *
     * @param og
     * @return
     */
    public int pageseek(final Page og) {
	var page = this.returned;
	int next;
	var bytes = this.fill - this.returned;
	if (this.headerbytes == 0) {
	    int _headerbytes, i;
	    if (bytes < 27) {
		return 0; // not enough for a header
	    }
	    /* verify capture pattern */
	    if (this.data[page] != 'O' || this.data[page + 1] != 'g' || this.data[page + 2] != 'g'
		    || this.data[page + 3] != 'S') {
		this.headerbytes = 0;
		this.bodybytes = 0;
		// search for possible capture
		next = 0;
		for (var ii = 0; ii < bytes - 1; ii++) {
		    if (this.data[page + 1 + ii] == 'O') {
			next = page + 1 + ii;
			break;
		    }
		}
		// next=memchr(page+1,'O',bytes-1);
		if (next == 0) {
		    next = this.fill;
		}
		this.returned = next;
		return -(next - page);
	    }
	    _headerbytes = (this.data[page + 26] & 0xff) + 27;
	    if (bytes < _headerbytes) {
		return 0; // not enough for header + seg table
	    }
	    // count up body length in the segment table
	    for (i = 0; i < (this.data[page + 26] & 0xff); i++) {
		this.bodybytes += this.data[page + 27 + i] & 0xff;
	    }
	    this.headerbytes = _headerbytes;
	}
	if (this.bodybytes + this.headerbytes > bytes) {
	    return 0;
	}
	// The whole test page is buffered. Verify the checksum
	synchronized (this.chksum) {
	    // Grab the checksum bytes, set the header field to zero
	    System.arraycopy(this.data, page + 22, this.chksum, 0, 4);
	    this.data[page + 22] = 0;
	    this.data[page + 23] = 0;
	    this.data[page + 24] = 0;
	    this.data[page + 25] = 0;
	    // set up a temp page struct and recompute the checksum
	    final var log = this.pageseek;
	    log.header_base = this.data;
	    log.header = page;
	    log.header_len = this.headerbytes;
	    log.body_base = this.data;
	    log.body = page + this.headerbytes;
	    log.body_len = this.bodybytes;
	    log.checksum();
	    // Compare
	    if (this.chksum[0] != this.data[page + 22] || this.chksum[1] != this.data[page + 23]
		    || this.chksum[2] != this.data[page + 24] || this.chksum[3] != this.data[page + 25]) {
		// D'oh. Mismatch! Corrupt page (or miscapture and not a page at
		// all)
		// replace the computed checksum with the one actually read in
		System.arraycopy(this.chksum, 0, this.data, page + 22, 4);
		// Bad checksum. Lose sync */
		this.headerbytes = 0;
		this.bodybytes = 0;
		// search for possible capture
		next = 0;
		for (var ii = 0; ii < bytes - 1; ii++) {
		    if (this.data[page + 1 + ii] == 'O') {
			next = page + 1 + ii;
			break;
		    }
		}
		// next=memchr(page+1,'O',bytes-1);
		if (next == 0) {
		    next = this.fill;
		}
		this.returned = next;
		return -(next - page);
	    }
	}
	// yes, have a whole page all ready to go
	{
	    page = this.returned;
	    if (og != null) {
		og.header_base = this.data;
		og.header = page;
		og.header_len = this.headerbytes;
		og.body_base = this.data;
		og.body = page + this.headerbytes;
		og.body_len = this.bodybytes;
	    }
	    this.unsynced = 0;
	    this.returned += bytes = this.headerbytes + this.bodybytes;
	    this.headerbytes = 0;
	    this.bodybytes = 0;
	    return bytes;
	}
    }

    // clear things to an initial state. Good to call, eg, before seeking
    /**
     *
     * @return
     */
    public int reset() {
	this.fill = 0;
	this.returned = 0;
	this.unsynced = 0;
	this.headerbytes = 0;
	this.bodybytes = 0;
	return 0;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public int wrote(final int bytes) {
	if (this.fill + bytes > this.storage) {
	    return -1;
	}
	this.fill += bytes;
	return 0;
    }
}
