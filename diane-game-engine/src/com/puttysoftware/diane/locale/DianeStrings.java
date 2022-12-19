/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.locale;

import java.util.ResourceBundle;

import com.puttysoftware.diane.direction.Direction1D;
import com.puttysoftware.diane.direction.Direction2D;
import com.puttysoftware.diane.direction.Direction3D;
import com.puttysoftware.diane.direction.Direction4D;
import com.puttysoftware.diane.direction.Direction5D;
import com.puttysoftware.diane.direction.Direction6D;
import com.puttysoftware.diane.utility.Directions;

public final class DianeStrings {
    public static final String EMPTY = "";

    public static String direction(final Directions item) {
	return ResourceBundle.getBundle("locale.diane.dirold").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Directions item) {
	return ResourceBundle.getBundle("locale.diane.dirsufold").getString(Integer.toString(item.ordinal()));
    }

    public static String direction(final Direction1D item) {
	return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String direction(final Direction2D item) {
	return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String direction(final Direction3D item) {
	return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String direction(final Direction4D item) {
	return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String direction(final Direction5D item) {
	return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String direction(final Direction6D item) {
	return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Direction1D item) {
	return ResourceBundle.getBundle("locale.diane.dirsuffix").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Direction2D item) {
	return ResourceBundle.getBundle("locale.diane.dirsuffix").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Direction3D item) {
	return ResourceBundle.getBundle("locale.diane.dirsuffix").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Direction4D item) {
	return ResourceBundle.getBundle("locale.diane.dirsuffix").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Direction5D item) {
	return ResourceBundle.getBundle("locale.diane.dirsuffix").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Direction6D item) {
	return ResourceBundle.getBundle("locale.diane.dirsuffix").getString(Integer.toString(item.ordinal()));
    }

    public static String subst(final String orig, final String... values) {
	var result = orig;
	for (var s = 0; s < values.length; s++) {
	    result = result.replace("%" + s, values[s]);
	}
	return result;
    }

    private DianeStrings() {
    }
}
