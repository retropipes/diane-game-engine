/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.sandbox;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;

final class MacOSSandbox extends Sandbox {
    // Constants
    private static final String APP_SUPPORT_FALLBACK_DIR = "Application Support"; //$NON-NLS-1$
    private static final String AUTOSAVE_FALLBACK_DIR = "Saved Application State"; //$NON-NLS-1$
    private static final String CACHES_FALLBACK_DIR = "Caches"; //$NON-NLS-1$
    private static final String DESKTOP_FALLBACK_DIR = "Desktop"; //$NON-NLS-1$
    private static final String DOCUMENTS_FALLBACK_DIR = "Documents"; //$NON-NLS-1$
    private static final String DOWNLOADS_FALLBACK_DIR = "Downloads"; //$NON-NLS-1$
    private static final String FALLBACK_PREFIX = "HOME"; //$NON-NLS-1$
    private static final String LIBRARY_FALLBACK_DIR = "Library/Application Support/Sandboxes/com.puttysoftware.tap"; //$NON-NLS-1$
    private static final String MOVIES_FALLBACK_DIR = "Movies"; //$NON-NLS-1$
    private static final String MUSIC_FALLBACK_DIR = "Music"; //$NON-NLS-1$
    private static final String PICTURES_FALLBACK_DIR = "Pictures"; //$NON-NLS-1$
    private static final String SHARED_PUBLIC_FALLBACK_DIR = "SharedPublic"; //$NON-NLS-1$

    private static String getLibraryFallbackDirectory() {
        return System.getenv(MacOSSandbox.FALLBACK_PREFIX) + File.pathSeparator + MacOSSandbox.LIBRARY_FALLBACK_DIR;
    }

    // Fields
    private final HashMap<SandboxFlag, Boolean> flagCache;

    // Constructor
    MacOSSandbox() {
        this.flagCache = new HashMap<>();
    }

    @Override
    public void cacheFlags() {
        final var noCapsLockLM = System.getProperty("LaunchModifierFlagCapsLock") == null;
        boolean capsLockFlag;
        if (noCapsLockLM) {
            capsLockFlag = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        } else {
            capsLockFlag = System.getProperty("LaunchModifierFlagCapsLock") == "true";
        }
        this.flagCache.put(SandboxFlag.ALT_OPTION, System.getProperty("LaunchModifierFlagOption") == "true");
        this.flagCache.put(SandboxFlag.CAPS_LOCK, capsLockFlag);
        this.flagCache.put(SandboxFlag.CONTROL, System.getProperty("LaunchModifierFlagControl") == "true");
        this.flagCache.put(SandboxFlag.FUNCTION, System.getProperty("LaunchModifierFlagFunction") == "true");
        this.flagCache.put(SandboxFlag.INSERT_HELP, System.getProperty("LaunchModifierFlagHelp") == "true");
        this.flagCache.put(SandboxFlag.NUMERIC_PAD, System.getProperty("LaunchModifierFlagNumericPad") == "true");
        this.flagCache.put(SandboxFlag.OS_ENABLED, System.getProperty("SandboxEnabled") == "true");
        this.flagCache.put(SandboxFlag.SHIFT, System.getProperty("LaunchModifierFlagShift") == "true");
        this.flagCache.put(SandboxFlag.SUPER_COMMAND, System.getProperty("LaunchModifierFlagCommand") == "true");
    }

