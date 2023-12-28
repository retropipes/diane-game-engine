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
 *
 */
public class Page {
    private static final int[] crc_lookup = new int[256];
    static {
	for (var i = 0; i < Page.crc_lookup.length; i++) {
	    Page.crc_lookup[i] = Page.crc_entry(i);
	}
    }

    private static int crc_entry(final int index) {
	var r = index << 24;
	for (var i = 0; i < 8; i++) {
	    if ((r & 0x80000000) != 0) {
		r = r << 1 ^ 0x04c11db7; /*
					  * The same as the ethernet generator polynomial, although we use an
					  * unreflected alg and an init/final of 0, not 0xffffffff
					  */
	    } else {
		r <<= 1;
	    }
	}
	return r & 0xffffffff;
    }

    /**
     *
     */
    public byte[] header_base;
    /**
     *
     */
    public int header;
    /**
     *
     */
    public int header_len;
    /**
     *
     */
    public byte[] body_base;
    /**
     *
     */
    public int body;
    /**
     *
     */
    public int body_len;

    /**
     *
     * @return
     */
    public int bos() {
	return this.header_base[this.header + 5] & 0x02;
    }

    void checksum() {
	var crc_reg = 0;
	for (var i = 0; i < this.header_len; i++) {
	    crc_reg = crc_reg << 8 ^ Page.crc_lookup[crc_reg >>> 24 & 0xff ^ this.header_base[this.header + i] & 0xff];
	}
	for (var i = 0; i < this.body_len; i++) {
	    crc_reg = crc_reg << 8 ^ Page.crc_lookup[crc_reg >>> 24 & 0xff ^ this.body_base[this.body + i] & 0xff];
	}
	this.header_base[this.header + 22] = (byte) crc_reg;
	this.header_base[this.header + 23] = (byte) (crc_reg >>> 8);
	this.header_base[this.header + 24] = (byte) (crc_reg >>> 16);
	this.header_base[this.header + 25] = (byte) (crc_reg >>> 24);
    }

    int continued() {
	return this.header_base[this.header + 5] & 0x01;
    }

    /**
     *
     * @return
     */
    public Page copy() {
	return this.copy(new Page());
    }

    /**
     *
     * @param p
     * @return
     */
    public Page copy(final Page p) {
	var tmp = new byte[this.header_len];
	System.arraycopy(this.header_base, this.header, tmp, 0, this.header_len);
	p.header_len = this.header_len;
	p.header_base = tmp;
	p.header = 0;
	tmp = new byte[this.body_len];
	System.arraycopy(this.body_base, this.body, tmp, 0, this.body_len);
	p.body_len = this.body_len;
	p.body_base = tmp;
	p.body = 0;
	return p;
    }

    /**
     *
     * @return
     */
    public int eos() {
	return this.header_base[this.header + 5] & 0x04;
    }

    /**
     *
     * @return
     */
    public long granulepos() {
	long foo = this.header_base[this.header + 13] & 0xff;
	foo = foo << 8 | this.header_base[this.header + 12] & 0xff;
	foo = foo << 8 | this.header_base[this.header + 11] & 0xff;
	foo = foo << 8 | this.header_base[this.header + 10] & 0xff;
	foo = foo << 8 | this.header_base[this.header + 9] & 0xff;
	foo = foo << 8 | this.header_base[this.header + 8] & 0xff;
	foo = foo << 8 | this.header_base[this.header + 7] & 0xff;
	foo = foo << 8 | this.header_base[this.header + 6] & 0xff;
	return foo;
    }

    int pageno() {
	return this.header_base[this.header + 18] & 0xff | (this.header_base[this.header + 19] & 0xff) << 8
		| (this.header_base[this.header + 20] & 0xff) << 16 | (this.header_base[this.header + 21] & 0xff) << 24;
    }

    /**
     *
     * @return
     */
    public int serialno() {
	return this.header_base[this.header + 14] & 0xff | (this.header_base[this.header + 15] & 0xff) << 8
		| (this.header_base[this.header + 16] & 0xff) << 16 | (this.header_base[this.header + 17] & 0xff) << 24;
    }

    int version() {
	return this.header_base[this.header + 4] & 0xff;
    }
}
