////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// ExportSettingsDialog.java                                                         //
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
// Export Settings Dialog:                                                    //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.prefexport;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.snapbackup.ui.Icons;
import com.snapbackup.ui.UIUtilities;
import com.snapbackup.utilities.settings.UserPreferences;
import com.snapbackup.utilities.settings.AppProperties;
import com.snapbackup.business.ExportDataModel;

public class ExportDialog extends JDialog {

   //Define Controls
   ExportUIProperties ui = new ExportUIProperties();
   JPanel       basePanel =               new JPanel();
   JPanel       locationPanel =           new JPanel();
   JPanel       locationInnerPanel =      new JPanel();
   JLabel       locationPromptLabel =     new JLabel(ui.locationPrompt);
   JTextField   locationTextField =       new JTextField(Export.fileNameCols);
   JButton      locationChooserButton =   new JButton(Icons.folderIcon);
   JLabel       locationDetails1Label =   new JLabel(ui.locationDetails1);
   JLabel       locationDetails2Label =   new JLabel(ui.locationDetails2);
   JPanel       buttonPanel =             new JPanel();
   JButton      cancelButton =            new JButton(ui.buttonCancel);
   JButton      actionButton =            new JButton(ui.buttonExport);
   JButton[]    buttonList =              { cancelButton, actionButton };

   public ExportDialog() {
      AppProperties.addSupplimentalProperty(Export.prefSettingsFileName,
            new Export().defaultSettingsFileName);
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setTitle(ui.title);
      configureContols();
      addContols();
      initValues();
      getContentPane().add(basePanel);
      setModal(true);
      setResizable(false);
      pack();
      }

   void configureContols() {
      basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.PAGE_AXIS));
      basePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.PAGE_AXIS));
   	locationPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createTitledBorder(ui.locationTitle),
         BorderFactory.createEmptyBorder(0, 5, 5, 5)));
      locationInnerPanel.setLayout(new BoxLayout(locationInnerPanel, BoxLayout.LINE_AXIS));
      locationTextField.setText(UserPreferences.readPref(Export.prefSettingsFileName));
      locationChooserButton.setToolTipText(ui.locationCmd);
      locationChooserButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { locationChooserButtonAction(e); }
         } );
   	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
      buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionCancel(); } } );
      actionButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionExport(); } } );
      UIUtilities.addFastKeys(buttonList);
      UIUtilities.makeBold(actionButton);
      getRootPane().setDefaultButton(actionButton);
      }

   void addContols() {
   	locationInnerPanel.add(locationPromptLabel);
      locationInnerPanel.add(locationTextField);
      locationInnerPanel.add(locationChooserButton);
   	locationPanel.add(locationInnerPanel);
   	locationPanel.add(locationDetails1Label);
   	locationPanel.add(locationDetails2Label);
   	basePanel.add(locationPanel);
      basePanel.add(Box.createRigidArea(new Dimension(0,10)));

   	buttonPanel.add(cancelButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
   	buttonPanel.add(actionButton);
   	basePanel.add(buttonPanel);
      }

   void initValues() {
      //set file name here
      }
   
   //
   // Callback Methods (Event Actions)
   //

   public void locationChooserButtonAction(ActionEvent e) {
      JFileChooser destFileChooser = new JFileChooser();
      destFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      destFileChooser.setCurrentDirectory(
         new File(locationTextField.getText()));
      int returnStatus =
         destFileChooser.showDialog(this, ui.locationCmd);
      if (returnStatus == JFileChooser.APPROVE_OPTION)
         locationTextField.setText(
            destFileChooser.getSelectedFile().getAbsolutePath());
      }


   // Buttons

   void actionCancel() {
      this.dispose();
      }

   void actionExport() {
      if (new ExportDataModel().doExport(locationTextField.getText()))
         {
            JOptionPane.showMessageDialog(this, ui.msgSuccess);
            this.dispose();
            }
         else
            JOptionPane.showMessageDialog(this, "Error!!!!");
      }
   
   }
