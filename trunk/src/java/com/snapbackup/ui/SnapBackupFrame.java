////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// SnapBackupFrame.java                                                       //
//                                                                            //
// GNU General Public License:                                                //
// This program is free software; you can redistribute it and/or modify it    //
// under the terms of the GNU General Public License as published by the      //
// Free Software Foundation; either version 2 of the License, or (at your     //
// option) any later version.                                                 //
//                                                                            //
// This program is distributed in the hope that it will be useful, but        //
// WITHOUT ANY WARRANTY; without even the implied warranty of                 //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                       //
//                                                                            //
// See the GNU General Public License at http://www.gnu.org for more          //
// details.                                                                   //
//                                                                            //
// Copyright (c) 2009 Center Key Software                                     //
// Snap Backup is a trademark of Dem Pilafian                                 //
// http://www.snapbackup.com                                                  //
//                                                                            //
// Snap Backup Frame:                                                         //
//    This object is the base UI frame and UI widgets for the main            //
//    application window.                                                     //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.snapbackup.business.DataModel;
import com.snapbackup.ui.about.AboutDialog;
import com.snapbackup.ui.filter.FilterDialog;
import com.snapbackup.ui.prefexport.ExportDialog;
//import com.snapbackup.ui.importsettings.ImportSettingsDialog;
import com.snapbackup.ui.options.Options;
import com.snapbackup.ui.options.OptionsDialog;
import com.snapbackup.ui.userguide.UserGuideDialog;
import com.snapbackup.utilities.settings.AppProperties;
import com.snapbackup.utilities.settings.SystemAttributes;
import com.snapbackup.utilities.settings.UserPreferences;
import com.snapbackup.utilities.string.Str;

public class SnapBackupFrame extends JFrame {

   private UIProperties ui = UIProperties.current;

   //Define Menu Controls
   private JMenuBar    menuBar =           new JMenuBar();
   private JMenu       fileMenuGroup =     new JMenu(ui.menuGroupFile);
   private JMenu       languagesMenuItem = new JMenu(ui.menuItemLangs);
   private JRadioButtonMenuItem languagesMenuItemButtonShow =
                                           new JRadioButtonMenuItem(ui.menuItemLangsShow);
   private JRadioButtonMenuItem languagesMenuItemButtonHide =
                                           new JRadioButtonMenuItem(ui.menuItemLangsHide);
   private ButtonGroup languagesGroup =    new ButtonGroup();
   private JMenu       filtersMenuItem =   new JMenu(ui.menuItemFilters);
   private JRadioButtonMenuItem filtersMenuItemButtonOn =
                                           new JRadioButtonMenuItem(ui.menuItemFiltersOn);
   private JRadioButtonMenuItem filtersMenuItemButtonOff =
                                           new JRadioButtonMenuItem(ui.menuItemFiltersOff);
   private ButtonGroup filtersGroup =      new ButtonGroup();
   private JMenu       profilesMenuItem =  new JMenu(ui.menuItemProfiles);
   private JRadioButtonMenuItem profilesMenuItemButtonOn =
                                           new JRadioButtonMenuItem(ui.menuItemProfilesOn);
   private JRadioButtonMenuItem profilesMenuItemButtonOff =
                                           new JRadioButtonMenuItem(ui.menuItemProfilesOff);
   private ButtonGroup profilesGroup =     new ButtonGroup();
   private JMenu       skinMenuItem =      new JMenu(ui.menuItemSkin);
   private JMenuItem   exportMenuItem =    new JMenuItem(ui.menuItemExport);
   private JMenuItem   importMenuItem =    new JMenuItem(ui.menuItemImport);
   private JMenuItem   optionsMenuItem =   new JMenuItem(ui.menuItemOptions);
   private JMenuItem   exitMenuItem =      new JMenuItem(ui.menuItemExit);
   private JMenu       helpMenuGroup =     new JMenu(ui.menuGroupHelp);
   private JMenuItem   guideMenuItem =     new JMenuItem(ui.menuItemGuide);
   private JMenuItem   aboutMenuItem =     new JMenuItem(ui.menuItemAbout);

   //Define Base
   private JPanel  basePanel =          new JPanel();

   //Define Header Panels
   private JPanel   headerPanel =     new JPanel();
   private JPanel   titlePanel =      new JPanel();  //holds title & source
   private JLabel   titleLabel =      new JLabel(ui.header);
   private int      maxFlagsPerRows = 6;
   private int      numFlagRows =     (SystemAttributes.localeCodes.length - 1) / maxFlagsPerRows + 1;
   private int      flagsPerRow =     (SystemAttributes.localeCodes.length - 1) / numFlagRows + 1;
   private JPanel[] langFlagsPanels = new JPanel[numFlagRows];
   private JPanel   iconPanel =       new JPanel();  //holds icon & profiles
   private JLabel   iconLabel =       new JLabel(Icons.snapBackupIcon);

