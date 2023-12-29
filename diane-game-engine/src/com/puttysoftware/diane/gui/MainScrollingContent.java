package com.puttysoftware.diane.gui;

import java.awt.Dimension;
import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.JScrollPane;

public final class MainScrollingContent extends JScrollPane implements Externalizable {
    private final boolean resizeEnabled;

    protected MainScrollingContent(final MainContent view) {
	super(view);
	this.resizeEnabled = true;
    }

    protected MainScrollingContent(final MainContent view, final Dimension contentSize) {
	super(view);
	this.resizeEnabled = false;
	super.setPreferredSize(contentSize);
	super.setMinimumSize(contentSize);
	super.setMaximumSize(contentSize);
	super.setSize(contentSize);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
	throw new NotSerializableException();
    }

    @Override
    public void setMaximumSize(final Dimension maximumSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setMaximumSize(maximumSize);
    }

    @Override
    public void setMinimumSize(final Dimension minimumSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setMinimumSize(minimumSize);
    }

    @Override
    public void setPreferredSize(final Dimension preferredSize) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setPreferredSize(preferredSize);
    }

    @Override
    public void setSize(final Dimension d) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setSize(d);
    }

    @Override
    public void setSize(final int width, final int height) {
	if (!this.resizeEnabled) {
	    // Deny
	    throw new UnsupportedOperationException();
	}
	super.setSize(width, height);
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
	throw new NotSerializableException();
    }
}
