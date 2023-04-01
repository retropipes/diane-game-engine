/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gameshell;

import javax.swing.JPanel;

public abstract class MenuManagerShell {
    // Fields
    protected final JPanel menuContainer;

    // Constructors
    public MenuManagerShell() {
        this.menuContainer = new JPanel();
    }

    public final JPanel getMenuContainer() {
        return this.menuContainer;
    }

    public abstract void populateMenus();

    public abstract void updateMenuItemState(final boolean loaded,
            final boolean dirty);
}
