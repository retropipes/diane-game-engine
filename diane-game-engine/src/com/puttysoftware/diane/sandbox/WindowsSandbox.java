/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.sandbox;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;

final class WindowsSandbox extends Sandbox {
	// Constants
	private static final String APP_SUPPORT_FALLBACK_DIR = "Support"; //$NON-NLS-1$
	private static final String AUTOSAVE_FALLBACK_DIR = "Autosave"; //$NON-NLS-1$
	private static final String CACHES_FALLBACK_DIR = "Caches"; //$NON-NLS-1$
	private static final String DESKTOP_FALLBACK_DIR = "Desktop"; //$NON-NLS-1$
	private static final String DOCUMENTS_FALLBACK_DIR = "Documents"; //$NON-NLS-1$
	private static final String DOWNLOADS_FALLBACK_DIR = "Downloads"; //$NON-NLS-1$
	private static final String FALLBACK_PREFIX = "APPDATA"; //$NON-NLS-1$
	private static final String LIBRARY_FALLBACK_DIR = "Sandboxes\\com.puttysoftware.tap"; //$NON-NLS-1$
	private static final String MOVIES_FALLBACK_DIR = "Movies"; //$NON-NLS-1$
	private static final String MUSIC_FALLBACK_DIR = "Music"; //$NON-NLS-1$
	private static final String PICTURES_FALLBACK_DIR = "Pictures"; //$NON-NLS-1$
	private static final String SHARED_PUBLIC_FALLBACK_DIR = "SharedPublic"; //$NON-NLS-1$

	private static String getLibraryFallbackDirectory() {
		return System.getenv(WindowsSandbox.FALLBACK_PREFIX) + File.pathSeparator + WindowsSandbox.LIBRARY_FALLBACK_DIR;
	}

	// Fields
	private final HashMap<SandboxFlag, Boolean> flagCache;

	// Constructor
	WindowsSandbox() {
		this.flagCache = new HashMap<>();
	}

	@Override
	public void cacheFlags() {
		this.flagCache.put(SandboxFlag.CAPS_LOCK,
				Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK));
	}

	@Override
	protected String getDirectory(final SystemDir dir) {
		return switch (dir) {
			case APPLICATION, SYSTEM_APPLICATION -> WindowsSandbox.getLibraryFallbackDirectory();
			case APPLICATION_SUPPORT, SYSTEM_APPLICATION_SUPPORT -> WindowsSandbox.getLibraryFallbackDirectory()
					+ File.pathSeparator + WindowsSandbox.APP_SUPPORT_FALLBACK_DIR;
			case AUTOSAVED_INFORMATION -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.AUTOSAVE_FALLBACK_DIR;
			case CACHES, SYSTEM_CACHES -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.CACHES_FALLBACK_DIR;
			case DOCUMENTS -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.DOCUMENTS_FALLBACK_DIR;
			case DESKTOP -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.DESKTOP_FALLBACK_DIR;
			case DOWNLOADS -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.DOWNLOADS_FALLBACK_DIR;
			case LIBRARY, SYSTEM_LIBRARY -> WindowsSandbox.getLibraryFallbackDirectory();
			case MOVIES -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.MOVIES_FALLBACK_DIR;
			case MUSIC -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.MUSIC_FALLBACK_DIR;
			case PICTURES -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.PICTURES_FALLBACK_DIR;
			case SHARED_PUBLIC -> WindowsSandbox.getLibraryFallbackDirectory() + File.pathSeparator
					+ WindowsSandbox.SHARED_PUBLIC_FALLBACK_DIR;
			case SYSTEM_USER, USER_HOME -> System.getProperty("user.home"); //$NON-NLS-1$
			default -> WindowsSandbox.getLibraryFallbackDirectory();
		};
	}

	@Override
	public boolean getFlag(final SandboxFlag flag) {
		return this.flagCache.getOrDefault(flag, false);
	}
}
