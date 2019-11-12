package com.puttysoftware.help;

import java.awt.BorderLayout;
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

import com.puttysoftware.commondialogs.CommonDialogs;
import com.puttysoftware.diane.gui.GameImage;
import com.puttysoftware.diane.gui.MainContent;
import com.puttysoftware.diane.internal.BufferedImageIcon;
import com.puttysoftware.fileutils.FilenameChecker;

public final class GraphicalHelpViewer {
  // Fields
  private final MainContent helpMainContent;
  private final MainContent choiceMainContent;
  private final JScrollPane scrollPane;

  // Constructor
  public GraphicalHelpViewer(final GameImage[] pictures,
      final String[] descriptions) {
    this.helpMainContent = new MainContent();
    this.helpMainContent.setLayout(new BorderLayout());
    this.choiceMainContent = new MainContent();
    this.scrollPane = new JScrollPane(this.choiceMainContent);
    this.scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.scrollPane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.helpMainContent.add(this.scrollPane, BorderLayout.CENTER);
    this.updateHelp(pictures, descriptions);
  }

  // Methods
  public MainContent getHelp() {
    return this.helpMainContent;
  }

  public void updateHelp(final GameImage[] newImages, final String[] newNames) {
    GameImage[] choices;
    String[] choiceNames;
    JLabel[] choiceArray;
    choices = newImages;
    choiceNames = newNames;
    this.choiceMainContent.removeAll();
    this.choiceMainContent.setLayout(new GridLayout(choices.length, 1));
    choiceArray = new JLabel[choices.length];
    for (int x = 0; x < choices.length; x++) {
      choiceArray[x] = new JLabel(choiceNames[x],
          new BufferedImageIcon(choices[x]), SwingConstants.LEFT);
      choiceArray[x].setOpaque(true);
      this.choiceMainContent.add(choiceArray[x]);
    }
  }

  public void exportHelp() {
    String filename = ""; //$NON-NLS-1$
    String fileOnly = "\\"; //$NON-NLS-1$
    String extension;
    final FileDialog fc = new FileDialog((JFrame) null, "Export Help", //$NON-NLS-1$
        FileDialog.SAVE);
    while (!FilenameChecker.isFilenameOK(fileOnly)) {
      fc.setVisible(true);
      if (fc.getFile() != null && fc.getDirectory() != null) {
        final File file = new File(fc.getDirectory() + fc.getFile());
        extension = GraphicalHelpViewer.getExtension(file);
        filename = file.getAbsolutePath();
        final String dirOnly = fc.getDirectory();
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
              filename = GraphicalHelpViewer.getNameWithoutExtension(file)
                  + ".png"; //$NON-NLS-1$
            }
          } else {
            filename += ".png"; //$NON-NLS-1$
          }
          final MainContent c = this.choiceMainContent;
          final Dimension d = c.getPreferredSize();
          final BufferedImage bi = new BufferedImage(d.width, d.height,
              BufferedImage.TYPE_INT_ARGB);
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

  public void setHelpSize(final int horz, final int vert) {
    this.helpMainContent.setPreferredSize(new Dimension(horz, vert));
    this.scrollPane.setPreferredSize(new Dimension(horz, vert));
  }

  private static String getExtension(final File f) {
    String ext = null;
    final String s = f.getName();
    final int i = s.lastIndexOf('.');
    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(i + 1).toLowerCase();
    }
    return ext;
  }

  private static String getNameWithoutExtension(final File f) {
    String ext = null;
    final String s = f.getAbsolutePath();
    final int i = s.lastIndexOf('.');
    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(0, i);
    } else {
      ext = s;
    }
    return ext;
  }
}
