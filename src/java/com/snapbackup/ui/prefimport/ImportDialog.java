////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// ImportSettingsDialog.java                                                  //
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
// Import Settings Dialog:                                                    //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.prefimport;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import com.snapbackup.business.ExportPrefs;
import com.snapbackup.business.ImportPrefs;
import com.snapbackup.ui.Application;
import com.snapbackup.ui.Icons;
import com.snapbackup.ui.UIUtilities;
import com.snapbackup.ui.SnapBackupFrame;
import com.snapbackup.ui.prefexport.Export;
import com.snapbackup.settings.AppProperties;
import com.snapbackup.settings.SystemAttributes;
import com.snapbackup.settings.UserPreferences;

public class ImportDialog extends JDialog {

   //Define Controls
   ImportUIProperties ui = new ImportUIProperties();
   JPanel       basePanel =               new JPanel();
   JPanel       locationPanel =           new JPanel();
   JPanel       locationInnerPanel =      new JPanel();
   JLabel       locationPromptLabel =     new JLabel(ui.locationPrompt + SystemAttributes.space);
   JTextField   locationTextField =       new JTextField(Export.fileNameCols);
   JButton      locationChooserButton =   new JButton(Icons.folderIcon);
   JLabel       locationDetailsLabel =    new JLabel(ui.locationDetails);
   JLabel       locationWarningLabel =    new JLabel(ui.locationWarning);
   JPanel       buttonPanel =             new JPanel();
   JButton      cancelButton =            new JButton(ui.buttonCancel);
   JButton      actionButton =            new JButton(ui.buttonImport);
   JButton[]    buttonList =              { cancelButton, actionButton };

   public ImportDialog(Frame owner) {
      super(owner);
      AppProperties.addSupplimentalProperty(Export.prefSettingsFileName,
         new Export().defaultSettingsFileName);
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setTitle(ui.title);
      configureContols();
      addContols();
      getContentPane().add(basePanel);
      setModal(true);
      setResizable(false);
      pack();
      setLocationRelativeTo(owner);
      setVisible(true);
      }

   void configureContols() {
      basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.PAGE_AXIS));
      basePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.PAGE_AXIS));
      locationPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createTitledBorder(ui.locationTitle),
         BorderFactory.createEmptyBorder(0, 5, 5, 5)));
      locationInnerPanel.setLayout(new BoxLayout(locationInnerPanel, BoxLayout.LINE_AXIS));
      locationInnerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      locationPanel.setAlignmentY(Component.LEFT_ALIGNMENT);
      locationTextField.setText(UserPreferences.readPref(Export.prefSettingsFileName));
      locationChooserButton.setToolTipText(ui.locationCmd);
      locationChooserButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { locationChooserButtonAction(e); }
         } );
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
      buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionCancel(); } } );
      actionButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionImport(); } } );
      UIUtilities.addFastKeys(buttonList);
      UIUtilities.makeBold(actionButton);
      getRootPane().setDefaultButton(actionButton);
      }

   void addContols() {
      basePanel.add(locationPanel);
      locationPanel.add(locationInnerPanel);
      locationInnerPanel.add(locationPromptLabel);
      locationInnerPanel.add(locationTextField);
      locationInnerPanel.add(Box.createRigidArea(new Dimension(5,0)));
      locationInnerPanel.add(locationChooserButton);
      locationPanel.add(Box.createRigidArea(new Dimension(0,10)));
      locationPanel.add(locationDetailsLabel);
      locationPanel.add(Box.createRigidArea(new Dimension(0,10)));
      locationPanel.add(locationWarningLabel);
      basePanel.add(Box.createRigidArea(new Dimension(0,10)));
      basePanel.add(buttonPanel);
      buttonPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(cancelButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
      buttonPanel.add(actionButton);
      }

   //
   // Callback Methods (Event Actions)
   //
   void locationChooserButtonAction(ActionEvent e) {
      JFileChooser locationFileChooser = new JFileChooser();
      locationFileChooser.setSelectedFile(new File(locationTextField.getText()));
      locationFileChooser.setFileFilter(new ExportPrefs().xmlFilter);
      int returnStatus = locationFileChooser.showOpenDialog(this);
      if (returnStatus == JFileChooser.APPROVE_OPTION)
         locationTextField.setText(
               locationFileChooser.getSelectedFile().getAbsolutePath());
      }

   void actionCancel() {
      this.dispose();
      }

   void actionImport() {
      String errMsg = new ImportPrefs().doImport(locationTextField.getText());
      if (errMsg == null) {
         JOptionPane.showMessageDialog(this, ui.msgSuccess, new Export().Settings,
               JOptionPane.PLAIN_MESSAGE);
         SnapBackupFrame oldFrame = SnapBackupFrame.current;
         AppProperties.reload();
         new Application();
         oldFrame.setEnabled(false);
         oldFrame.dispose();
         this.dispose();
         }
      else
         JOptionPane.showMessageDialog(this, errMsg, ui.locationCmd,
               JOptionPane.ERROR_MESSAGE);
      }

   }
