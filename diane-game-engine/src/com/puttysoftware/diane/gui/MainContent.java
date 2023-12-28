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

    protected MainContent(Dimension contentSize) {
	this.resizeEnabled = false;
	super.setPreferredSize(contentSize);
	super.setMinimumSize(contentSize);
	super.setMaximumSize(contentSize);
	super.setSize(contentSize);
    }

    @Override
    public final void writeExternal(ObjectOutput out) throws IOException {
	throw new NotSerializableException();
    }

    @Override
    public final void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	throw new NotSerializableException();
    }

    @Override
    public final void setPreferredSize(Dimension preferredSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setPreferredSize(preferredSize);
    }

    @Override
    public final void setMaximumSize(Dimension maximumSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setMaximumSize(maximumSize);
    }

    @Override
    public final void setMinimumSize(Dimension minimumSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setMinimumSize(minimumSize);
    }

    @Override
    public final void setSize(int width, int height) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setSize(width, height);
    }

    @Override
    public final void setSize(Dimension d) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setSize(d);
    }
}
