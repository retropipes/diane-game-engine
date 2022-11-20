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
