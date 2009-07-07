////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// OptionsDialog.java                                                         //
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
// Options Dialog:                                                            //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.options;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.snapbackup.ui.UIUtilities;
import com.snapbackup.utilities.DateStamp;
import com.snapbackup.settings.AppProperties;
import com.snapbackup.settings.SystemAttributes;
import com.snapbackup.settings.UserPreferences;

public class OptionsDialog extends JDialog {

   String[] str2Array(String str) {
      String[] strArray = new String[str.length()];
      for (int count = 0; count < strArray.length; count++)
         strArray[count] = Character.toString(str.charAt(count));
      return strArray;
      }

   //Define Options Controls
   String coreBackupName;
   OptionsUIProperties ui = new OptionsUIProperties();   //make singleton for performance????

   JPanel       optionsPanel =         new JPanel();
   
   JPanel       nameFormatPanel =      new JPanel();
   JPanel       examplePanel =         new JPanel();
   JLabel       exampleHeaderLabel =   new JLabel(ui.fileFormatExample);
   JLabel       exampleDataLabel =     new JLabel();
   JPanel       spacerPanel =          new JPanel();
   JLabel       spacerPromptLabel =    new JLabel(ui.fileFormatSpacerPrompt);
   JComboBox    spacerDropDown =       new JComboBox(str2Array(ui.fileFormatSpacerList));
   JPanel       orderPanel =           new JPanel();
   JLabel       orderLabel =           new JLabel(ui.fileFormatOrderPrompt);
   JComboBox    order1stDropDown =     new JComboBox(ui.orderList);
   JComboBox    order2ndDropDown =     new JComboBox(ui.orderList);
   JComboBox    order3rdDropDown =     new JComboBox(ui.orderListNone);
   JComboBox[]  orderDropDowns =       { order1stDropDown, order2ndDropDown, order3rdDropDown };
   boolean      orderUserInputMode;    //Yeah, this is a hack
   JPanel       yearPanel =            new JPanel();
   JLabel       yearLabel =            new JLabel(ui.fileFormatYearPrompt);
   ButtonGroup  yearButtonGroup =      new ButtonGroup();
   JRadioButton year2DigitsButton =    new JRadioButton(ui.fileFormatYear2Digits);
   JRadioButton year4DigitsButton =    new JRadioButton(ui.fileFormatYear4Digits);
   JPanel       separatorPanel =       new JPanel();
   JLabel       separatorPromptLabel = new JLabel(ui.fileFormatSeparatorPrompt);
   JComboBox    separatorDropDown =    new JComboBox(str2Array(ui.fileFormatSeparatorList));

   JPanel       fileOverwritePanel =   new JPanel();
   JLabel       askBackupLabel =       new JLabel(ui.overwriteBackupPrompt);
   JLabel       askArchiveLabel =      new JLabel(ui.overwriteArchivePrompt);
   JCheckBox    askBackupCheckbox =    new JCheckBox();
   JCheckBox    askArchiveCheckbox =   new JCheckBox();

   JPanel       numRowsPanel =         new JPanel();
   JLabel       srcRowsPromptLabel =   new JLabel(ui.numRowsSrcPrompt);
   JLabel       logRowsPromptLabel =   new JLabel(ui.numRowsLogPrompt);
   SpinnerModel srcNumRowsModel =      new SpinnerNumberModel(4, 4, 25, 1);  //min < 4 seems to be ignored
   SpinnerModel logNumRowsModel =      new SpinnerNumberModel(2, 2, 25, 1);
   JSpinner     srcRowsSpinner =       new JSpinner(srcNumRowsModel);
   JSpinner     logRowsSpinner =       new JSpinner(logNumRowsModel);

   JPanel       buttonPanel =          new JPanel();
   JButton      cancelButton =         new JButton(ui.buttonCancel);
   JButton      defaultsButton =       new JButton(ui.buttonDefaults);
   JButton      okButton =             new JButton(ui.buttonOk);
   JButton[]    buttonList =           { cancelButton, defaultsButton, okButton };
   
   public OptionsDialog(String backupName) {
      coreBackupName = SystemAttributes.space + backupName;
      }

   public void initGUI(Component parent) {
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setTitle(ui.title);
      configureContols();
      addContols();
      initValues();
      updateFileName();
      getContentPane().add(optionsPanel);
      setModal(true);
      setResizable(false);
      pack();
      setLocationRelativeTo(parent);
      setVisible(true);
      }

