package com.puttysoftware.diane.strings;

import java.util.ResourceBundle;

import com.puttysoftware.diane.locale.ErrorString;
import com.puttysoftware.diane.utilties.Directions;

public final class DianeStrings {
    public static final String EMPTY = "";

    public static String direction(final Directions item) {
	return ResourceBundle.getBundle("locale.diane.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Directions item) {
	return ResourceBundle.getBundle("locale.diane.dirsuffix").getString(Integer.toString(item.ordinal()));
    }

    public static String error(final ErrorString item) {
	return ResourceBundle.getBundle("locale.diane.error").getString(Integer.toString(item.ordinal()));
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
