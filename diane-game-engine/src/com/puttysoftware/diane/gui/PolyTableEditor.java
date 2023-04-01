/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.polytable.PolyTable;

public final class PolyTableEditor {
	public static final int DEFAULT_MAX_POWER = 6;
	public static final int DEFAULT_PARAMS = 1;
	public static final int DEFAULT_MAX_RANGE = 99;
	private static final String SUFFIX_N = "th"; //$NON-NLS-1$
	private static final String SUFFIX_1 = "st"; //$NON-NLS-1$
	private static final String SUFFIX_2 = "nd"; //$NON-NLS-1$
	private static final String SUFFIX_3 = "rd"; //$NON-NLS-1$
	private static final String ENTRY_PROMPT_PART_1 = " coefficient for "; //$NON-NLS-1$
	private static final String ENTRY_PROMPT_PART_2 = " parameter:"; //$NON-NLS-1$
	private static final String EDITOR_STRING = "PolyTable Editor"; //$NON-NLS-1$
	private static final String EXPERIENCE_EDITOR_STRING = "Experience PolyTable Editor"; //$NON-NLS-1$
	private static final String DIALOG_PROMPT = "Do you want to save this page?"; //$NON-NLS-1$
	private static final String DIALOG_TITLE = "Save?"; //$NON-NLS-1$
	// Fields
	private final PolyTable page;

	// Constructors
	public PolyTableEditor() {
		this.page = new PolyTable(PolyTableEditor.DEFAULT_MAX_POWER, PolyTableEditor.DEFAULT_PARAMS,
				PolyTableEditor.DEFAULT_MAX_RANGE, false);
	}

	public PolyTableEditor(final int maxPower, final int params, final int range, final boolean experience) {
		this.page = new PolyTable(maxPower, params, range, experience);
	}

	public PolyTableEditor(final PolyTable oldPage) {
		this.page = oldPage;
	}

	public PolyTable edit() {
		String editorString;
		if (this.page.isExperience()) {
			editorString = PolyTableEditor.EXPERIENCE_EDITOR_STRING;
		} else {
			editorString = PolyTableEditor.EDITOR_STRING;
		}
		var bad = true;
		boolean inputValid;
		int result, x, y;
		var input = 0.0;
		String xSuffix, ySuffix, rawInput;
		while (bad) {
			for (x = this.page.getMaxPower(); x >= 0; x--) {
				for (y = 1; y <= this.page.getParamCount(); y++) {
					if (x % 100 >= 10 && x % 100 <= 19) {
						xSuffix = PolyTableEditor.SUFFIX_N;
					} else if (x % 10 == 1) {
						xSuffix = PolyTableEditor.SUFFIX_1;
					} else if (x % 10 == 2) {
						xSuffix = PolyTableEditor.SUFFIX_2;
					} else if (x % 10 == 3) {
						xSuffix = PolyTableEditor.SUFFIX_3;
					} else {
						xSuffix = PolyTableEditor.SUFFIX_N;
					}
					if (y % 100 >= 10 && y % 100 <= 19) {
						ySuffix = PolyTableEditor.SUFFIX_N;
					} else if (y % 10 == 1) {
						ySuffix = PolyTableEditor.SUFFIX_1;
					} else if (y % 10 == 2) {
						ySuffix = PolyTableEditor.SUFFIX_2;
					} else if (y % 10 == 3) {
						ySuffix = PolyTableEditor.SUFFIX_3;
					} else {
						ySuffix = PolyTableEditor.SUFFIX_N;
					}
					inputValid = false;
					while (!inputValid) {
						rawInput = CommonDialogs.showTextInputDialogWithDefault(
								x + xSuffix + PolyTableEditor.ENTRY_PROMPT_PART_1 + y + ySuffix
										+ PolyTableEditor.ENTRY_PROMPT_PART_2,
								editorString, Double.toString(this.page.getCoefficient(x, y)));
						try {
							input = Double.parseDouble(rawInput);
							if (input < 0.0) {
								// Input can't be negative
								throw new NumberFormatException();
							}
							inputValid = true;
						} catch (final NumberFormatException nf) {
							// Ignore exception
						} catch (final NullPointerException np) {
							return null;
						}
						if (!inputValid) {
							CommonDialogs.showErrorDialog("The input provided was invalid - please try again.", //$NON-NLS-1$
									editorString);
						}
					}
					this.page.setCoefficient(x, y, input);
				}
			}
			PolyTableViewer.view(this.page);
			result = CommonDialogs.showConfirmDialog(PolyTableEditor.DIALOG_PROMPT, PolyTableEditor.DIALOG_TITLE);
			if (result == CommonDialogs.YES_OPTION) {
				bad = false;
			}
		}
		return this.page;
	}
}
