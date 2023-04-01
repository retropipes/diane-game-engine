/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.fileio.utility.FilenameChecker;
import com.puttysoftware.diane.asset.image.BufferedImageIcon;

public final class GraphicalHelpViewer {
    private static String getExtension(final File f) {
        String ext = null;
        final var s = f.getName();
        final var i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private static String getNameWithoutExtension(final File f) {
        String ext = null;
        final var s = f.getAbsolutePath();
        final var i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(0, i);
        } else {
            ext = s;
        }
        return ext;
    }

    // Fields
    private final Container helpContainer;
    private final Container choiceContainer;
    private final JScrollPane scrollPane;
    private final Color fill;

    // Constructor
    public GraphicalHelpViewer(final BufferedImageIcon[] pictures,
            final String[] descriptions) {
        this.helpContainer = new Container();
        this.helpContainer.setLayout(new BorderLayout());
        this.choiceContainer = new Container();
        this.scrollPane = new JScrollPane(this.choiceContainer);
        this.scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.helpContainer.add(this.scrollPane, BorderLayout.CENTER);
        this.fill = null;
        this.updateHelp(pictures, descriptions);
    }

    public GraphicalHelpViewer(final BufferedImageIcon[] pictures,
            final String[] descriptions, final Color fillColor) {
        this.helpContainer = new Container();
        this.helpContainer.setLayout(new BorderLayout());
        this.choiceContainer = new Container();
        this.scrollPane = new JScrollPane(this.choiceContainer);
        this.scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.helpContainer.add(this.scrollPane, BorderLayout.CENTER);
        this.fill = fillColor;
        this.helpContainer.setBackground(fillColor);
        this.choiceContainer.setBackground(fillColor);
        this.scrollPane.setBackground(fillColor);
        this.updateHelp(pictures, descriptions);
    }

    public void exportHelp() {
        var filename = ""; //$NON-NLS-1$
        var fileOnly = "\\"; //$NON-NLS-1$
        String extension;
        final var fc = new FileDialog((JFrame) null, "Export Help", //$NON-NLS-1$
                FileDialog.SAVE);
        while (!FilenameChecker.isFilenameOK(fileOnly)) {
            fc.setVisible(true);
            if (fc.getFile() != null && fc.getDirectory() != null) {
                final var file = new File(fc.getDirectory() + fc.getFile());
                extension = GraphicalHelpViewer.getExtension(file);
                filename = file.getAbsolutePath();
                final var dirOnly = fc.getDirectory();
                fileOnly = filename.substring(dirOnly.length() + 1);
                if (!FilenameChecker.isFilenameOK(fileOnly)) {
                    CommonDialogs.showErrorDialog(
                            "The file name you entered contains illegal characters.\n" //$NON-NLS-1$
                                    + "These characters are not allowed: /?<>\\:|\"\n" //$NON-NLS-1$
                                    + "Files named con, nul, or prn are illegal, as are files\n" //$NON-NLS-1$
                                    + "named com1 through com9 and lpt1 through lpt9.", //$NON-NLS-1$
                            "Save"); //$NON-NLS-1$
                } else {
                    if (extension != null) {
                        if (!extension.equals("png")) { //$NON-NLS-1$
                            filename = GraphicalHelpViewer
                                    .getNameWithoutExtension(file) + ".png"; //$NON-NLS-1$
                        }
                    } else {
                        filename += ".png"; //$NON-NLS-1$
                    }
                    final var c = this.choiceContainer;
                    final var d = c.getPreferredSize();
                    final var bi = new BufferedImage(d.width,
                            d.height, BufferedImage.TYPE_INT_ARGB);
                    if (this.fill != null) {
                        for (var x = 0; x < d.width; x++) {
                            for (var y = 0; y < d.height; y++) {
                                bi.setRGB(x, y, this.fill.getRGB());
                            }
                        }
                    }
                    c.paintComponents(bi.createGraphics());
                    try {
                        ImageIO.write(bi, "PNG", new File(filename)); //$NON-NLS-1$
                        CommonDialogs.showDialog("Export Successful!"); //$NON-NLS-1$
                    } catch (final IOException io) {
                        CommonDialogs.showDialog("Export Failed!"); //$NON-NLS-1$
                    }
                }
            } else {
                break;
            }
        }
    }

    public Container getHelp() {
        return this.helpContainer;
    }

    public void setHelpSize(final int horz, final int vert) {
        this.helpContainer.setPreferredSize(new Dimension(horz, vert));
        this.scrollPane.setPreferredSize(new Dimension(horz, vert));
    }

    public void updateHelp(final BufferedImageIcon[] newImages,
            final String[] newNames) {
        BufferedImageIcon[] choices;
        String[] choiceNames;
        JLabel[] choiceArray;
        choices = newImages;
        choiceNames = newNames;
        this.choiceContainer.removeAll();
        this.choiceContainer.setLayout(new GridLayout(choices.length, 1));
        choiceArray = new JLabel[choices.length];
        for (var x = 0; x < choices.length; x++) {
            choiceArray[x] = new JLabel(choiceNames[x], choices[x],
                    SwingConstants.LEFT);
            choiceArray[x].setOpaque(true);
            choiceArray[x].setBackground(this.fill);
            this.choiceContainer.add(choiceArray[x]);
        }
    }
}
