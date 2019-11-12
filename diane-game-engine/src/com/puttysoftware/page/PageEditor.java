package com.puttysoftware.page;

import com.puttysoftware.commondialogs.CommonDialogs;

public final class PageEditor {
    // Fields
    private Page page;
    public static final int DEFAULT_MAX_POWER = 6;
    public static final int DEFAULT_PARAMS = 1;
    public static final int DEFAULT_MAX_RANGE = 99;
    private static final String SUFFIX_N = "th"; //$NON-NLS-1$
    private static final String SUFFIX_1 = "st"; //$NON-NLS-1$
    private static final String SUFFIX_2 = "nd"; //$NON-NLS-1$
    private static final String SUFFIX_3 = "rd"; //$NON-NLS-1$
    private static final String ENTRY_PROMPT_PART_1 = " coefficient for "; //$NON-NLS-1$
    private static final String ENTRY_PROMPT_PART_2 = " parameter:"; //$NON-NLS-1$
    private static final String EDITOR_STRING = "Page Editor"; //$NON-NLS-1$
    private static final String EXPERIENCE_EDITOR_STRING = "Experience Page Editor"; //$NON-NLS-1$
    private static final String DIALOG_PROMPT = "Do you want to save this page?"; //$NON-NLS-1$
    private static final String DIALOG_TITLE = "Save?"; //$NON-NLS-1$

    // Constructors
    public PageEditor() {
        this.page = new Page(PageEditor.DEFAULT_MAX_POWER,
                PageEditor.DEFAULT_PARAMS, PageEditor.DEFAULT_MAX_RANGE, false);
    }

    public PageEditor(int maxPower, int params, int range, boolean experience) {
        this.page = new Page(maxPower, params, range, experience);
    }

    public PageEditor(Page oldPage) {
        this.page = oldPage;
    }

    // Methods
    public Page edit() {
        String editorString;
        if (this.page.isExperience()) {
            editorString = PageEditor.EXPERIENCE_EDITOR_STRING;
        } else {
            editorString = PageEditor.EDITOR_STRING;
        }
        boolean bad = true;
        boolean inputValid;
        int result, x, y;
        double input = 0.0;
        String xSuffix, ySuffix, rawInput;
        while (bad) {
            for (x = this.page.getMaxPower(); x >= 0; x--) {
                for (y = 1; y <= this.page.getParamCount(); y++) {
                    if (x % 100 >= 10 && x % 100 <= 19) {
                        xSuffix = SUFFIX_N;
                    } else if (x % 10 == 1) {
                        xSuffix = SUFFIX_1;
                    } else if (x % 10 == 2) {
                        xSuffix = SUFFIX_2;
                    } else if (x % 10 == 3) {
                        xSuffix = SUFFIX_3;
                    } else {
                        xSuffix = SUFFIX_N;
                    }
                    if (y % 100 >= 10 && y % 100 <= 19) {
                        ySuffix = SUFFIX_N;
                    } else if (y % 10 == 1) {
                        ySuffix = SUFFIX_1;
                    } else if (y % 10 == 2) {
                        ySuffix = SUFFIX_2;
                    } else if (y % 10 == 3) {
                        ySuffix = SUFFIX_3;
                    } else {
                        ySuffix = SUFFIX_N;
                    }
                    inputValid = false;
                    while (!inputValid) {
                        rawInput = CommonDialogs.showTextInputDialogWithDefault(
                                x + xSuffix + ENTRY_PROMPT_PART_1 + y + ySuffix
                                        + ENTRY_PROMPT_PART_2,
                                editorString,
                                Double.valueOf(this.page.getCoefficient(x, y))
                                        .toString());
                        try {
                            input = Double.parseDouble(rawInput);
                            if (input < 0.0) {
                                // Input can't be negative
                                throw new NumberFormatException();
                            }
                            inputValid = true;
                        } catch (NumberFormatException nf) {
                            // Ignore exception
                        } catch (NullPointerException np) {
                            return null;
                        }
                        if (!inputValid) {
                            CommonDialogs.showErrorDialog(
                                    "The input provided was invalid - please try again.", //$NON-NLS-1$
                                    editorString);
                        }
                    }
                    this.page.setCoefficient(x, y, input);
                }
            }
            PageViewer.view(this.page);
            result = CommonDialogs.showConfirmDialog(DIALOG_PROMPT,
                    DIALOG_TITLE);
            if (result == CommonDialogs.YES_OPTION) {
                bad = false;
            }
        }
        return this.page;
    }
}