    @Override
    protected String getDirectory(final SystemDir dir) {
        switch (dir) {
            case APPLICATION:
                var app = System.getProperty("ApplicationDirectory"); //$NON-NLS-1$
                if (app == null) {
                    app = MacOSSandbox.getLibraryFallbackDirectory();
                }
                return app;
            case APPLICATION_SUPPORT:
                var appSupport = System.getProperty("ApplicationSupportDirectory"); //$NON-NLS-1$
                if (appSupport == null) {
                    appSupport = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.APP_SUPPORT_FALLBACK_DIR;
                }
                return appSupport;
            case AUTOSAVED_INFORMATION:
                var autosave = System.getProperty("AutosavedInformationDirectory"); //$NON-NLS-1$
                if (autosave == null) {
                    autosave = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.AUTOSAVE_FALLBACK_DIR;
                }
                return autosave;
            case CACHES:
                var caches = System.getProperty("CachesDirectory"); //$NON-NLS-1$
                if (caches == null) {
                    caches = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.CACHES_FALLBACK_DIR;
                }
                return caches;
            case DOCUMENTS:
                var docs = System.getProperty("DocumentsDirectory"); //$NON-NLS-1$
                if (docs == null) {
                    docs = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.DOCUMENTS_FALLBACK_DIR;
                }
                return docs;
            case DESKTOP:
                var desktop = System.getProperty("DesktopDirectory"); //$NON-NLS-1$
                if (desktop == null) {
                    desktop = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.DESKTOP_FALLBACK_DIR;
                }
                return desktop;
            case DOWNLOADS:
                var downloads = System.getProperty("DownloadsDirectory"); //$NON-NLS-1$
                if (downloads == null) {
                    downloads = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.DOWNLOADS_FALLBACK_DIR;
                }
                return downloads;
            case LIBRARY:
                var library = System.getProperty("LibraryDirectory"); //$NON-NLS-1$
                if (library == null) {
                    library = MacOSSandbox.getLibraryFallbackDirectory();
                }
                return library;
            case MOVIES:
                var movies = System.getProperty("MoviesDirectory"); //$NON-NLS-1$
                if (movies == null) {
                    movies = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.MOVIES_FALLBACK_DIR;
                }
                return movies;
            case MUSIC:
                var music = System.getProperty("MusicDirectory"); //$NON-NLS-1$
                if (music == null) {
                    music = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.MUSIC_FALLBACK_DIR;
                }
                return music;
            case PICTURES:
                var pictures = System.getProperty("PicturesDirectory"); //$NON-NLS-1$
                if (pictures == null) {
                    pictures = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.PICTURES_FALLBACK_DIR;
                }
                return pictures;
            case SHARED_PUBLIC:
                var sharedPublic = System.getProperty("SharedPublicDirectory"); //$NON-NLS-1$
                if (sharedPublic == null) {
                    sharedPublic = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.SHARED_PUBLIC_FALLBACK_DIR;
                }
                return sharedPublic;
            case SYSTEM_APPLICATION:
                var sysApp = System.getProperty("SystemApplicationDirectory"); //$NON-NLS-1$
                if (sysApp == null) {
                    sysApp = MacOSSandbox.getLibraryFallbackDirectory();
                }
                return sysApp;
            case SYSTEM_APPLICATION_SUPPORT:
                var sysAppSupport = System.getProperty("SystemApplicationSupportDirectory"); //$NON-NLS-1$
                if (sysAppSupport == null) {
                    sysAppSupport = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.APP_SUPPORT_FALLBACK_DIR;
                }
                return sysAppSupport;
            case SYSTEM_CACHES:
                var sysCaches = System.getProperty("SystemCachesDirectory"); //$NON-NLS-1$
                if (sysCaches == null) {
                    sysCaches = MacOSSandbox.getLibraryFallbackDirectory() + File.pathSeparator
                            + MacOSSandbox.CACHES_FALLBACK_DIR;
                }
                return sysCaches;
            case SYSTEM_LIBRARY:
                var sysLibrary = System.getProperty("SystemLibraryDirectory"); //$NON-NLS-1$
                if (sysLibrary == null) {
                    sysLibrary = MacOSSandbox.getLibraryFallbackDirectory();
                }
                return sysLibrary;
            case SYSTEM_USER:
                final var sysUser = System.getProperty("SystemUserDirectory"); //$NON-NLS-1$
                if (sysUser != null) {
                    return sysUser;
                }
                return System.getProperty("user.home"); //$NON-NLS-1$
            case USER_HOME:
                final var userHome = System.getProperty("UserHome"); //$NON-NLS-1$
                if (userHome != null) {
                    return userHome;
                }
                return System.getProperty("user.home"); //$NON-NLS-1$
            default:
                return MacOSSandbox.getLibraryFallbackDirectory();
        }
    }

    @Override
    public boolean getFlag(final SandboxFlag flag) {
        return this.flagCache.getOrDefault(flag, false);
    }
}