   void configureContols() {
      optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
      optionsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

      nameFormatPanel.setLayout(new BoxLayout(nameFormatPanel, BoxLayout.PAGE_AXIS));
      nameFormatPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createTitledBorder(ui.fileFormatTitle),
         BorderFactory.createEmptyBorder(0, 5, 5, 5)));
      UIUtilities.makeEmphasized(exampleDataLabel);
      spacerDropDown.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { updateFileName(); } } );
      order1stDropDown.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { updateFileName(0); } } );
      order2ndDropDown.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { updateFileName(1); } } );
      order3rdDropDown.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { updateFileName(2); } } );
      yearButtonGroup.add(year2DigitsButton);
      yearButtonGroup.add(year4DigitsButton);
      year2DigitsButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { updateFileName(); } } );
      year4DigitsButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { updateFileName(); } } );
      separatorDropDown.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { updateFileName(); } } );

      fileOverwritePanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createTitledBorder(ui.overwriteTitle),
         BorderFactory.createEmptyBorder(0, 5, 5, 5)));

      numRowsPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createTitledBorder(ui.numRowsTitle),
         BorderFactory.createEmptyBorder(0, 5, 5, 5)));

      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
      buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionCancel(); } } );
      defaultsButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionReset(); } } );
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionOk(); } } );
      UIUtilities.addFastKeys(buttonList);
      UIUtilities.makeBold(okButton);
      getRootPane().setDefaultButton(okButton);
      getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               actionCancel(); } },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);
      }

   void addContols() {
      examplePanel.add(exampleHeaderLabel);
      examplePanel.add(exampleDataLabel);
      nameFormatPanel.add(examplePanel);
      spacerPanel.add(spacerPromptLabel);
      spacerPanel.add(spacerDropDown);
      nameFormatPanel.add(spacerPanel);
      orderPanel.add(orderLabel);
      orderPanel.add(order1stDropDown);
      orderPanel.add(order2ndDropDown);
      orderPanel.add(order3rdDropDown);
      nameFormatPanel.add(orderPanel);
      yearPanel.add(yearLabel);
      yearPanel.add(year2DigitsButton);
      yearPanel.add(year4DigitsButton);
      nameFormatPanel.add(yearPanel);
      separatorPanel.add(separatorPromptLabel);
      separatorPanel.add(separatorDropDown);
      nameFormatPanel.add(separatorPanel);
      optionsPanel.add(nameFormatPanel);
      optionsPanel.add(Box.createRigidArea(new Dimension(0,10)));

      fileOverwritePanel.add(askBackupCheckbox);
      fileOverwritePanel.add(askBackupLabel);
      fileOverwritePanel.add(new JLabel(SystemAttributes.tab + SystemAttributes.tab));
      fileOverwritePanel.add(askArchiveCheckbox);
      fileOverwritePanel.add(askArchiveLabel);
      optionsPanel.add(fileOverwritePanel);
      optionsPanel.add(Box.createRigidArea(new Dimension(0,10)));

      numRowsPanel.add(srcRowsPromptLabel);
      numRowsPanel.add(srcRowsSpinner);
      numRowsPanel.add(new JLabel(SystemAttributes.tab + SystemAttributes.tab));
      numRowsPanel.add(logRowsPromptLabel);
      numRowsPanel.add(logRowsSpinner);
      optionsPanel.add(numRowsPanel);
      optionsPanel.add(Box.createRigidArea(new Dimension(0,15)));

      buttonPanel.add(cancelButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
      buttonPanel.add(defaultsButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
      buttonPanel.add(okButton);
      optionsPanel.add(buttonPanel);
      }

   int orderIndex(int slot) {
      return Integer.parseInt(
         UserPreferences.readPref(Options.prefOrder).substring(slot, slot + 1));
      }

   void initValues() {
      spacerDropDown.setSelectedItem(UserPreferences.readPref(Options.prefSpacer));
      orderUserInputMode = false;
      order1stDropDown.setSelectedIndex(orderIndex(0));
      order2ndDropDown.setSelectedIndex(orderIndex(1));
      order3rdDropDown.setSelectedIndex(orderIndex(2));
      orderUserInputMode = true;
      year2DigitsButton.setSelected(UserPreferences.readPref(Options.prefYear).equals(Options.year2Digits));
      year4DigitsButton.setSelected(UserPreferences.readPref(Options.prefYear).equals(Options.year4Digits));
      separatorDropDown.setSelectedItem(UserPreferences.readPref(Options.prefSeparator));

      askBackupCheckbox.setSelected(UserPreferences.readPref(Options.prefAskBackup).equals(Options.askYes));
      askArchiveCheckbox.setSelected(UserPreferences.readPref(Options.prefAskArchive).equals(Options.askYes));

      srcRowsSpinner.setValue(Integer.valueOf(UserPreferences.readPref(Options.prefNumRowsSrc)));
      logRowsSpinner.setValue(Integer.valueOf(UserPreferences.readPref(Options.prefNumRowsLog)));
      }

   void updateFileName() {
      this.exampleDataLabel.setText(coreBackupName +
         spacerDropDown.getSelectedItem() +
         DateStamp.todaysDateStamp(
               Integer.toString(order1stDropDown.getSelectedIndex()) +
               Integer.toString(order2ndDropDown.getSelectedIndex()) +
               Integer.toString(order3rdDropDown.getSelectedIndex()),
            year2DigitsButton.isSelected(),
            (String)separatorDropDown.getSelectedItem()) +
         AppProperties.getProperty("StandardZipExtension"));
      }
   
   void updateFileName(int activeOrder) {
      if (orderUserInputMode) {
         int activeOrderValue = orderDropDowns[activeOrder].getSelectedIndex();
         for (int count = 0; count < orderDropDowns.length; count++)
            if (count != activeOrder && activeOrderValue == orderDropDowns[count].getSelectedIndex()) { //collision
               orderUserInputMode = false;
               int newValue = (activeOrderValue + 1) % 3;
               orderDropDowns[count].setSelectedIndex(newValue);
               if (newValue == orderDropDowns[3 - count - activeOrder].getSelectedIndex())  //2nd collision
                  orderDropDowns[count].setSelectedIndex((newValue + 1) % 3);
               orderUserInputMode = true;
               }
            }
      updateFileName();
      }
   
   //
   // Callback Methods (Event Actions)
   //
   // Buttons

   void actionCancel() {
      this.dispose();
      }

   void actionReset() {
      spacerDropDown.setSelectedItem(Options.spacerDefault);
      orderUserInputMode = false;
      order1stDropDown.setSelectedIndex(Integer.parseInt(Options.orderDefault.substring(0, 1)));
      order2ndDropDown.setSelectedIndex(Integer.parseInt(Options.orderDefault.substring(1, 2)));
      order3rdDropDown.setSelectedIndex(Integer.parseInt(Options.orderDefault.substring(2, 3)));
      orderUserInputMode = true;
      year2DigitsButton.setSelected(Options.year2Digits.equals(Options.yearDefault));
      year4DigitsButton.setSelected(Options.year4Digits.equals(Options.yearDefault));
      separatorDropDown.setSelectedItem(Options.separatorDefault);
      updateFileName();

      askBackupCheckbox.setSelected(Options.askBackupDefault.equals(Options.askYes));
      askArchiveCheckbox.setSelected(Options.askArchiveDefault.equals(Options.askYes));

      srcRowsSpinner.setValue(Integer.valueOf(Options.numRowsSrcDefault));
      logRowsSpinner.setValue(Integer.valueOf(Options.numRowsLogDefault));
      }

   void actionOk() {
      UserPreferences.savePref(Options.prefSpacer, (String)spacerDropDown.getSelectedItem());
      UserPreferences.savePref(Options.prefOrder, "" +
            order1stDropDown.getSelectedIndex() +
            order2ndDropDown.getSelectedIndex() +
            order3rdDropDown.getSelectedIndex());
      UserPreferences.savePref(Options.prefYear, year2DigitsButton.isSelected() ? Options.year2Digits : Options.year4Digits);
      UserPreferences.savePref(Options.prefSeparator, (String)separatorDropDown.getSelectedItem());

      UserPreferences.savePref(Options.prefAskBackup, askBackupCheckbox.isSelected() ? Options.askYes : Options.askNo);
      UserPreferences.savePref(Options.prefAskArchive, askArchiveCheckbox.isSelected() ? Options.askYes : Options.askNo);

      UserPreferences.savePref(Options.prefNumRowsSrc, ((Integer)srcRowsSpinner.getValue()).toString());
      UserPreferences.savePref(Options.prefNumRowsLog, ((Integer)logRowsSpinner.getValue()).toString());

      this.dispose();
      }
   
   }
