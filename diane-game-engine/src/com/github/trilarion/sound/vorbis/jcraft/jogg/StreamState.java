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

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 *
 */
public class StreamState {
    byte[] body_data; /* bytes from packet bodies */
    int body_storage; /* storage elements allocated */
    int body_fill; /* elements stored; fill mark */
    private int body_returned; /* elements of fill returned */
    int[] lacing_vals; /* The values that will go to the segment table */
    long[] granule_vals; /*
			  * pcm_pos values for headers. Not compact this way, but it is simple coupled to
			  * the lacing fifo
			  */
    int lacing_storage;
    int lacing_fill;
    int lacing_packet;
    int lacing_returned;
    byte[] header = new byte[282]; /* working space for header encode */
    /**
     *
     */
    public int e_o_s; /*
		       * set when we have buffered the last packet in the logical bitstream
		       */
    int b_o_s; /*
	        * set after we've written the initial page of a logical bitstream
	        */
    int serialno;
    int pageno;
    long packetno; /*
		    * sequence number for decode; the framing knows where there's a hole in the
		    * data, but we need coupling so that the codec (which is in a seperate
		    * abstraction layer) also knows about the gap
		    */
    long granulepos;

    /**
     *
     */
    public StreamState() {
	this.init();
    }

    StreamState(final int serialno) {
	this();
	this.init(serialno);
    }

    void body_expand(final int needed) {
	if (this.body_storage <= this.body_fill + needed) {
	    this.body_storage += needed + 1024;
	    final var foo = new byte[this.body_storage];
	    System.arraycopy(this.body_data, 0, foo, 0, this.body_data.length);
	    this.body_data = foo;
	}
    }

    /**
     *
     */
    public void clear() {
	this.body_data = null;
	this.lacing_vals = null;
	this.granule_vals = null;
    }

    void destroy() {
	this.clear();
    }

    /**
     *
     * @return
     */
    public int eof() {
	return this.e_o_s;
    }

