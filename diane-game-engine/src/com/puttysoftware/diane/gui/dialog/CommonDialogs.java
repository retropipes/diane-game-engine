package com.puttysoftware.diane.gui.dialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ExecutionException;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.asset.BufferedImageIcon;

public class CommonDialogs {
    // Fields
    private static BufferedImageIcon ICON = null;
    private static String DEFAULT_TITLE = null;
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int DEFAULT_OPTION = -1;
    public static final int CANCEL = -1;

    // Constructor
    private CommonDialogs() {
	// Do nothing
    }

    // Methods
    /**
     * Displays a dialog.
     *
     * @param msg The dialog message.
     */
    public static void showDialog(final String msg) {
	try {
	    GeneralDialog.showDialog(msg, CommonDialogs.DEFAULT_TITLE, CommonDialogs.ICON).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	}
    }

    /**
     * Displays a dialog with a title.
     *
     * @param msg   The dialog message.
     * @param title The dialog title.
     */
    public static void showTitledDialog(final String msg, final String title) {
	try {
	    GeneralDialog.showDialog(msg, title, CommonDialogs.ICON).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	}
    }

    /**
     * Displays an error dialog with the default title.
     *
     * @param msg The dialog message.
     */
    public static void showErrorDialog(final String msg) {
	try {
	    GeneralDialog.showDialog(msg, CommonDialogs.DEFAULT_TITLE, CommonDialogs.ICON).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	}
    }

    /**
     * Displays an error dialog with a title.
     *
     * @param msg   The dialog message.
     * @param title The dialog title.
     */
    public static void showErrorDialog(final String msg, final String title) {
	try {
	    GeneralDialog.showDialog(msg, title, CommonDialogs.ICON).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	}
    }

    /**
     * Displays an input dialog, allowing the user to pick from a list.
     *
     * @param prompt        The input prompt.
     * @param title         The dialog title.
     * @param choices       The list of choices.
     * @param defaultChoice The default choice, which should be one of the list
     *                      entries.
     * @return The choice picked
     */
    public static String showInputDialog(final String prompt, final String title, final String[] choices,
	    final String defaultChoice) {
	try {
	    return ListDialog.showDialog(prompt, title, CommonDialogs.ICON, choices, defaultChoice).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return null;
	}
    }

    /**
     * Displays a text input dialog, allowing the user to enter a value.
     *
     * @param prompt The input prompt.
     * @param title  The dialog title.
     * @return The value the user input.
     */
    public static String showTextInputDialog(final String prompt, final String title) {
	try {
	    return TextInputDialog.showDialog(prompt, title, CommonDialogs.ICON, null).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return null;
	}
    }

    /**
     * Displays a text input dialog, allowing the user to enter a value.
     *
     * @param prompt The input prompt.
     * @param title  The dialog title.
     * @return The value the user input.
     */
    public static String showTextInputDialogWithDefault(final String prompt, final String title,
	    final String defaultValue) {
	try {
	    return TextInputDialog.showDialog(prompt, title, CommonDialogs.ICON, defaultValue).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return null;
	}
    }

    /**
     * Displays a yes/no confirm dialog.
     *
     * @param prompt The confirmation prompt.
     * @param title  The dialog title.
     * @return A JOptionPane constant specifying what the user clicked.
     */
    public static int showConfirmDialog(final String prompt, final String title) {
	try {
	    return InputDialog.showConfirmDialog(prompt, title, CommonDialogs.ICON).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return CommonDialogs.CANCEL;
	}
    }

    /**
     * Displays a yes/no/cancel confirm dialog.
     *
     * @param prompt The confirmation prompt.
     * @param title  The dialog title.
     * @return A JOptionPane constant specifying what the user clicked.
     */
    public static int showYNCConfirmDialog(final String prompt, final String title) {
	try {
	    return InputDialog.showYNCConfirmDialog(prompt, title, CommonDialogs.ICON).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return CommonDialogs.CANCEL;
	}
    }

    public static int showCustomDialog(final String prompt, final String title, final String[] buttonNames) {
	try {
	    return InputDialog.showDialog(prompt, title, CommonDialogs.ICON, buttonNames).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return CommonDialogs.CANCEL;
	}
    }

    public static File showFileOpenDialog(final File dir, final FilenameFilter filter, final String prompt) {
	final String[] choices = dir.list(filter);
	if (choices == null || choices.length == 0) {
	    CommonDialogs.showErrorDialog("No Files To Open!", "Open");
	} else {
	    final String ext = CommonDialogs.getExtension(choices[0]);
	    for (int z = 0; z < choices.length; z++) {
		choices[z] = CommonDialogs.getNameWithoutExtension(choices[z]);
	    }
	    final String value = CommonDialogs.showInputDialog(prompt, "Open", choices, choices[0]);
	    if (value != null) {
		return new File(dir.getAbsolutePath() + File.separator + value + ext);
	    }
	}
	return null;
    }

    public static String showListWithDescDialog(final String labelText, final String title,
	    final String[] possibleValues, final String initialValue, final String descValue,
	    final String... possibleDescriptions) {
	try {
	    return ListWithDescDialog
		    .showDialog(labelText, title, possibleValues, initialValue, descValue, possibleDescriptions).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return null;
	}
    }

    public static int showImageListDialog(final String labelText, final String title,
	    final BufferedImageIcon[] possibleValues, final int initialValue) {
	try {
	    return ImageListDialog.showDialog(labelText, title, possibleValues, initialValue).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return CommonDialogs.CANCEL;
	}
    }

    public static int showImageListWithDescDialog(final String labelText, final String title,
	    final BufferedImageIcon[] possibleValues, final int initialValue, final String descValue,
	    final String... possibleDescriptions) {
	try {
	    return ImageListWithDescDialog
		    .showDialog(labelText, title, possibleValues, initialValue, descValue, possibleDescriptions).get();
	} catch (InterruptedException | ExecutionException e) {
	    Diane.handleError(e);
	    return CommonDialogs.CANCEL;
	}
    }

    public static File showFileSaveDialog(final File dir, final String prompt) {
	final String value = CommonDialogs.showTextInputDialog(prompt, "Save");
	if (value != null) {
	    return new File(dir.getAbsolutePath() + File.separator + value);
	}
	return null;
    }

    /**
     * Sets the default title for dialogs.
     *
     * @param title The default title
     */
    public static void setDefaultTitle(final String title) {
	CommonDialogs.DEFAULT_TITLE = title;
    }

    /**
     * Sets the image to use instead of the default icons.
     *
     * @param icon The image - should be a BufferedImageIcon from the Graphics
     *             library.
     */
    public static void setIcon(final BufferedImageIcon icon) {
	CommonDialogs.ICON = icon;
    }

    private static String getNameWithoutExtension(final String s) {
	String ext = null;
	final int i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(0, i);
	} else {
	    ext = s;
	}
	return ext;
    }

    private static String getExtension(final String s) {
	String ext = null;
	final int i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(i).toLowerCase();
	}
	return ext;
    }
}
