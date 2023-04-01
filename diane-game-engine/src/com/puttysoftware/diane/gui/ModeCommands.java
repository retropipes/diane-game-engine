/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

public interface ModeCommands {
    void enterMode();

    void exitMode();

    void enableModeCommands();

    void disableModeCommands();

    void setInitialState();

    void createCommandsPane();

    void enableLoadedCommands();

    void disableLoadedCommands();

    void enableDirtyCommands();

    void disableDirtyCommands();

    void setStatusMessage(String msg);
}