    /*
     * This will flush remaining packets into a page (returning nonzero), even if
     * there is not enough data to trigger a flush normally (undersized page). If
     * there are no packets or partial packets to flush, ogg_stream_flush returns 0.
     * Note that ogg_stream_flush will try to flush a normal sized page like
     * ogg_stream_pageout; a call to ogg_stream_flush does not gurantee that all
     * packets have flushed. Only a return value of 0 from ogg_stream_flush
     * indicates all packet data is flushed into pages.
     *
     * ogg_stream_page will flush the last page in a stream even if it's undersized;
     * you almost certainly want to use ogg_stream_pageout (and *not*
     * ogg_stream_flush) unless you need to flush an undersized page in the middle
     * of a stream for some reason.
     */
    /**
     *
     * @param og
     * @return
     */
    public int flush(final Page og) {
	int i;
	var vals = 0;
	final var maxvals = this.lacing_fill > 255 ? 255 : this.lacing_fill;
	var bytes = 0;
	var acc = 0;
	var granule_pos = this.granule_vals[0];
	if (maxvals == 0) {
	    return 0;
	}
	/* construct a page */
	/* decide how many segments to include */
	/*
	 * If this is the initial header case, the first page must only include the
	 * initial header packet
	 */
	if (this.b_o_s == 0) { /* 'initial header page' case */
	    granule_pos = 0;
	    for (vals = 0; vals < maxvals; vals++) {
		if ((this.lacing_vals[vals] & 0x0ff) < 255) {
		    vals++;
		    break;
		}
	    }
	} else {
	    for (vals = 0; vals < maxvals; vals++) {
		if (acc > 4096) {
		    break;
		}
		acc += this.lacing_vals[vals] & 0x0ff;
		granule_pos = this.granule_vals[vals];
	    }
	}
	/* construct the header in temp storage */
	System.arraycopy("OggS".getBytes(Charset.forName("US-ASCII")), 0, this.header, 0, 4);
	/* stream structure version */
	this.header[4] = 0x00;
	/* continued packet flag? */
	this.header[5] = 0x00;
	if ((this.lacing_vals[0] & 0x100) == 0) {
	    this.header[5] |= 0x01;
	}
	/* first page flag? */
	if (this.b_o_s == 0) {
	    this.header[5] |= 0x02;
	}
	/* last page flag? */
	if (this.e_o_s != 0 && this.lacing_fill == vals) {
	    this.header[5] |= 0x04;
	}
	this.b_o_s = 1;
	/* 64 bits of PCM position */
	for (i = 6; i < 14; i++) {
	    this.header[i] = (byte) granule_pos;
	    granule_pos >>>= 8;
	}
	/* 32 bits of stream serial number */
	{
	    var _serialno = this.serialno;
	    for (i = 14; i < 18; i++) {
		this.header[i] = (byte) _serialno;
		_serialno >>>= 8;
	    }
	}
	/*
	 * 32 bits of page counter (we have both counter and page header because this
	 * val can roll over)
	 */
	if (this.pageno == -1) {
	    this.pageno = 0; /*
			      * because someone called stream_reset; this would be a strange thing to do in
			      * an encode stream, but it has plausible uses
			      */
	}
	{
	    var _pageno = this.pageno++;
	    for (i = 18; i < 22; i++) {
		this.header[i] = (byte) _pageno;
		_pageno >>>= 8;
	    }
	}
	/* zero for computation; filled in later */
	this.header[22] = 0;
	this.header[23] = 0;
	this.header[24] = 0;
	this.header[25] = 0;
	/* segment table */
	this.header[26] = (byte) vals;
	for (i = 0; i < vals; i++) {
	    this.header[i + 27] = (byte) this.lacing_vals[i];
	    bytes += this.header[i + 27] & 0xff;
	}
	/* set pointers in the ogg_page struct */
	og.header_base = this.header;
	og.header = 0;
	og.header_len = vals + 27;
	og.body_base = this.body_data;
	og.body = this.body_returned;
	og.body_len = bytes;
	/* advance the lacing data and set the body_returned pointer */
	this.lacing_fill -= vals;
	System.arraycopy(this.lacing_vals, vals, this.lacing_vals, 0, this.lacing_fill * 4);
	System.arraycopy(this.granule_vals, vals, this.granule_vals, 0, this.lacing_fill * 8);
	this.body_returned += bytes;
	/* calculate the checksum */
	og.checksum();
	/* done */
	return 1;
    }

    /**
     *
     */
    public final void init() {
	this.body_storage = 16 * 1024;
	this.body_data = new byte[this.body_storage];
	this.lacing_storage = 1024;
	this.lacing_vals = new int[this.lacing_storage];
	this.granule_vals = new long[this.lacing_storage];
    }

    /**
     *
     * @param serialno
     */
    public final void init(final int serialno) {
	if (this.body_data == null) {
	    this.init();
	} else {
	    for (var i = 0; i < this.body_data.length; i++) {
		this.body_data[i] = 0;
	    }
	    Arrays.fill(this.lacing_vals, 0);
	    Arrays.fill(this.granule_vals, 0);
	}
	this.serialno = serialno;
    }

    void lacing_expand(final int needed) {
	if (this.lacing_storage <= this.lacing_fill + needed) {
	    this.lacing_storage += needed + 32;
	    final var foo = new int[this.lacing_storage];
	    System.arraycopy(this.lacing_vals, 0, foo, 0, this.lacing_vals.length);
	    this.lacing_vals = foo;
	    final var bar = new long[this.lacing_storage];
	    System.arraycopy(this.granule_vals, 0, bar, 0, this.granule_vals.length);
	    this.granule_vals = bar;
	}
    }

