/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.locale;

import java.util.ResourceBundle;

import com.puttysoftware.diane.direction.Direction;

public final class DianeStrings {
    public static final String EMPTY = "";

    public static String direction(final Direction item) {
	return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Direction item) {
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
