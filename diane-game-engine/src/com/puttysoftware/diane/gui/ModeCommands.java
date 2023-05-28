/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

public interface ModeCommands {
    void createCommandsPane();

    void disableDirtyCommands();

    void disableLoadedCommands();

    void disableModeCommands();

    void enableDirtyCommands();

    void enableLoadedCommands();

    void enableModeCommands();

    void enterMode();

    void exitMode();

    void setInitialState();

    void setStatusMessage(String msg);
}