    /* submit data to the internal buffer of the framing engine */
    /**
     *
     * @param op
     * @return
     */
    public int packetin(final Packet op) {
	final var lacing_val = op.bytes / 255 + 1;
	if (this.body_returned != 0) {
	    /*
	     * advance packet data according to the body_returned pointer. We had to keep it
	     * around to return a pointer into the buffer last call
	     */
	    this.body_fill -= this.body_returned;
	    if (this.body_fill != 0) {
		System.arraycopy(this.body_data, this.body_returned, this.body_data, 0, this.body_fill);
	    }
	    this.body_returned = 0;
	}
	/* make sure we have the buffer storage */
	this.body_expand(op.bytes);
	this.lacing_expand(lacing_val);
	/*
	 * Copy in the submitted packet. Yes, the copy is a waste; this is the liability
	 * of overly clean abstraction for the time being. It will actually be fairly
	 * easy to eliminate the extra copy in the future
	 */
	System.arraycopy(op.packet_base, op.packet, this.body_data, this.body_fill, op.bytes);
	this.body_fill += op.bytes;
	/* Store lacing vals for this packet */
	int j;
	for (j = 0; j < lacing_val - 1; j++) {
	    this.lacing_vals[this.lacing_fill + j] = 255;
	    this.granule_vals[this.lacing_fill + j] = this.granulepos;
	}
	this.lacing_vals[this.lacing_fill + j] = op.bytes % 255;
	this.granulepos = this.granule_vals[this.lacing_fill + j] = op.granulepos;
	/* flag the first segment as the beginning of the packet */
	this.lacing_vals[this.lacing_fill] |= 0x100;
	this.lacing_fill += lacing_val;
	/* for the sake of completeness */
	this.packetno++;
	if (op.e_o_s != 0) {
	    this.e_o_s = 1;
	}
	return 0;
    }

    /**
     *
     * @param op
     * @return
     */
    public int packetout(final Packet op) {
	/*
	 * The last part of decode. We have the stream broken into packet segments. Now
	 * we need to group them into packets (or return the out of sync markers)
	 */
	var ptr = this.lacing_returned;
	if (this.lacing_packet <= ptr) {
	    return 0;
	}
	if ((this.lacing_vals[ptr] & 0x400) != 0) {
	    /* We lost sync here; let the app know */
	    this.lacing_returned++;
	    /*
	     * we need to tell the codec there's a gap; it might need to handle previous
	     * packet dependencies.
	     */
	    this.packetno++;
	    return -1;
	}
	/* Gather the whole packet. We'll have no holes or a partial packet */
	{
	    var size = this.lacing_vals[ptr] & 0xff;
	    var bytes = 0;
	    op.packet_base = this.body_data;
	    op.packet = this.body_returned;
	    op.e_o_s = this.lacing_vals[ptr] & 0x200; /* last packet of the stream? */
	    op.b_o_s = this.lacing_vals[ptr] & 0x100; /* first packet of the stream? */
	    bytes += size;
	    while (size == 255) {
		ptr++;
		final var val = this.lacing_vals[ptr];
		size = val & 0xff;
		if ((val & 0x200) != 0) {
		    op.e_o_s = 0x200;
		}
		bytes += size;
	    }
	    op.packetno = this.packetno;
	    op.granulepos = this.granule_vals[ptr];
	    op.bytes = bytes;
	    this.body_returned += bytes;
	    this.lacing_returned = ptr + 1;
	}
	this.packetno++;
	return 1;
    }

