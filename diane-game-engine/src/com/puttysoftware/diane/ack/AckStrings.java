package com.puttysoftware.diane.ack;

import java.util.ResourceBundle;

final class AckStrings {
    private AckStrings() {
    }

    static String load(final int index) {
	return ResourceBundle.getBundle("locale.ack.ack").getString(Integer.toString(index));
    }
}
