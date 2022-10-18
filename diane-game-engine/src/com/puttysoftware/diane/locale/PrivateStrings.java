package com.puttysoftware.diane.locale;

import java.util.ResourceBundle;

public final class PrivateStrings {
    public static String error(final ErrorString item) {
	return ResourceBundle.getBundle("locale.diane.error").getString(Integer.toString(item.ordinal()));
    }

    private PrivateStrings() {
    }
}
