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
public class Buffer {
    private static final int BUFFER_INCREMENT = 256;
    private static final int[] mask = { 0x00000000, 0x00000001, 0x00000003, 0x00000007, 0x0000000f, 0x0000001f,
	    0x0000003f, 0x0000007f, 0x000000ff, 0x000001ff, 0x000003ff, 0x000007ff, 0x00000fff, 0x00001fff, 0x00003fff,
	    0x00007fff, 0x0000ffff, 0x0001ffff, 0x0003ffff, 0x0007ffff, 0x000fffff, 0x001fffff, 0x003fffff, 0x007fffff,
	    0x00ffffff, 0x01ffffff, 0x03ffffff, 0x07ffffff, 0x0fffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
	    0xffffffff };

    /**
     *
     * @param v
     * @return
     */
    public static int ilog(int v) {
	var ret = 0;
	while (v > 0) {
	    ret++;
	    v >>>= 1;
	}
	return ret;
    }

    int ptr = 0;
    byte[] buffer = null;
    int endbit = 0;
    int endbyte = 0;
    int storage = 0;

    /**
     *
     * @param bits
     */
    public void adv(int bits) {
	bits += this.endbit;
	this.ptr += bits / 8;
	this.endbyte += bits / 8;
	this.endbit = bits & 7;
    }

    /**
     *
     */
    public void adv1() {
	++this.endbit;
	if (this.endbit > 7) {
	    this.endbit = 0;
	    this.ptr++;
	    this.endbyte++;
	}
    }

    /**
     *
     * @return
     */
    public int bits() {
	return this.endbyte * 8 + this.endbit;
    }

    /**
     *
     * @return
     */
    public byte[] buffer() {
	return this.buffer;
    }

    /**
     *
     * @return
     */
    public int bytes() {
	return this.endbyte + (this.endbit + 7) / 8;
    }

    /**
     *
     * @param bits
     * @return
     */
    public int look(int bits) {
	int ret;
	final var m = Buffer.mask[bits];
	bits += this.endbit;
	if ((this.endbyte + 4 >= this.storage) && (this.endbyte + (bits - 1) / 8 >= this.storage)) {
	    return -1;
	}
	ret = (this.buffer[this.ptr] & 0xff) >>> this.endbit;
	if (bits > 8) {
	    ret |= (this.buffer[this.ptr + 1] & 0xff) << 8 - this.endbit;
	    if (bits > 16) {
		ret |= (this.buffer[this.ptr + 2] & 0xff) << 16 - this.endbit;
		if (bits > 24) {
		    ret |= (this.buffer[this.ptr + 3] & 0xff) << 24 - this.endbit;
		    if (bits > 32 && this.endbit != 0) {
			ret |= (this.buffer[this.ptr + 4] & 0xff) << 32 - this.endbit;
		    }
		}
	    }
	}
	return m & ret;
    }

    /**
     *
     * @return
     */
    public int look1() {
	if (this.endbyte >= this.storage) {
	    return -1;
	}
	return this.buffer[this.ptr] >> this.endbit & 1;
    }

    /**
     *
     * @param s
     * @param bytes
     */
    public void read(final byte[] s, int bytes) {
	var i = 0;
	while (bytes-- != 0) {
	    s[i] = (byte) this.read(8);
	    i++;
	}
    }

    /**
     *
     * @param bits
     * @return
     */
    public int read(int bits) {
	int ret;
	final var m = Buffer.mask[bits];
	bits += this.endbit;
	if (this.endbyte + 4 >= this.storage) {
	    ret = -1;
	    if (this.endbyte + (bits - 1) / 8 >= this.storage) {
		this.ptr += bits / 8;
		this.endbyte += bits / 8;
		this.endbit = bits & 7;
		return ret;
	    }
	}
	ret = (this.buffer[this.ptr] & 0xff) >>> this.endbit;
	if (bits > 8) {
	    ret |= (this.buffer[this.ptr + 1] & 0xff) << 8 - this.endbit;
	    if (bits > 16) {
		ret |= (this.buffer[this.ptr + 2] & 0xff) << 16 - this.endbit;
		if (bits > 24) {
		    ret |= (this.buffer[this.ptr + 3] & 0xff) << 24 - this.endbit;
		    if (bits > 32 && this.endbit != 0) {
			ret |= (this.buffer[this.ptr + 4] & 0xff) << 32 - this.endbit;
		    }
		}
	    }
	}
	ret &= m;
	this.ptr += bits / 8;
	this.endbyte += bits / 8;
	this.endbit = bits & 7;
	return ret;
    }

