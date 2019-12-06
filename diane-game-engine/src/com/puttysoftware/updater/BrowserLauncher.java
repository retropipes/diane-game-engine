/*  Rachel: An RPG
 Copyright (C) 2008-2012 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.updater;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowserLauncher {
  public static void openURL(final String url)
      throws IOException, URISyntaxException {
    if (Desktop.isDesktopSupported()) {
      Desktop.getDesktop().browse(new URI(url));
    }
  }
}
