/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gameshell;

import java.util.HashMap;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.ErrorHandler;
import com.puttysoftware.diane.internal.ErrorLogger;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.gui.Screen;

public abstract class GameShell {
    // Fields
    private final String gameName;
    private final ErrorLogger logger;
    private final ExceptionMessageConfiguration errorConfig;
    private final ExceptionMessageConfiguration warningConfig;
    private final HashMap<Integer, Screen> screens;

    // Constructor
    public GameShell(final String name,
            final ExceptionMessageConfiguration errorSettings,
            final ExceptionMessageConfiguration warningSettings) {
        super();
        this.gameName = name;
        this.errorConfig = errorSettings;
        this.warningConfig = warningSettings;
        this.logger = new ErrorLogger(this.gameName);
        this.screens = new HashMap<>();
        Diane.installCustomErrorHandler(new GameShellErrorHandler());
    }

    public final void handleError(final Throwable problem) {
        if (this.errorConfig != null && this.errorConfig.isDialogDisplayed()) {
            // Display error message
            CommonDialogs.showErrorDialog(this.errorConfig.getMessage(),
                    this.errorConfig.getTitle());
        }
        // Record it with the logger
        this.logger.logError(problem);
    }

    public final void handleWarning(final Throwable problem) {
        if (this.warningConfig != null
                && this.warningConfig.isDialogDisplayed()) {
            // Display warning message
            CommonDialogs.showTitledDialog(this.warningConfig.getMessage(),
                    this.warningConfig.getTitle());
        }
        // Record it with the logger
        this.logger.logWarning(problem);
    }

    public final void registerScreen(final int key,
            final Screen value) {
        this.screens.put(key, value);
    }

    public final Screen getScreen(final int key) {
        return this.screens.get(key);
    }

    public abstract MenuManagerShell getMenus();

    private class GameShellErrorHandler implements ErrorHandler {
        @Override
        public void uncaughtException(Thread inT, Throwable inE) {
            GameShell.this.handleError(inE);
        }

        @Override
        public void handleWarning(final Throwable t) {
            GameShell.this.handleWarning(t);
        }
    }
}
