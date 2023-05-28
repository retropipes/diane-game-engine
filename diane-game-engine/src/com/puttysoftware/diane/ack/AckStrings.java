/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.ack;

import java.util.ResourceBundle;

final class AckStrings {
    static String load(final int index) {
	return ResourceBundle.getBundle("locale.ack.ack").getString(Integer.toString(index));
    }

    private AckStrings() {
    }
}
