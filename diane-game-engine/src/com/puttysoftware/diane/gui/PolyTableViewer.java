/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.polytable.PolyTable;

public final class PolyTableViewer {
    // Private constants
    private static final String ENTRY_PROMPT_PART_1 = "Enter value for "; //$NON-NLS-1$
    private static final String ENTRY_PROMPT_PART_2 = "parameter:"; //$NON-NLS-1$
    private static final String SUFFIX_N = "th"; //$NON-NLS-1$
    private static final String SUFFIX_1 = "st"; //$NON-NLS-1$
    private static final String SUFFIX_2 = "nd"; //$NON-NLS-1$
    private static final String SUFFIX_3 = "rd"; //$NON-NLS-1$
    private static final String VIEWER_STRING = "PolyTable Viewer"; //$NON-NLS-1$
    private static final String EXPERIENCE_VIEWER_STRING = "Experience PolyTable Viewer"; //$NON-NLS-1$

    private static void listView(final PolyTable page, final String viewerString) {
        final var lData = page.evaluateToArray();
        final var sData = new String[lData.length];
        for (var x = 0; x < lData.length; x++) {
            sData[x] = Integer.toString(x + 1) + ": " //$NON-NLS-1$
                    + Long.toString(lData[x]);
        }
        CommonDialogs.showInputDialog("List of values:", viewerString, sData, //$NON-NLS-1$
                sData[0]);
    }

    private static void mpValueView(final PolyTable page, final String viewerString) {
        var viewMore = true;
        while (viewMore) {
            final var paramValues = new int[page.getParamCount()];
            for (var x = 0; x < paramValues.length; x++) {
                var valid = false;
                String rawInput;
                var input = 0;
                String suffix;
                if ((x + 1) % 100 >= 10 && (x + 1) % 100 <= 19) {
                    suffix = PolyTableViewer.SUFFIX_N;
                } else if ((x + 1) % 10 == 1) {
                    suffix = PolyTableViewer.SUFFIX_1;
                } else if ((x + 1) % 10 == 2) {
                    suffix = PolyTableViewer.SUFFIX_2;
                } else if ((x + 1) % 10 == 3) {
                    suffix = PolyTableViewer.SUFFIX_3;
                } else {
                    suffix = PolyTableViewer.SUFFIX_N;
                }
                while (!valid) {
                    rawInput = CommonDialogs
                            .showTextInputDialog(PolyTableViewer.ENTRY_PROMPT_PART_1 + (x + 1) + suffix + " " //$NON-NLS-1$
                                    + PolyTableViewer.ENTRY_PROMPT_PART_2, viewerString);
                    try {
                        input = Integer.parseInt(rawInput);
                        if (input < 0) {
                            // Input can't be negative
                            throw new NumberFormatException();
                        }
                        valid = true;
                    } catch (final NumberFormatException | NullPointerException np) {
                        // Ignore exception
                    }
                    if (!valid) {
                        CommonDialogs.showErrorDialog("The input provided was invalid - please try again.", //$NON-NLS-1$
                                viewerString);
                    }
                }
                paramValues[x] = input;
            }
            final var value = page.evaluate(paramValues);
            CommonDialogs.showTitledDialog("Value for the given parameters: " //$NON-NLS-1$
                    + Long.toString(value), viewerString);
            final var returnCode = CommonDialogs.showConfirmDialog("View more values?", //$NON-NLS-1$
                    viewerString);
            if (returnCode == CommonDialogs.YES_OPTION) {
                viewMore = true;
            } else {
                viewMore = false;
            }
        }
    }

    private static void spValueView(final PolyTable page, final String viewerString) {
        var viewMore = true;
        while (viewMore) {
            var paramValue = 0;
            var valid = false;
            String rawInput;
            var input = 0;
            while (!valid) {
                rawInput = CommonDialogs.showTextInputDialog(
                        PolyTableViewer.ENTRY_PROMPT_PART_1 + PolyTableViewer.ENTRY_PROMPT_PART_2, viewerString);
                try {
                    input = Integer.parseInt(rawInput);
                    if (input < 0) {
                        // Input can't be negative
                        throw new NumberFormatException();
                    }
                    valid = true;
                } catch (final NumberFormatException | NullPointerException np) {
                    // Ignore exception
                }
                if (!valid) {
                    CommonDialogs.showErrorDialog("The input provided was invalid - please try again.", //$NON-NLS-1$
                            viewerString);
                }
            }
            paramValue = input;
            final var value = page.evaluate(paramValue);
            CommonDialogs.showTitledDialog("Value: " + Long.toString(value), //$NON-NLS-1$
                    viewerString);
            final var returnCode = CommonDialogs.showConfirmDialog("View more values?", //$NON-NLS-1$
                    viewerString);
            if (returnCode == CommonDialogs.YES_OPTION) {
                viewMore = true;
            } else {
                viewMore = false;
            }
        }
    }

    public static void view(final PolyTable page) {
        String viewerString;
        if (page.isExperience()) {
            viewerString = PolyTableViewer.EXPERIENCE_VIEWER_STRING;
        } else {
            viewerString = PolyTableViewer.VIEWER_STRING;
        }
        if (page.getParamCount() > 1) {
            PolyTableViewer.mpValueView(page, viewerString);
        } else {
            var viewAsArray = true;
            final var returnCode = CommonDialogs.showConfirmDialog("View as a list, or as single values?", //$NON-NLS-1$
                    viewerString);
            if (returnCode == CommonDialogs.YES_OPTION) {
                viewAsArray = true;
            } else {
                viewAsArray = false;
            }
            if (viewAsArray) {
                PolyTableViewer.listView(page, viewerString);
            } else {
                PolyTableViewer.spValueView(page, viewerString);
            }
        }
    }

    // Private constructor
    private PolyTableViewer() {
        // Do nothing
    }
}
