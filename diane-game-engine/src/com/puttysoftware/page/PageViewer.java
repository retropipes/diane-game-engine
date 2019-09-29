package com.puttysoftware.page;

import com.puttysoftware.commondialogs.CommonDialogs;

public final class PageViewer {
    // Private constants
    private static final String ENTRY_PROMPT_PART_1 = "Enter value for "; //$NON-NLS-1$
    private static final String ENTRY_PROMPT_PART_2 = "parameter:"; //$NON-NLS-1$
    private static final String SUFFIX_N = "th"; //$NON-NLS-1$
    private static final String SUFFIX_1 = "st"; //$NON-NLS-1$
    private static final String SUFFIX_2 = "nd"; //$NON-NLS-1$
    private static final String SUFFIX_3 = "rd"; //$NON-NLS-1$
    private static final String VIEWER_STRING = "Page Viewer"; //$NON-NLS-1$
    private static final String EXPERIENCE_VIEWER_STRING = "Experience Page Viewer"; //$NON-NLS-1$

    // Private constructor
    private PageViewer() {
        // Do nothing
    }

    // Methods
    public static void view(Page page) {
        String viewerString;
        if (page.isExperience()) {
            viewerString = PageViewer.EXPERIENCE_VIEWER_STRING;
        } else {
            viewerString = PageViewer.VIEWER_STRING;
        }
        if (page.getParamCount() > 1) {
            PageViewer.mpValueView(page, viewerString);
        } else {
            boolean viewAsArray = true;
            int returnCode = CommonDialogs.showConfirmDialog(
                    "View as a list, or as single values?", viewerString); //$NON-NLS-1$
            if (returnCode == CommonDialogs.YES_OPTION) {
                viewAsArray = true;
            } else {
                viewAsArray = false;
            }
            if (viewAsArray) {
                PageViewer.listView(page, viewerString);
            } else {
                PageViewer.spValueView(page, viewerString);
            }
        }
    }

    private static void listView(Page page, String viewerString) {
        long[] lData = page.evaluateToArray();
        String[] sData = new String[lData.length];
        for (int x = 0; x < lData.length; x++) {
            sData[x] = Integer.valueOf(x + 1).toString() + ": " //$NON-NLS-1$
                    + Long.valueOf(lData[x]).toString();
        }
        CommonDialogs.showInputDialog("List of values:", viewerString, sData, //$NON-NLS-1$
                sData[0]);
    }

    private static void mpValueView(Page page, String viewerString) {
        boolean viewMore = true;
        while (viewMore) {
            int[] paramValues = new int[page.getParamCount()];
            for (int x = 0; x < paramValues.length; x++) {
                boolean valid = false;
                String rawInput;
                int input = 0;
                String suffix;
                if ((x + 1) % 100 >= 10 && (x + 1) % 100 <= 19) {
                    suffix = SUFFIX_N;
                } else if ((x + 1) % 10 == 1) {
                    suffix = SUFFIX_1;
                } else if ((x + 1) % 10 == 2) {
                    suffix = SUFFIX_2;
                } else if ((x + 1) % 10 == 3) {
                    suffix = SUFFIX_3;
                } else {
                    suffix = SUFFIX_N;
                }
                while (!valid) {
                    rawInput = CommonDialogs
                            .showTextInputDialog(
                                    ENTRY_PROMPT_PART_1 + (x + 1) + suffix + " " //$NON-NLS-1$
                                            + ENTRY_PROMPT_PART_2,
                                    viewerString);
                    try {
                        input = Integer.parseInt(rawInput);
                        if (input < 0) {
                            // Input can't be negative
                            throw new NumberFormatException();
                        }
                        valid = true;
                    } catch (NumberFormatException nf) {
                        // Ignore exception
                    } catch (NullPointerException np) {
                        // Ignore exception
                    }
                    if (!valid) {
                        CommonDialogs.showErrorDialog(
                                "The input provided was invalid - please try again.", //$NON-NLS-1$
                                viewerString);
                    }
                }
                paramValues[x] = input;
            }
            long value = page.evaluate(paramValues);
            CommonDialogs.showTitledDialog("Value for the given parameters: " //$NON-NLS-1$
                    + Long.valueOf(value).toString(), viewerString);
            int returnCode = CommonDialogs
                    .showConfirmDialog("View more values?", viewerString); //$NON-NLS-1$
            if (returnCode == CommonDialogs.YES_OPTION) {
                viewMore = true;
            } else {
                viewMore = false;
            }
        }
    }

    private static void spValueView(Page page, String viewerString) {
        boolean viewMore = true;
        while (viewMore) {
            int paramValue = 0;
            boolean valid = false;
            String rawInput;
            int input = 0;
            while (!valid) {
                rawInput = CommonDialogs.showTextInputDialog(
                        ENTRY_PROMPT_PART_1 + ENTRY_PROMPT_PART_2,
                        viewerString);
                try {
                    input = Integer.parseInt(rawInput);
                    if (input < 0) {
                        // Input can't be negative
                        throw new NumberFormatException();
                    }
                    valid = true;
                } catch (NumberFormatException nf) {
                    // Ignore exception
                } catch (NullPointerException np) {
                    // Ignore exception
                }
                if (!valid) {
                    CommonDialogs.showErrorDialog(
                            "The input provided was invalid - please try again.", //$NON-NLS-1$
                            viewerString);
                }
            }
            paramValue = input;
            long value = page.evaluate(paramValue);
            CommonDialogs.showTitledDialog(
                    "Value: " + Long.valueOf(value).toString(), viewerString); //$NON-NLS-1$
            int returnCode = CommonDialogs
                    .showConfirmDialog("View more values?", viewerString); //$NON-NLS-1$
            if (returnCode == CommonDialogs.YES_OPTION) {
                viewMore = true;
            } else {
                viewMore = false;
            }
        }
    }
}