   //Define Profiles Controls
   private JPanel     profilesPanel =        new JPanel();
   private JPanel     profilesInnerPanel =   new JPanel();
   private JPanel     profilesButtonPanel =  new JPanel();
   private JLabel     profilesPromptLabel =  new JLabel(ui.profilesPrompt);
   private JTextField profilesCurrentTextField = new JTextField();  //hidden field
   private JComboBox  profilesDropDown =     new JComboBox(DataModel.getProfilesNames());  //note: holds user data
   private JButton    profilesAddButton =    new JButton(ui.profilesNew);
   private JButton    profilesDeleteButton = new JButton(ui.profilesDelete);

   //Define Source (Zip List) Controls
   private JPanel       srcPanel =             new JPanel();
   private JPanel       srcPanelButtons =      new JPanel();
   private JLabel       srcPromptLabel =       new JLabel(ui.srcPrompt);
   private final DefaultListModel srcZipListModel = new DefaultListModel();
   private JList        srcZipList =           new JList(srcZipListModel);
   private JScrollPane  srcZipListScrollPane = new JScrollPane();
   private JButton      srcAddFileButton =     new JButton(ui.srcAddFile);
   private JButton      srcAddFolderButton =   new JButton(ui.srcAddFolder);
   private JButton      srcRemoveButton =      new JButton(ui.srcRemove);
   private JButton      srcFilterButton =      new JButton(ui.srcFilter);

   //Define Tip
   private JLabel tipLabel = new JLabel(ui.tip);

   //Define Destination (Backup & Archive) Controls
   private JPanel       destPanel =                 new JPanel(new GridBagLayout());
   private JLabel       destBackupPromptLabel =     new JLabel(ui.destBackupPrompt);
   private JTextField   destBackupDirTextField =    new JTextField(UIProperties.srcDirCols);
   private JButton      destBackupChooserButton =   new JButton(Icons.folderIcon);
   private JLabel       destBackupNamePromptLabel = new JLabel(ui.destBackupNamePrompt);
   private JTextField   destBackupNameTextField =   new JTextField(UIProperties.srcNameCols);
   private JLabel       destBackupTagLabel =        new JLabel(ui.destBackupTag);
   private JLabel       destBackupPathLabel =       new JLabel();
   private JCheckBox    destArchivePromptCheckBox = new JCheckBox(ui.destArchivePrompt);
   private JTextField   destArchiveDirTextField =   new JTextField(UIProperties.archiveDirCols);
   private JButton      destArchiveChooserButton =  new JButton(Icons.folderIcon);
   private JLabel       destArchiveTagLabel =       new JLabel(ui.destArchiveTag);
   private JLabel       destArchivePathLabel =      new JLabel();

   //Define Log Controls
   private JPanel      logPanel =      new JPanel();
   private JScrollPane logScrollPane = new JScrollPane();
   private JTextArea   logTextArea =   new JTextArea(UIProperties.logMinRows, UIProperties.logCols);

   //Define Button Controls
   private JPanel  buttonPanel =    new JPanel();
   private JButton saveButton =     new JButton(ui.buttonSave);
   private JButton resetButton =    new JButton(ui.buttonReset);
   private JButton doBackupButton = new JButton(ui.buttonDoBackup);
   private JButton exitButton =     new JButton(ui.buttonExit);

   JButton[] buttonList = { srcAddFileButton, srcAddFolderButton, srcRemoveButton, srcFilterButton,
         profilesAddButton, profilesDeleteButton, saveButton, resetButton, doBackupButton, exitButton };

   public static SnapBackupFrame current;

   public SnapBackupFrame() {
      current = this;
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //setTitle(UIProperties.appTitle);
      setTitle(ui.appTitle);
      configureContols();
      addContols();
      setupMenuCallbacks();
      UIUtilities.addFastKeys(menuBar);
      setupCallbacks();
      pack();
      destPanel.setMaximumSize(new Dimension(100000, destPanel.getSize().height));
      doBackupButton.grabFocus();
      }

   void popupMsg(String msg) {
      JOptionPane.showMessageDialog(null, msg);	
      }

   private boolean switchLocaleCodes(String codeA, String codeB, Locale currentLocale) {
      Locale a = new Locale(codeA);
      Locale b = new Locale(codeB);
      return b.equals(currentLocale) || (!a.equals(currentLocale) &&
         a.getDisplayLanguage(currentLocale).compareTo(b.getDisplayLanguage(currentLocale)) > 0);
      }

   private void sortLocaleCodes(String[] localeCodes, Locale currentLocale) {
      String holder;
      boolean done = true;
      for (int count = 1; count < localeCodes.length; count++)
         if (switchLocaleCodes(localeCodes[count-1], localeCodes[count], currentLocale)) {
            holder = localeCodes[count-1];
            localeCodes[count-1] = localeCodes[count];
            localeCodes[count] = holder;
            done = false;
            }
      if (!done)
         sortLocaleCodes(localeCodes, currentLocale);
      }

