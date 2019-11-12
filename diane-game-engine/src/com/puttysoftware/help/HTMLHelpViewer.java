package com.puttysoftware.help;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import com.puttysoftware.diane.gui.MainContent;

public final class HTMLHelpViewer {
  // Fields
  private JEditorPane helpContents;
  private final MainContent helpMainContent;
  private final JScrollPane scrollPane;

  // Constructor
  public HTMLHelpViewer(final URL helpPage) {
    this.helpMainContent = new MainContent();
    this.helpMainContent.setLayout(new FlowLayout());
    try {
      this.helpContents = new JEditorPane(helpPage);
    } catch (final Exception e) {
      this.helpContents = new JEditorPane("text/plain", //$NON-NLS-1$
          "An error occurred while fetching the help contents."); //$NON-NLS-1$
    }
    this.helpContents.setEditable(false);
    this.scrollPane = new JScrollPane(this.helpContents);
    this.helpMainContent.add(this.scrollPane);
  }

  // Methods
  public MainContent getHelp() {
    return this.helpMainContent;
  }

  public void setHelpSize(final int horz, final int vert) {
    this.helpContents.setPreferredSize(new Dimension(horz, vert));
    this.scrollPane.setPreferredSize(new Dimension(horz, vert));
  }
}
