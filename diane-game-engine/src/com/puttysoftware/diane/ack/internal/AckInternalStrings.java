package com.puttysoftware.diane.ack.internal;

import java.util.ResourceBundle;

final class AckInternalStrings {
    private AckInternalStrings() {
    }

    static String load(final int index) {
	return ResourceBundle.getBundle("locale.ack.internal").getString(Integer.toString(index));
    }
}
