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
package com.github.trilarion.sound.vorbis.jcraft.jorbis;

import com.github.trilarion.sound.vorbis.jcraft.jogg.Buffer;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Packet;

/**
 * The comments are not part of vorbis_info so that vorbis_info can be static
 * storage.
 */
public class Comment {
    private static final byte[] _vorbis = "vorbis".getBytes();
    private static final byte[] _vendor = "Xiphophorus libVorbis I 20000508".getBytes();
    private static final int OV_EIMPL = -130;

    static boolean tagcompare(final byte[] s1, final byte[] s2, final int n) {
	var c = 0;
	byte u1, u2;
	while (c < n) {
	    u1 = s1[c];
	    u2 = s2[c];
	    if ('Z' >= u1 && u1 >= 'A') {
		u1 = (byte) (u1 - 'A' + 'a');
	    }
	    if ('Z' >= u2 && u2 >= 'A') {
		u2 = (byte) (u2 - 'A' + 'a');
	    }
	    if (u1 != u2) {
		return false;
	    }
	    c++;
	}
	return true;
    }

    // unlimited user comment fields.
    /**
     *
     */
    public byte[][] user_comments;
    /**
     *
     */
    public int[] comment_lengths;
    /**
     *
     */
    public int comments;
    /**
     *
     */
    public byte[] vendor;

    private void add(final byte[] comment) {
	final var foo = new byte[this.comments + 2][];
	if (this.user_comments != null) {
	    System.arraycopy(this.user_comments, 0, foo, 0, this.comments);
	}
	this.user_comments = foo;
	final var goo = new int[this.comments + 2];
	if (this.comment_lengths != null) {
	    System.arraycopy(this.comment_lengths, 0, goo, 0, this.comments);
	}
	this.comment_lengths = goo;
	final var bar = new byte[comment.length + 1];
	System.arraycopy(comment, 0, bar, 0, comment.length);
	this.user_comments[this.comments] = bar;
	this.comment_lengths[this.comments] = comment.length;
	this.comments++;
	this.user_comments[this.comments] = null;
    }

    /**
     *
     * @param comment
     */
    public void add(final String comment) {
	this.add(comment.getBytes());
    }

    /**
     *
     * @param tag
     * @param contents
     */
    public void add_tag(final String tag, String contents) {
	if (contents == null) {
	    contents = "";
	}
	this.add(tag + "=" + contents);
    }

    void clear() {
	for (var i = 0; i < this.comments; i++) {
	    this.user_comments[i] = null;
	}
	this.user_comments = null;
	this.vendor = null;
    }

    /**
     *
     * @param i
     * @return
     */
    public String getComment(final int i) {
	if (this.comments <= i) {
	    return null;
	}
	return new String(this.user_comments[i], 0, this.user_comments[i].length - 1);
    }

    /**
     *
     * @return
     */
    public String getVendor() {
	return new String(this.vendor, 0, this.vendor.length - 1);
    }

    /**
     *
     * @param op
     * @return
     */
    public int header_out(final Packet op) {
	final var opb = new Buffer();
	opb.writeinit();
	if (this.pack(opb) != 0) {
	    return Comment.OV_EIMPL;
	}
	op.packet_base = new byte[opb.bytes()];
	op.packet = 0;
	op.bytes = opb.bytes();
	System.arraycopy(opb.buffer(), 0, op.packet_base, 0, op.bytes);
	op.b_o_s = 0;
	op.e_o_s = 0;
	op.granulepos = 0;
	return 0;
    }

    /**
     *
     */
    public void init() {
	this.user_comments = null;
	this.comments = 0;
	this.vendor = null;
    }

    int pack(final Buffer opb) {
	// preamble
	opb.write(0x03, 8);
	opb.write(Comment._vorbis);
	// vendor
	opb.write(Comment._vendor.length, 32);
	opb.write(Comment._vendor);
	// comments
	opb.write(this.comments, 32);
	if (this.comments != 0) {
	    for (var i = 0; i < this.comments; i++) {
		if (this.user_comments[i] != null) {
		    opb.write(this.comment_lengths[i], 32);
		    opb.write(this.user_comments[i]);
		} else {
		    opb.write(0, 32);
		}
	    }
	}
	opb.write(1, 1);
	return 0;
    }

    private int query(final byte[] tag, final int count) {
	var i = 0;
	var found = 0;
	final var fulltaglen = tag.length + 1;
	final var fulltag = new byte[fulltaglen];
	System.arraycopy(tag, 0, fulltag, 0, tag.length);
	fulltag[tag.length] = (byte) '=';
	for (i = 0; i < this.comments; i++) {
	    if (Comment.tagcompare(this.user_comments[i], fulltag, fulltaglen)) {
		if (count == found) {
		    // We return a pointer to the data, not a copy
		    // return user_comments[i] + taglen + 1;
		    return i;
		}
		found++;
	    }
	}
	return -1;
    }

    /**
     *
     * @param tag
     * @return
     */
    public String query(final String tag) {
	return this.query(tag, 0);
    }

    /**
     *
     * @param tag
     * @param count
     * @return
     */
    public String query(final String tag, final int count) {
	final var foo = this.query(tag.getBytes(), count);
	if (foo == -1) {
	    return null;
	}
	final var comment = this.user_comments[foo];
	for (var i = 0; i < this.comment_lengths[foo]; i++) {
	    if (comment[i] == '=') {
		return new String(comment, i + 1, this.comment_lengths[foo] - (i + 1));
	    }
	}
	return null;
    }

    @Override
    public String toString() {
	var foo = new StringBuilder("Vendor: ").append(new String(this.vendor, 0, this.vendor.length - 1));
	for (var i = 0; i < this.comments; i++) {
	    foo.append("\nComment: ").append(new String(this.user_comments[i], 0, this.user_comments[i].length - 1));
	}
	return foo.append("\n").toString();
    }

    int unpack(final Buffer opb) {
	final var vendorlen = opb.read(32);
	if (vendorlen < 0) {
	    this.clear();
	    return -1;
	}
	this.vendor = new byte[vendorlen + 1];
	opb.read(this.vendor, vendorlen);
	this.comments = opb.read(32);
	if (this.comments < 0) {
	    this.clear();
	    return -1;
	}
	this.user_comments = new byte[this.comments + 1][];
	this.comment_lengths = new int[this.comments + 1];
	for (var i = 0; i < this.comments; i++) {
	    final var len = opb.read(32);
	    if (len < 0) {
		this.clear();
		return -1;
	    }
	    this.comment_lengths[i] = len;
	    this.user_comments[i] = new byte[len + 1];
	    opb.read(this.user_comments[i], len);
	}
	if (opb.read(1) != 1) {
	    this.clear();
	    return -1;
	}
	return 0;
    }
}