    // add the incoming page to the stream state; we decompose the page
    // into packet segments here as well.
    /**
     *
     * @param og
     * @return
     */
    public int pagein(final Page og) {
	final var header_base = og.header_base;
	final var header = og.header;
	final var body_base = og.body_base;
	var body = og.body;
	var bodysize = og.body_len;
	var segptr = 0;
	final var version = og.version();
	final var continued = og.continued();
	var bos = og.bos();
	final var eos = og.eos();
	final var granulepos = og.granulepos();
	final var _serialno = og.serialno();
	final var _pageno = og.pageno();
	final var segments = header_base[header + 26] & 0xff;
	// clean up 'returned data'
	{
	    final var lr = this.lacing_returned;
	    final var br = this.body_returned;
	    // body data
	    if (br != 0) {
		this.body_fill -= br;
		if (this.body_fill != 0) {
		    System.arraycopy(this.body_data, br, this.body_data, 0, this.body_fill);
		}
		this.body_returned = 0;
	    }
	    if (lr != 0) {
		// segment table
		if (this.lacing_fill - lr != 0) {
		    System.arraycopy(this.lacing_vals, lr, this.lacing_vals, 0, this.lacing_fill - lr);
		    System.arraycopy(this.granule_vals, lr, this.granule_vals, 0, this.lacing_fill - lr);
		}
		this.lacing_fill -= lr;
		this.lacing_packet -= lr;
		this.lacing_returned = 0;
	    }
	}
	// check the serial number
	if ((_serialno != this.serialno) || (version > 0)) {
	    return -1;
	}
	this.lacing_expand(segments + 1);
	// are we in sequence?
	if (_pageno != this.pageno) {
	    int i;
	    // unroll previous partial packet (if any)
	    for (i = this.lacing_packet; i < this.lacing_fill; i++) {
		this.body_fill -= this.lacing_vals[i] & 0xff;
		// System.out.println("??");
	    }
	    this.lacing_fill = this.lacing_packet;
	    // make a note of dropped data in segment table
	    if (this.pageno != -1) {
		this.lacing_vals[this.lacing_fill++] = 0x400;
		this.lacing_packet++;
	    }
	    // are we a 'continued packet' page? If so, we'll need to skip
	    // some segments
	    if (continued != 0) {
		bos = 0;
		for (; segptr < segments; segptr++) {
		    final var val = header_base[header + 27 + segptr] & 0xff;
		    body += val;
		    bodysize -= val;
		    if (val < 255) {
			segptr++;
			break;
		    }
		}
	    }
	}
	if (bodysize != 0) {
	    this.body_expand(bodysize);
	    System.arraycopy(body_base, body, this.body_data, this.body_fill, bodysize);
	    this.body_fill += bodysize;
	}
	{
	    var saved = -1;
	    while (segptr < segments) {
		final var val = header_base[header + 27 + segptr] & 0xff;
		this.lacing_vals[this.lacing_fill] = val;
		this.granule_vals[this.lacing_fill] = -1;
		if (bos != 0) {
		    this.lacing_vals[this.lacing_fill] |= 0x100;
		    bos = 0;
		}
		if (val < 255) {
		    saved = this.lacing_fill;
		}
		this.lacing_fill++;
		segptr++;
		if (val < 255) {
		    this.lacing_packet = this.lacing_fill;
		}
	    }
	    /* set the granulepos on the last pcmval of the last full packet */
	    if (saved != -1) {
		this.granule_vals[saved] = granulepos;
	    }
	}
	if (eos != 0) {
	    this.e_o_s = 1;
	    if (this.lacing_fill > 0) {
		this.lacing_vals[this.lacing_fill - 1] |= 0x200;
	    }
	}
	this.pageno = _pageno + 1;
	return 0;
    }

    /*
     * This constructs pages from buffered packet segments. The pointers returned
     * are to static buffers; do not free. The returned buffers are good only until
     * the next call (using the same ogg_stream_state)
     */
    /**
     *
     * @param og
     * @return
     */
    public int pageout(final Page og) {
	if (this.e_o_s != 0 && this.lacing_fill != 0
		|| /* 'were done, now flush' case */ this.body_fill - this.body_returned > 4096
		|| /* 'page nominal size' case */ this.lacing_fill >= 255
		|| /* 'segment table full' case */ this.lacing_fill != 0
			&& this.b_o_s == 0) { /* 'initial header page' case */
	    return this.flush(og);
	}
	return 0;
    }

    /**
     *
     * @return
     */
    public int reset() {
	this.body_fill = 0;
	this.body_returned = 0;
	this.lacing_fill = 0;
	this.lacing_packet = 0;
	this.lacing_returned = 0;
	this.e_o_s = 0;
	this.b_o_s = 0;
	this.pageno = -1;
	this.packetno = 0;
	this.granulepos = 0;
	return 0;
    }
}