    /**
     *
     * @return
     */
    public int read1() {
	int ret;
	if (this.endbyte >= this.storage) {
	    ret = -1;
	    this.endbit++;
	    if (this.endbit > 7) {
		this.endbit = 0;
		this.ptr++;
		this.endbyte++;
	    }
	    return ret;
	}
	ret = this.buffer[this.ptr] >> this.endbit & 1;
	this.endbit++;
	if (this.endbit > 7) {
	    this.endbit = 0;
	    this.ptr++;
	    this.endbyte++;
	}
	return ret;
    }

    /**
     *
     * @param bits
     * @return
     */
    public int readB(int bits) {
	int ret;
	final var m = 32 - bits;
	bits += this.endbit;
	if (this.endbyte + 4 >= this.storage) {
	    /* not the main path */
	    ret = -1;
	    if (this.endbyte * 8 + bits > this.storage * 8) {
		this.ptr += bits / 8;
		this.endbyte += bits / 8;
		this.endbit = bits & 7;
		return ret;
	    }
	}
	ret = (this.buffer[this.ptr] & 0xff) << 24 + this.endbit;
	if (bits > 8) {
	    ret |= (this.buffer[this.ptr + 1] & 0xff) << 16 + this.endbit;
	    if (bits > 16) {
		ret |= (this.buffer[this.ptr + 2] & 0xff) << 8 + this.endbit;
		if (bits > 24) {
		    ret |= (this.buffer[this.ptr + 3] & 0xff) << this.endbit;
		    if (bits > 32 && this.endbit != 0) {
			ret |= (this.buffer[this.ptr + 4] & 0xff) >> 8 - this.endbit;
		    }
		}
	    }
	}
	ret = ret >>> (m >> 1) >>> (m + 1 >> 1);
	this.ptr += bits / 8;
	this.endbyte += bits / 8;
	this.endbit = bits & 7;
	return ret;
    }

    /**
     *
     * @param buf
     * @param bytes
     */
    public void readinit(final byte[] buf, final int bytes) {
	this.readinit(buf, 0, bytes);
    }

    /**
     *
     * @param buf
     * @param start
     * @param bytes
     */
    public void readinit(final byte[] buf, final int start, final int bytes) {
	this.ptr = start;
	this.buffer = buf;
	this.endbit = this.endbyte = 0;
	this.storage = bytes;
    }

    /**
     *
     * @param s
     */
    public void write(final byte[] s) {
	for (final byte b : s) {
	    if (b == 0) {
		break;
	    }
	    this.write(b, 8);
	}
    }

    /**
     *
     * @param value
     * @param bits
     */
    public void write(int value, int bits) {
	if (this.endbyte + 4 >= this.storage) {
	    final var foo = new byte[this.storage + Buffer.BUFFER_INCREMENT];
	    System.arraycopy(this.buffer, 0, foo, 0, this.storage);
	    this.buffer = foo;
	    this.storage += Buffer.BUFFER_INCREMENT;
	}
	value &= Buffer.mask[bits];
	bits += this.endbit;
	this.buffer[this.ptr] |= (byte) (value << this.endbit);
	if (bits >= 8) {
	    this.buffer[this.ptr + 1] = (byte) (value >>> 8 - this.endbit);
	    if (bits >= 16) {
		this.buffer[this.ptr + 2] = (byte) (value >>> 16 - this.endbit);
		if (bits >= 24) {
		    this.buffer[this.ptr + 3] = (byte) (value >>> 24 - this.endbit);
		    if (bits >= 32) {
			if (this.endbit > 0) {
			    this.buffer[this.ptr + 4] = (byte) (value >>> 32 - this.endbit);
			} else {
			    this.buffer[this.ptr + 4] = 0;
			}
		    }
		}
	    }
	}
	this.endbyte += bits / 8;
	this.ptr += bits / 8;
	this.endbit = bits & 7;
    }

    /**
     *
     */
    public void writeclear() {
	this.buffer = null;
    }

    /**
     *
     */
    public void writeinit() {
	this.buffer = new byte[Buffer.BUFFER_INCREMENT];
	this.ptr = 0;
	this.buffer[0] = (byte) '\0';
	this.storage = Buffer.BUFFER_INCREMENT;
    }
}
