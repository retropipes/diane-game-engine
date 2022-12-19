/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.internal;

import java.util.ResourceBundle;

final class AckInternalStrings {
    private AckInternalStrings() {
    }

    static String load(final int index) {
	return ResourceBundle.getBundle("locale.ack.internal").getString(Integer.toString(index));
    }
}
