/* WAV Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-wav
 */
package com.puttysoftware.diane.sound;

import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SoundException extends RuntimeException implements Externalizable {
    public SoundException() {
    }

    public SoundException(final String message) {
	super(message);
    }

    public SoundException(final String message, final Throwable cause) {
	super(message, cause);
    }

    public SoundException(final String message, final Throwable cause, final boolean enableSuppression,
	    final boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public SoundException(final Throwable cause) {
	super(cause);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
	throw new NotSerializableException();
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
	throw new NotSerializableException();
    }
}
