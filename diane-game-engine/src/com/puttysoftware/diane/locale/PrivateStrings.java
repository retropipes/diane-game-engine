/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.locale;

import java.util.ResourceBundle;

public final class PrivateStrings {
    public static String error(final ErrorString item) {
	return ResourceBundle.getBundle("locale.diane.error").getString(Integer.toString(item.ordinal()));
    }

    private PrivateStrings() {
    }
}
