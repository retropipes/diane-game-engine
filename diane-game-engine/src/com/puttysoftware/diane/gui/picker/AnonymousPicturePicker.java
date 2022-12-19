/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.gui.picker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.puttysoftware.diane.asset.BufferedImageIcon;

public final class AnonymousPicturePicker {
    private class EventHandler implements ActionListener {
	public EventHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    final var cmd = e.getActionCommand();
	    // A radio button
	    AnonymousPicturePicker.this.index = Integer.parseInt(cmd);
	}
    }

    /**
     * @version 1.0.0
     */
    // Fields
    private BufferedImageIcon[] choices;
    private JLabel[] choiceArray;
    private final JPanel pickerJPanel;
    private final JPanel choiceJPanel;
    private final JPanel radioJPanel;
    private final JPanel choiceRadioJPanel;
    private final ButtonGroup radioGroup;
    private JRadioButton[] radioButtons;
    private final JScrollPane scrollPane;
    int index;
    private Color savedSPColor;
    private Color savedPCColor;
    private Color savedCCColor;
    private Color savedRCColor;
    private Color savedCRCColor;
    private Color savedCHColor;
    private final EventHandler handler;

    // Constructors
    public AnonymousPicturePicker(final BufferedImageIcon[] pictures, final boolean[] enabled) {
	this.handler = new EventHandler();
	this.pickerJPanel = new JPanel();
	this.pickerJPanel.setLayout(new BorderLayout());
	this.choiceJPanel = new JPanel();
	this.radioJPanel = new JPanel();
	this.radioGroup = new ButtonGroup();
	this.choiceRadioJPanel = new JPanel();
	this.choiceRadioJPanel.setLayout(new BorderLayout());
	this.choiceRadioJPanel.add(this.radioJPanel, BorderLayout.WEST);
	this.choiceRadioJPanel.add(this.choiceJPanel, BorderLayout.CENTER);
	this.scrollPane = new JScrollPane(this.choiceRadioJPanel);
	this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	this.pickerJPanel.add(this.scrollPane, BorderLayout.CENTER);
	this.updatePicker(pictures, enabled);
	this.index = 0;
	this.savedSPColor = this.scrollPane.getBackground();
	this.savedPCColor = this.pickerJPanel.getBackground();
	this.savedCCColor = this.choiceJPanel.getBackground();
	this.savedRCColor = this.radioJPanel.getBackground();
	this.savedCRCColor = this.choiceRadioJPanel.getBackground();
	this.savedCHColor = this.scrollPane.getBackground();
    }

    public AnonymousPicturePicker(final BufferedImageIcon[] pictures, final boolean[] enabled,
	    final Color choiceColor) {
	this.handler = new EventHandler();
	this.pickerJPanel = new JPanel();
	this.pickerJPanel.setLayout(new BorderLayout());
	this.choiceJPanel = new JPanel();
	this.radioJPanel = new JPanel();
	this.radioGroup = new ButtonGroup();
	this.choiceRadioJPanel = new JPanel();
	this.choiceRadioJPanel.setLayout(new BorderLayout());
	this.choiceRadioJPanel.add(this.radioJPanel, BorderLayout.WEST);
	this.choiceRadioJPanel.add(this.choiceJPanel, BorderLayout.CENTER);
	this.scrollPane = new JScrollPane(this.choiceRadioJPanel);
	this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	this.pickerJPanel.add(this.scrollPane, BorderLayout.CENTER);
	this.updatePicker(pictures, enabled);
	this.index = 0;
	this.savedSPColor = this.scrollPane.getBackground();
	this.savedPCColor = this.pickerJPanel.getBackground();
	this.savedCCColor = this.choiceJPanel.getBackground();
	this.savedRCColor = this.radioJPanel.getBackground();
	this.savedCRCColor = this.choiceRadioJPanel.getBackground();
	this.savedCHColor = choiceColor;
    }

    public void changePickerColor(final Color c) {
	this.pickerJPanel.setBackground(c);
	this.choiceJPanel.setBackground(c);
	this.radioJPanel.setBackground(c);
	this.choiceRadioJPanel.setBackground(c);
	this.scrollPane.setBackground(c);
	for (var x = 0; x < this.choiceArray.length; x++) {
	    this.choiceArray[x].setBackground(c);
	    this.radioButtons[x].setBackground(c);
	}
	// Update saved colors
	this.savedSPColor = c;
	this.savedPCColor = c;
	this.savedCCColor = c;
	this.savedRCColor = c;
	this.savedCRCColor = c;
	this.savedCHColor = c;
    }

    public void disablePicker() {
	this.pickerJPanel.setEnabled(false);
	this.pickerJPanel.setBackground(Color.gray);
	this.choiceJPanel.setBackground(Color.gray);
	this.radioJPanel.setBackground(Color.gray);
	this.choiceRadioJPanel.setBackground(Color.gray);
	this.scrollPane.setBackground(Color.gray);
	for (final JRadioButton radioButton : this.radioButtons) {
	    radioButton.setEnabled(false);
	}
    }

    public void enablePicker() {
	this.pickerJPanel.setEnabled(true);
	this.pickerJPanel.setBackground(this.savedPCColor);
	this.choiceJPanel.setBackground(this.savedCCColor);
	this.radioJPanel.setBackground(this.savedRCColor);
	this.choiceRadioJPanel.setBackground(this.savedCRCColor);
	this.scrollPane.setBackground(this.savedSPColor);
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
	this.choiceJPanel.removeAll();
	this.radioJPanel.removeAll();
	this.radioButtons = new JRadioButton[this.choices.length];
	this.choiceJPanel.setLayout(new GridLayout(this.choices.length, 1));
	this.radioJPanel.setLayout(new GridLayout(this.choices.length, 1));
	this.choiceArray = new JLabel[this.choices.length];
	for (var x = 0; x < this.choices.length; x++) {
	    this.choiceArray[x] = new JLabel("", this.choices[x], //$NON-NLS-1$
		    SwingConstants.LEFT);
	    this.choiceArray[x].setOpaque(true);
	    this.choiceArray[x].setBackground(this.savedCHColor);
	    this.choiceJPanel.add(this.choiceArray[x]);
	    this.radioButtons[x] = new JRadioButton();
	    this.radioButtons[x].setOpaque(true);
	    this.radioButtons[x].setBackground(this.savedCHColor);
	    this.radioButtons[x].setActionCommand(Integer.toString(x));
	    this.radioGroup.add(this.radioButtons[x]);
	    this.radioButtons[x].addActionListener(this.handler);
	    this.radioButtons[x].setEnabled(enabled[x]);
	    this.radioJPanel.add(this.radioButtons[x]);
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
	final var newPreferredWidth = this.pickerJPanel.getLayout().preferredLayoutSize(this.pickerJPanel).width
		+ this.scrollPane.getVerticalScrollBar().getWidth();
	final var newPreferredHeight = Math.min(maxHeight,
		this.pickerJPanel.getLayout().preferredLayoutSize(this.pickerJPanel).height);
	this.pickerJPanel.setPreferredSize(new Dimension(newPreferredWidth, newPreferredHeight));
    }
}
