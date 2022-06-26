package com.puttysoftware.diane.locale;

import java.util.Locale;
import java.util.ResourceBundle;

public final class Translations {
    private static final String RESOURCES = "asset.locale.Translations"; //$NON-NLS-1$
    private static final String PLACEHOLDER = "$"; //$NON-NLS-1$
    private static Locale ACTIVE = Locale.getDefault();

    // Class contains only static methods.
    private Translations() {
    }

    public static String load(Strings item) {
	return ResourceBundle.getBundle(Translations.RESOURCES, Translations.ACTIVE).getString(item.toString());
    }

    public static String load(Strings item, String... replacements) {
	String result = ResourceBundle.getBundle(Translations.RESOURCES, Translations.ACTIVE)
		.getString(item.toString());
	for (int x = 0; x < replacements.length; x++) {
	    result = result.replace(Translations.PLACEHOLDER + x, replacements[x]);
	}
	return result;
    }

    public static void setActiveLanguage(final Locale newActive) {
	Translations.ACTIVE = newActive;
    }
}
