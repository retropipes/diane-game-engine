/*  Rachel: An RPG
 Copyright (C) 2008-2012 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.updater;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.puttysoftware.commondialogs.CommonDialogs;

public class BrowserLauncher {
    private static final String errMsg = "Error attempting to launch web browser!"; //$NON-NLS-1$

    public static void openURL(final String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (final IOException e) {
                CommonDialogs.showErrorDialog(BrowserLauncher.errMsg,
                        "Updater Error"); //$NON-NLS-1$
            } catch (final URISyntaxException e) {
                CommonDialogs.showErrorDialog(BrowserLauncher.errMsg,
                        "Updater Error"); //$NON-NLS-1$
            }
        } else {
            CommonDialogs.showErrorDialog(BrowserLauncher.errMsg,
                    "Updater Error"); //$NON-NLS-1$
        }
    }
}
