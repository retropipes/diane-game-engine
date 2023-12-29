package com.puttysoftware.diane.gui;

import java.awt.Dimension;
import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.JPanel;

public class MainContent extends JPanel implements Externalizable {
    private final boolean resizeEnabled;

    protected MainContent() {
	this.resizeEnabled = true;
    }

    protected MainContent(final Dimension contentSize) {
	this.resizeEnabled = false;
	super.setPreferredSize(contentSize);
	super.setMinimumSize(contentSize);
	super.setMaximumSize(contentSize);
	super.setSize(contentSize);
    }

    @Override
    public final void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
	throw new NotSerializableException();
    }

    @Override
    public final void setMaximumSize(final Dimension maximumSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setMaximumSize(maximumSize);
    }

    @Override
    public final void setMinimumSize(final Dimension minimumSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setMinimumSize(minimumSize);
    }

    @Override
    public final void setPreferredSize(final Dimension preferredSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setPreferredSize(preferredSize);
    }

    @Override
    public final void setSize(final Dimension d) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setSize(d);
    }

    @Override
    public final void setSize(final int width, final int height) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setSize(width, height);
    }

    @Override
    public final void writeExternal(final ObjectOutput out) throws IOException {
	throw new NotSerializableException();
    }
}
