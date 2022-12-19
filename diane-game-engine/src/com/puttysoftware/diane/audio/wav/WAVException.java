/* WAV Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-wav
 */
package com.puttysoftware.diane.audio.wav;

import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class WAVException extends RuntimeException implements Externalizable {
    public WAVException() {
	super();
    }

    public WAVException(String message) {
	super(message);
    }

    public WAVException(Throwable cause) {
	super(cause);
    }

    public WAVException(String message, Throwable cause) {
	super(message, cause);
    }

    public WAVException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
	throw new NotSerializableException();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	throw new NotSerializableException();
    }
}
