/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.gui.picker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.puttysoftware.diane.asset.BufferedImageIcon;

public final class StackedPicturePicker {
    private class EventHandler implements ActionListener {
	public EventHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    final var cmd = e.getActionCommand();
	    // A radio button
	    StackedPicturePicker.this.index = Integer.parseInt(cmd);
	}
    }

    /**
     * @version 2.0.0
     */
    // Fields
    private BufferedImageIcon[] choices;
    private JLabel[] choiceArray;
    private final JPanel pickerJPanel;
    private final ButtonGroup radioGroup;
    private JRadioButton[] radioButtons;
    int index;
    private Color savedCRCColor;
    private Color savedCHColor;
    private final EventHandler handler;
    private final int stackCount;
    private final int imageSize;

    // Constructors
    public StackedPicturePicker(final BufferedImageIcon[] pictures, final boolean[] enabled, final int newStackCount,
	    final int placeholderSize) {
	this.imageSize = placeholderSize;
	this.stackCount = newStackCount;
	this.handler = new EventHandler();
	this.radioGroup = new ButtonGroup();
	this.pickerJPanel = new JPanel();
	this.savedCHColor = this.pickerJPanel.getBackground();
	this.updatePicker(pictures, enabled);
	this.index = 0;
	this.savedCRCColor = this.pickerJPanel.getBackground();
    }

    public StackedPicturePicker(final BufferedImageIcon[] pictures, final boolean[] enabled, final Color choiceColor,
	    final int newStackCount, final int placeholderSize) {
	this.imageSize = placeholderSize;
	this.stackCount = newStackCount;
	this.handler = new EventHandler();
	this.radioGroup = new ButtonGroup();
	this.pickerJPanel = new JPanel();
	this.savedCHColor = choiceColor;
	this.updatePicker(pictures, enabled);
	this.index = 0;
	this.savedCRCColor = this.pickerJPanel.getBackground();
    }

    public void changePickerColor(final Color c) {
	this.pickerJPanel.setBackground(c);
	for (var x = 0; x < this.choiceArray.length; x++) {
	    this.choiceArray[x].setBackground(c);
	    this.radioButtons[x].setBackground(c);
	}
	// Update saved colors
	this.savedCRCColor = c;
	this.savedCHColor = c;
    }

    public void disablePicker() {
	this.pickerJPanel.setEnabled(false);
	this.pickerJPanel.setBackground(Color.gray);
	for (final JRadioButton radioButton : this.radioButtons) {
	    radioButton.setEnabled(false);
	}
    }

    public void enablePicker() {
	this.pickerJPanel.setEnabled(true);
	this.pickerJPanel.setBackground(this.savedCRCColor);
	for (final JRadioButton radioButton : this.radioButtons) {
	    radioButton.setEnabled(true);
	}
    }

    /**
     *
     * @return the index of the picture picked
     */
    public int getPicked() {
	return this.index;
    }

    // Methods
    public JPanel getPicker() {
	return this.pickerJPanel;
    }

    public void selectLastPickedChoice(final int lastPicked) {
	this.radioButtons[lastPicked].setSelected(true);
    }

    public void updatePicker(final BufferedImageIcon[] newImages, final boolean[] enabled) {
	this.choices = newImages;
	this.pickerJPanel.removeAll();
	var rows = this.choices.length / (this.stackCount / 2);
	final var extra = this.choices.length % (this.stackCount / 2);
	if (extra != 0) {
	    rows += 2;
	}
	this.radioButtons = new JRadioButton[this.choices.length];
	this.choiceArray = new JLabel[this.choices.length];
	this.pickerJPanel.setLayout(new GridLayout(0, this.stackCount));
	var picCounter = 0;
	var radioCounter = 0;
	var rowCounter = 0;
	for (var x = 0; x < rows; x++) {
	    for (var y = 0; y < this.stackCount; y++) {
		if (picCounter < this.choices.length) {
		    this.choiceArray[picCounter] = new JLabel("", //$NON-NLS-1$
			    this.choices[picCounter], SwingConstants.LEFT);
		    this.choiceArray[picCounter].setOpaque(true);
		    this.choiceArray[picCounter].setBackground(this.savedCHColor);
		    this.pickerJPanel.add(this.choiceArray[picCounter]);
		} else if (rowCounter == rows - 2) {
		    // Add spacer
		    final var spacer = new JLabel("", //$NON-NLS-1$
			    new BufferedImageIcon(this.imageSize, this.savedCHColor), SwingConstants.LEFT);
		    this.pickerJPanel.add(spacer);
		}
		picCounter++;
	    }
	    rowCounter++;
	    for (var y = 0; y < this.stackCount; y++) {
		if (radioCounter < this.choices.length) {
		    this.radioButtons[radioCounter] = new JRadioButton();
		    this.radioButtons[radioCounter].setHorizontalAlignment(SwingConstants.CENTER);
		    this.radioButtons[radioCounter].setOpaque(true);
		    this.radioButtons[radioCounter].setBackground(this.savedCHColor);
		    this.radioButtons[radioCounter].setActionCommand(Integer.toString(radioCounter));
		    this.radioGroup.add(this.radioButtons[radioCounter]);
		    this.radioButtons[radioCounter].addActionListener(this.handler);
		    this.radioButtons[x].setEnabled(enabled[x]);
		    this.pickerJPanel.add(this.radioButtons[radioCounter]);
		} else if (rowCounter == rows - 1) {
		    // Add spacer
		    final var spacer = new JLabel("", //$NON-NLS-1$
			    new BufferedImageIcon(this.imageSize, this.savedCHColor), SwingConstants.LEFT);
		    this.pickerJPanel.add(spacer);
		}
		radioCounter++;
	    }
	    rowCounter++;
	}
	for (var x = 0; x < this.choices.length; x++) {
	    if (enabled[x]) {
		this.radioButtons[x].setSelected(true);
		this.index = x;
		break;
	    }
	}
    }

    public void updatePickerLayout(final int maxHeight) {
	final var newPreferredWidth = this.pickerJPanel.getLayout().preferredLayoutSize(this.pickerJPanel).width;
	final var newPreferredHeight = Math.min(maxHeight,
		this.pickerJPanel.getLayout().preferredLayoutSize(this.pickerJPanel).height);
	this.pickerJPanel.setPreferredSize(new Dimension(newPreferredWidth, newPreferredHeight));
    }
}