   public void configureContols() {
      //Configure Menu Controls
      languagesGroup.add(languagesMenuItemButtonShow);
      languagesGroup.add(languagesMenuItemButtonHide);
      languagesMenuItem.add(languagesMenuItemButtonShow);  //menu: File | Language Options | Show
      languagesMenuItem.add(languagesMenuItemButtonHide);  //menu: File | Language Options | Hide
      languagesMenuItem.setMnemonic('C');
      filtersGroup.add(filtersMenuItemButtonOn);
      filtersGroup.add(filtersMenuItemButtonOff);
      filtersMenuItem.add(filtersMenuItemButtonOn);   //menu: File | Backup Filters | On
      filtersMenuItem.add(filtersMenuItemButtonOff);  //menu: File | Backup Filters | Off
      profilesGroup.add(profilesMenuItemButtonOn);
      profilesGroup.add(profilesMenuItemButtonOff);
      profilesMenuItem.add(profilesMenuItemButtonOn);   //menu: File | Multiple Profiles | On
      profilesMenuItem.add(profilesMenuItemButtonOff);  //menu: File | Multiple Profiles | Off

      //Configure Base
      basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.PAGE_AXIS));
      basePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      
      //Configure Header
      headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.LINE_AXIS));
      titlePanel.setLayout(new BorderLayout());
      iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.PAGE_AXIS));
      tipLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
      titleLabel.setHorizontalAlignment(JLabel.CENTER);
      for (int count = 0; count < numFlagRows; count++) {
         langFlagsPanels[count] = new JPanel();
         langFlagsPanels[count].setLayout(new FlowLayout(FlowLayout.TRAILING));
         langFlagsPanels[count].setAlignmentX(1.0f);
         //langFlagsPanels[count].setBackground(Color.cyan);
         }
      iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));   //????
      iconLabel.setAlignmentX(1.0f);
      titleLabel.setAlignmentX(0.5f);
      srcPanel.setAlignmentX(0.5f);
      tipLabel.setAlignmentX(0.5f);

      //Configure Profiles
      profilesPanel.setLayout(new FlowLayout());
      profilesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
      profilesPanel.setAlignmentX(1.0f);
      profilesInnerPanel.setBorder(BorderFactory.createTitledBorder(ui.profilesTitle));
      profilesInnerPanel.setLayout(new BoxLayout(profilesInnerPanel, BoxLayout.PAGE_AXIS));
      profilesPromptLabel.setAlignmentX(0.0f);
      profilesDropDown.setAlignmentX(0.0f);
      profilesButtonPanel.setAlignmentX(0.0f);
      
      //Configure Source (Zip List) Controls
      srcPanel.setLayout(new BorderLayout());
      srcPanel.setBorder(BorderFactory.createTitledBorder(ui.srcTitle));
      srcZipList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      srcZipList.setVisibleRowCount(UIProperties.srcMinRows);  //Set field height
      //srcZipList.setVisibleRowCount(15);  //Set field height !!!!!!!!!!

      //Configure Tip
      tipLabel.setHorizontalAlignment(JLabel.CENTER);
      tipLabel.setToolTipText(ui.tipHelp);
      tipLabel.setFont(new Font(null, Font.BOLD, tipLabel.getFont().getSize()-1));
      
      //Configure Destination (Backup & Archive) Controls
      destPanel.setBorder(BorderFactory.createTitledBorder(ui.destTitle));
      //destBackupChooserButton.setIcon(Icons.folderIcon);
      destBackupChooserButton.setToolTipText(ui.destBackupCmd);
      UIUtilities.makeEmphasized(destBackupPathLabel);
      destBackupTagLabel.setIcon(Icons.zipIcon);
      destArchivePromptCheckBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
      //destArchiveChooserButton.setIcon(Icons.folderIcon);
      destArchiveChooserButton.setToolTipText(ui.destArchiveCmd);
      destArchiveTagLabel.setIcon(Icons.driveIcon);
      UIUtilities.makeEmphasized(destArchivePathLabel);

      //Configure Log Controls
      logPanel.setLayout(new BorderLayout());      
      logPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 0, 5, 0),
            BorderFactory.createTitledBorder(ui.logTitle)));
      logTextArea.setEditable(false);
      logTextArea.setFont(UIProperties.standardFont);
      logTextArea.setBackground(Color.lightGray);
      logTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
      /*   OLD BAD WAY???
      logScrollPane.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEmptyBorder(10, 0, 10, 0),
         BorderFactory.createTitledBorder(ui.logTitle)));
         */

      //Configure Button Controls
      UIUtilities.addFastKeys(buttonList);
      UIUtilities.makeBold(doBackupButton);
      getRootPane().setDefaultButton(doBackupButton);
      }

   public void addContols() {
      //Add Menu Controls
      setJMenuBar(menuBar);
      menuBar.add(fileMenuGroup);           //menu: File
      fileMenuGroup.add(languagesMenuItem); //menu: File | Langauge Options
      fileMenuGroup.add(filtersMenuItem);   //menu: File | Backup Filters
      fileMenuGroup.add(profilesMenuItem);  //menu: File | Multiple Profiles
      fileMenuGroup.add(skinMenuItem);      //menu: File | Look & Feel
      fileMenuGroup.addSeparator();
      fileMenuGroup.add(exportMenuItem);    //menu: File | Export Settings...
      fileMenuGroup.add(importMenuItem);    //menu: File | Import Settings...
      fileMenuGroup.addSeparator();
      fileMenuGroup.add(optionsMenuItem);   //menu: File | Options...    ###############################
      fileMenuGroup.add(exitMenuItem);      //menu: File | Exit
      menuBar.add(helpMenuGroup);           //menu: Help
      helpMenuGroup.add(guideMenuItem);     //menu: Help | User Guide
      helpMenuGroup.add(aboutMenuItem);     //menu: Help | About

      //Add Title Control
      titlePanel.add(titleLabel, BorderLayout.PAGE_START);

      ////////////////////////
      //Configure Language Flags
      final String localeCodeKey = SystemAttributes.userName;  //actual value not important
      Locale currentLocale = new Locale(UserPreferences.readLocalePref());
      sortLocaleCodes(SystemAttributes.localeCodes, currentLocale);
      for (int count = 0; count < SystemAttributes.localeCodes.length; count ++) {
         String localeCode = SystemAttributes.localeCodes[count];
         Locale locale = new Locale(localeCode);
         JLabel langLabel = new JLabel(Icons.langIcon(localeCode));
         langLabel.setToolTipText(locale.getDisplayLanguage(currentLocale) +
            (locale.equals(currentLocale) ? SystemAttributes.nullStr : SystemAttributes.dividerStr +
            locale.getDisplayLanguage(locale)));
         langLabel.putClientProperty(localeCodeKey, localeCode);
         langLabel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) { selectLanguage((String)((JLabel)e.getSource()).getClientProperty(localeCodeKey)); }
            public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
            public void mouseExited(MouseEvent e) { setCursor(Cursor.getPredefinedCursor (Cursor.DEFAULT_CURSOR)); }
            public void mousePressed(MouseEvent e) { }
            public void mouseReleased(MouseEvent e) { }  
            } );
         langFlagsPanels[count/flagsPerRow].add(langLabel);
         }
      ////////////////////////

      for (int count = 0; count < numFlagRows; count++)
         iconPanel.add(langFlagsPanels[count]);
      iconPanel.add(iconLabel);
      
      //Add Profiles
      profilesInnerPanel.add(profilesPromptLabel);
      profilesInnerPanel.add(profilesDropDown);
      profilesButtonPanel.add(profilesAddButton);
      profilesButtonPanel.add(profilesDeleteButton);
      profilesInnerPanel.add(profilesButtonPanel);
      profilesPanel.add(profilesInnerPanel);
      iconPanel.add(profilesPanel);

      //Add Source (Zip List) Controls
      srcZipListScrollPane.setViewportView(srcZipList);
      srcPanel.add(srcPromptLabel, BorderLayout.PAGE_START);
      srcPanel.add(srcZipListScrollPane, BorderLayout.CENTER);
      srcPanelButtons.add(srcAddFileButton);
      srcPanelButtons.add(srcAddFolderButton);
      srcPanelButtons.add(srcRemoveButton);
      srcPanelButtons.add(srcFilterButton);
      srcPanel.add(srcPanelButtons, BorderLayout.PAGE_END);
      titlePanel.add(srcPanel, BorderLayout.CENTER);

      //Add Tip
      titlePanel.add(tipLabel, BorderLayout.PAGE_END);
      headerPanel.add(titlePanel);
      headerPanel.add(iconPanel);
      basePanel.add(headerPanel);

      //Add Destination (Backup and Archive) Controls
      GridBagConstraints destFormat = new GridBagConstraints();
      destFormat.anchor = GridBagConstraints.WEST;
      destFormat.insets = new Insets(0, 10, 0, 10);  //top, left, bottom, right
      destPanel.add(destBackupPromptLabel,     gridLoc(destFormat, 0, 0, 4, 1));
      destPanel.add(destBackupDirTextField,    gridLoc(destFormat, 0, 1, 3, 1,
         GridBagConstraints.HORIZONTAL));
      destPanel.add(destBackupChooserButton,   gridLoc(destFormat, 3, 1));
      destPanel.add(destBackupNamePromptLabel, gridLoc(destFormat, 4, 0));
      destPanel.add(destBackupNameTextField,   gridLoc(destFormat, 4, 1));
      destPanel.add(destBackupTagLabel,        gridLoc(destFormat, 0, 2));
      destPanel.add(destArchivePromptCheckBox, gridLoc(destFormat, 0, 3, 3, 1));
      destPanel.add(destArchiveDirTextField,   gridLoc(destFormat, 0, 4, 2, 1,
         GridBagConstraints.HORIZONTAL));
      destPanel.add(destArchiveChooserButton,  gridLoc(destFormat, 2, 4));
      destPanel.add(destArchiveTagLabel,       gridLoc(destFormat, 0, 5));
      destFormat.insets = new Insets(2, 0, 0, 0);  //top, left, bottom, right
      destPanel.add(destBackupPathLabel,       gridLoc(destFormat, 1, 2, 4, 1));
      destPanel.add(destArchivePathLabel,      gridLoc(destFormat, 1, 5, 4, 1));
      basePanel.add(destPanel);

      //Add Log Controls
      logScrollPane.setViewportView(logTextArea);
      logPanel.add(logScrollPane, BorderLayout.CENTER);
      basePanel.add(logPanel);
      
      //Add Button Controls
      buttonPanel.add(saveButton);
      buttonPanel.add(resetButton);
      buttonPanel.add(doBackupButton);
      buttonPanel.add(exitButton);
      basePanel.add(buttonPanel);

      //Put It All Together
      getContentPane().add(basePanel);

      }

   /*
   void setFastKeys(JMenu menuGroup) {
      Component[] menuItems = menuGroup.getMenuComponents();
      for (int count = 0; count < menuItems.length; count++) {
           JMenuItem menuItem = (JMenuItem)menuItems[count];
         String menuText = menuItem.getText();
         int hitLoc = menuText.indexOf('&');  //default is -1
         if (hitLoc > 0)       //strip out hit character ('&')
            menuItem.setText(menuText.substring(0, hitLoc) + menuText.substring(hitLoc + 1));
         menuItem.setMnemonic(menuText.charAt(hitLoc + 1));  //typically 1st character
         if (menuItem.getClass().equals(JMenu.class))
            setFastKeys((JMenu)menuItems[count]);
        }
      }
   */

   //
   // GridBagConstraints Utilities
   //
   public GridBagConstraints gridLoc(GridBagConstraints constraints,
            int col, int row, int colSpan, int rowSpan, int stretch) {
      constraints.gridx = col;
      constraints.gridy = row;
      constraints.gridwidth = colSpan;
      constraints.gridheight = rowSpan;
      constraints.fill = stretch;
      constraints.weightx = (stretch == GridBagConstraints.NONE ? 0.0 : 0.5);
      constraints.weighty = (stretch == GridBagConstraints.BOTH ? 0.5 : 0.0);
      return constraints;
      }
   public GridBagConstraints gridLoc(GridBagConstraints constraints,
            int col, int row, int colSpan, int rowSpan) {
      return gridLoc(constraints, col, row, colSpan, rowSpan,
         GridBagConstraints.NONE);
      }
   public GridBagConstraints gridLoc(GridBagConstraints constraints,
            int col, int row) {
      return gridLoc(constraints, col, row, 1, 1, GridBagConstraints.NONE);
      }

   //
   // Access to UI Controls
   //
   public JComboBox getProfilesDropDown() {
      return profilesDropDown;
      }
   public void initLanguagesUI(boolean showLanguages) {
      languagesMenuItemButtonShow.setSelected(showLanguages);
      languagesMenuItemButtonHide.setSelected(!showLanguages);
      for (int count = 0; count < numFlagRows; count++)
         langFlagsPanels[count].setVisible(showLanguages);
      }
   public boolean getShowLanguages() {
      return languagesMenuItemButtonShow.isSelected();
      }
   public void initFiltersUI(boolean filtersEnabled) {
      filtersMenuItemButtonOn.setSelected(filtersEnabled);
      filtersMenuItemButtonOff.setSelected(!filtersEnabled);
      srcFilterButton.setVisible(filtersEnabled);
      }
   public boolean getFiltersEnabled() {
      return filtersMenuItemButtonOn.isSelected();
      }
   public String getSkinName() {
      return (String)skinMenuItem.getSelectedObjects()[0];
      }
   public void initProfilesUI(boolean showProfiles) {
      profilesMenuItemButtonOn.setSelected(showProfiles);
      profilesMenuItemButtonOff.setSelected(!showProfiles);
      profilesInnerPanel.setVisible(showProfiles);
      if (profilesDropDown.getItemCount() == 1)
         profilesDeleteButton.setEnabled(false);
      }
   public boolean getProfilesEnabled() {
      return profilesMenuItemButtonOn.isSelected();
      }
   public void setProfilesDropDown(String[] profileNames) {
      profilesDropDown.removeAllItems();
      for (int count = 0; count < profileNames.length; count++)
         profilesDropDown.addItem(profileNames[count]);
      }
   public String readProfilesDropDown() {
      String profileList = (String)profilesDropDown.getItemAt(0);
      for (int count = 1; count < profilesDropDown.getItemCount(); count++)
         profileList = profileList + SystemAttributes.newLine + (String)profilesDropDown.getItemAt(count);
      return profileList;
      }
   public void setCurrentProfile(String profileName) {
       profilesCurrentTextField.setText(profileName);
       }
   public void selectProfile(String profileName) {
       setCurrentProfile(profileName);
       profilesDropDown.setSelectedItem(profileName);  //triggers callback (unless unchanged)
       }
   public String getCurrentProfileName() {
      //return (String)profilesDropDown.getSelectedItem();
      return profilesCurrentTextField.getText();
      }
   public DefaultListModel getSrcZipListModel() {
      return srcZipListModel;
      }
   public JList getSrcZipList() {
      return srcZipList;
      }
   public JButton getSrcRemoveButton() {
       return srcRemoveButton;
       }
   public JButton getSrcFilterButton() {
       return srcFilterButton;
       }
   public JTextField getDestBackupDirTextField() {
      return destBackupDirTextField;
      }
   public JTextField getDestBackupNameTextField() {
      return destBackupNameTextField;
      }
   public JLabel getDestBackupPathLabel() {
      return destBackupPathLabel;
      }
   public JCheckBox getDestArchivePromptCheckBox() {
      return destArchivePromptCheckBox;
      }
   public JTextField getDestArchiveDirTextField() {
      return destArchiveDirTextField;
      }
   public JButton getDestArchiveChooserButton() {
      return destArchiveChooserButton;
      }
   public JLabel getDestArchiveTagLabel() {
      return destArchiveTagLabel;
      }
   public JLabel getDestArchivePathLabel() {
      return destArchivePathLabel;
      }
   public JTextArea getLogTextArea() {
      return logTextArea;
      }
   public JScrollPane getLogScrollPane() {
      return logScrollPane;
      }
   public JButton getExitButton() {
      return exitButton;
      }


   //
   // Action Listeners for Menu Items
   //
   public void setupMenuCallbacks() {       
      languagesMenuItemButtonShow.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { languagesMenuItemAction(e); }
         } );
      languagesMenuItemButtonHide.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { languagesMenuItemAction(e); }
         } );
      filtersMenuItemButtonOn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { filtersMenuItemAction(e); }
         } );
      filtersMenuItemButtonOff.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { filtersMenuItemAction(e); }
         } );
      profilesMenuItemButtonOn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { profilesMenuItemAction(e); }
         } );
      profilesMenuItemButtonOff.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { profilesMenuItemAction(e); }
         } );
      exportMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { exportMenuItemAction(e); }
         } );
      importMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { importMenuItemAction(e); }
         } );
      optionsMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { optionsMenuItemAction(e); }
         } );
      //Skin (Look and Feel)
      String currentSkinName = UserPreferences.readPref(DataModel.prefSkinName);
      currentSkinName = UIManager.getLookAndFeel().getClass().getName();
      JRadioButtonMenuItem skinRbmi;
      ButtonGroup skinGroup = new ButtonGroup();
      for (int count = 0; count < UIProperties.lafs.length; count++) {
         skinRbmi = new JRadioButtonMenuItem(UIProperties.lafs[count].getName(),
            UIProperties.lafs[count].getClassName().equals(currentSkinName));
         skinRbmi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { skinMenuItemAction(e); }
            } );
         skinGroup.add(skinRbmi);
         skinMenuItem.add(skinRbmi); //menu: File | Look & Feel | ZZZZ
         }
      exitMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { exitMenuItemAction(e); }
         } );
      guideMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { guideMenuItemAction(e); }
         } );
      aboutMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { aboutMenuItemAction(e); }
         } );
      }

   //
   // Action Listeners for UI Controls
   //
   public void setupCallbacks() {

      //Setup Callbacks for Profiles Controls
      profilesDropDown.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) { profilesSelectAction(e); }
         } );
      profilesAddButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { profilesNewButtonAction(e); }
         } );
      profilesDeleteButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { profilesDeleteButtonAction(e); }
         } );

      //Setup Callbacks for Source (Zip List) Controls
      srcZipList.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) { srcZipListSelection(e); }
         } );
      srcAddFileButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { srcAddButtonAction(e, JFileChooser.FILES_ONLY); }
         } );
      srcAddFolderButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { srcAddButtonAction(e, JFileChooser.DIRECTORIES_ONLY); }
         } );
      srcRemoveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { srcRemoveButtonAction(e); }
         } );
      srcFilterButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) { srcFilterButtonAction(e); }
          } );

      //Setup Callbacks for Destination (Backup & Archive) Controls
      destBackupDirTextField.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent e) { destBackupDirTextFieldEdited(e); }
         });
      destBackupNameTextField.addKeyListener(new KeyAdapter(){
         public void keyReleased(KeyEvent e){ destBackupNameTextFieldEdited(e); }
         });
      destBackupChooserButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { destBackupChooserButtonAction(e); }
         } );
      destArchivePromptCheckBox.addItemListener(new ItemListener(){
         public void itemStateChanged(ItemEvent e){destArchivePromptCheckBoxToggled(e);}
         });
      destArchiveDirTextField.addKeyListener(new KeyAdapter(){
         public void keyReleased(KeyEvent e){ destArchiveDirTextFieldEdited(e);}
         });
      destArchiveChooserButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { destArchiveChooserButtonAction(e); }
         } );

      //Setup Callbacks for Button Controls
      saveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { saveButtonAction (e); }
         } );
      resetButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { resetButtonAction (e); }
         } );
      doBackupButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { doBackupButtonAction (e); }
         } );
      exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { exitButtonAction (e); }
         } );
       }


   //
   // Callback Methods (Event Actions)
   //

   //Menu Items Callbacks
   public void languagesMenuItemAction(ActionEvent e) {
      for (int count = 0; count < numFlagRows; count++)
         langFlagsPanels[count].setVisible(languagesMenuItemButtonShow.isSelected());
      DataModel.saveShowLanguagesSetting(this);
      }
   public void filtersMenuItemAction(ActionEvent e) {
      //boolean filtersEnabled = filtersMenuItemButtonOn.isSelected();
      //srcFilterButton.setVisible(filtersEnabled);
      DataModel.buildZipListModel(getSrcZipListModel(), this);
      DataModel.updateSrcButtons(this);
      DataModel.saveFiltersEnabledSetting(this);
      }
   public void profilesMenuItemAction(ActionEvent e) {
      boolean showProfiles = profilesMenuItemButtonOn.isSelected();
      if (!showProfiles)
         popupMsg(Str.macroExpand(ui.profilesOffMsg, getCurrentProfileName()));
      profilesInnerPanel.setVisible(showProfiles);
      DataModel.saveShowProfilesSetting(this);
      }
   public void skinMenuItemAction(ActionEvent e) {
      int count = 0;
      while (count < UIProperties.lafs.length &&
            !e.getActionCommand().equals(UIProperties.lafs[count].getName()))
         count++;
      try {
         UIManager.LookAndFeelInfo laf = UIProperties.lafs[count];
         UIManager.setLookAndFeel(laf.getClassName());
         SwingUtilities.updateComponentTreeUI(this);

         //FIX NULL ICON PROBLEM ???
         //destBackupChooserButton.setIcon(Icons.folderIcon);
         //private static JButton      destBackupChooserButton =   new JButton(Icons.folderIcon);
         //Icons.folderIcon == null ? 'Choose...' : Icons.folderIcon);

         pack();
         UIUtilities.lockInMinSize(this);
         popupMsg(Str.macroExpand(UIProperties.skinSetMsg, laf.getName()));
         DataModel.saveSkinSetting(laf.getClassName());
         }
      catch (Exception e2) {
         popupMsg(UIProperties.skinErrMsg);
         }
      }
   public void exitMenuItemAction(ActionEvent e) {
      DataModel.exit();
      }
   public void exportMenuItemAction(ActionEvent e) {
      ExportDialog exportDialog = new ExportDialog();
      UIUtilities.centerDialog(exportDialog, this);
      }
   public void importMenuItemAction(ActionEvent e) {
      // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      }
   public void optionsMenuItemAction(ActionEvent e) {
      String oldNumRowsSrc = UserPreferences.readPref(Options.prefNumRowsSrc);
      String oldNumRowsLog = UserPreferences.readPref(Options.prefNumRowsLog);
      OptionsDialog optionsDialog = new OptionsDialog(destBackupNameTextField.getText());
      UIUtilities.centerDialog(optionsDialog, this);
      DataModel.updateDestPaths(this);
      if (!oldNumRowsSrc.equals(UserPreferences.readPref(Options.prefNumRowsSrc)) ||
         !oldNumRowsLog.equals(UserPreferences.readPref(Options.prefNumRowsLog))) {
         UIUtilities.setInitialNumRows(this);
         //Below lines are hack to force resize of frame (freaky!)
         this.setSize(this.getWidth()+1, this.getHeight());  //N
         this.validate();   //N
         this.pack();   //N
         this.setSize(this.getWidth()+1, this.getHeight());  //N
         this.validate();  //N
         this.pack();  //N
         }
      }
   public void guideMenuItemAction(ActionEvent e) {
      UserGuideDialog guideDialog = new UserGuideDialog(this.getSize());
      UIUtilities.centerDialog(guideDialog, this);
      }
   public void aboutMenuItemAction(ActionEvent e) {
      AboutDialog aboutDialog = new AboutDialog();
      UIUtilities.centerDialog(aboutDialog, this);
      }

   //Language Icon Callbacks
   public void selectLanguage(String localeCode) {
      UserPreferences.saveLocalePref(localeCode);
      AppProperties.reload();
      new Application();
      this.setEnabled(false);
      this.dispose();
      }

   //Profiles Callbacks
   public void profilesSelectAction(ItemEvent e) {
       if (e.getStateChange() == ItemEvent.SELECTED) {
          setCurrentProfile((String)profilesDropDown.getSelectedItem());
          DataModel.loadProfile(this);
          UserPreferences.savePref(DataModel.prefCurrentProfile, getCurrentProfileName());
          }
       }
   public void profilesNewButtonAction(ActionEvent e) {   //is 'e' even needed????
      String profileName =
         JOptionPane.showInputDialog(null, ui.profilesAddPrompt, ui.profilesAddTitle, JOptionPane.PLAIN_MESSAGE);
      if (profileName != null) {
         profileName = profileName.trim();
         if (profileName.length() == 0) {
            popupMsg(ui.profilesAddMsgBlank);
            profilesNewButtonAction(e);  //prompt again
            }
         else if (profileName.equalsIgnoreCase(getCurrentProfileName())) {
            //handles edge case for very first new profile added
            popupMsg(ui.profilesAddMsgExists);
            profilesNewButtonAction(e);  //prompt again
            }
         else if (UserPreferences.profileInDB(profileName)) {
            popupMsg(ui.profilesAddMsgExists);
            profilesNewButtonAction(e);  //prompt again
            }
         else {
            if (!UserPreferences.profileInDB())
               DataModel.saveSettings(this); //handles edge case for very first new profile added
            setCurrentProfile(profileName);
            DataModel.saveSettings(this);  //copies last profile into new profile
            profilesDropDown.addItem(profileName);
            profilesDropDown.setSelectedItem(profileName);  //trigger callback
            profilesDeleteButton.setEnabled(true);
            }
          }
       }
   private boolean promptDeleteProfile(String name) {
      return JOptionPane.showConfirmDialog(null,
         Str.macroExpand(ui.profilesDeletePrompt, name),
         ui.profilesDeleteTitle, JOptionPane.YES_NO_OPTION) == 0;
      }
   public void profilesDeleteButtonAction(ActionEvent e) {
      if (promptDeleteProfile(getCurrentProfileName())) {
         UserPreferences.deleteProfile();
         int loc = profilesDropDown.getSelectedIndex();
         profilesDropDown.removeItemAt(loc);
         profilesDropDown.setSelectedIndex(
            Math.min(loc, profilesDropDown.getItemCount() - 1));  //triggers callback
         profilesDeleteButton.setEnabled(profilesDropDown.getItemCount() > 1);
         }
      }
   
   //Source Callbacks
   public void srcZipListSelection(ListSelectionEvent e) {
      DataModel.updateSrcButtons(this);
      }
   /*
   public void srcZipListScrollPane(MouseEvent e) {
      DataModel.updateSrcButtons();
      System.out.println('Mouse Event: ' + e.toString());
      }
      */
   public void srcAddButtonAction(ActionEvent e, int fileSelectionMode) {
      JFileChooser srcAddFileChooser = new JFileChooser();
      srcAddFileChooser.setFileSelectionMode(fileSelectionMode);
      //jFileChooserData.setCurrentDirectory(new File(nullStr));
      final int returnStatus =
         //srcAddFileChooser.showDialog(this, ui.srcAddCmd);
         srcAddFileChooser.showOpenDialog(this);
      if (returnStatus == JFileChooser.APPROVE_OPTION)
         DataModel.addZipItem(srcAddFileChooser.getSelectedFile().getAbsolutePath(), this);
      }
   public void srcRemoveButtonAction(ActionEvent e) {
      DataModel.removeCurrentZipItem(this);
      }
   public void srcFilterButtonAction(ActionEvent e) {
      FilterDialog dialog = new FilterDialog();
      UIUtilities.centerDialog(dialog, this);
      }


   //Destination Callbacks
   public void destChooserButtonAction(JTextField destDirTextField, String msg) {
      JFileChooser destFileChooser = new JFileChooser();
      destFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      destFileChooser.setCurrentDirectory(
         new File(destDirTextField.getText()));
      int returnStatus =
         //destFileChooser.showDialog(this, msg);
         destFileChooser.showOpenDialog(this);
      if (returnStatus == JFileChooser.APPROVE_OPTION)
         destDirTextField.setText(
            destFileChooser.getSelectedFile().getAbsolutePath());
      }
   public void destBackupChooserButtonAction(ActionEvent e) {
      destChooserButtonAction(destBackupDirTextField, ui.destBackupCmd);
      }
   public void destArchiveChooserButtonAction(ActionEvent e) {
      destChooserButtonAction(destArchiveDirTextField, ui.destArchiveCmd);
      }
   public void destBackupDirTextFieldEdited(KeyEvent e) {
      DataModel.updateDestPaths(this);
      }
   public void destBackupNameTextFieldEdited(KeyEvent e) {
      int badKeyLoc = destBackupNameTextField.getText().indexOf(SystemAttributes.fileSeparator);
      while (badKeyLoc != -1) {
         destBackupNameTextField.setText(
            destBackupNameTextField.getText().substring(0, badKeyLoc) +
            destBackupNameTextField.getText().substring(badKeyLoc + 1));
         badKeyLoc = destBackupNameTextField.getText().indexOf(SystemAttributes.fileSeparator);
         }
      DataModel.updateDestPaths(this);
      }
   public void destArchivePromptCheckBoxToggled(ItemEvent e) {
      DataModel.updateArchiveDir(this);
      }

   //Button Callbacks
   public void destArchiveDirTextFieldEdited(KeyEvent e) {
      DataModel.updateDestPaths(this);
      }
   public void saveButtonAction(ActionEvent e) {
      popupMsg(DataModel.saveSettings(this));
      }
   public void resetButtonAction(ActionEvent e) {
      popupMsg(DataModel.restoreDefaultSettings(this));
      }
   public void doBackupButtonAction(ActionEvent e) {
      DataModel.doBackupNow();
      }
   public void exitButtonAction(ActionEvent e) {
      DataModel.exit();
      }

   }
