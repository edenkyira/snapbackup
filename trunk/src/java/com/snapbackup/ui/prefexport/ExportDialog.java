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
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import com.snapbackup.ui.UIUtilities;

public class ExportDialog extends JDialog {

   //User Preference Keys
   static final String prefSettingsFileName = "SettingsFileName";

   //Define Controls
   ExportUIProperties ui = new ExportUIProperties();
   JPanel       exportSettingsPanel =  new JPanel();
   JPanel       fileNamePanel =        new JPanel();
   JLabel       fileNamePromptLabel =  new JLabel();
   JLabel       fileNameDetailsLabel = new JLabel();
   JPanel       buttonPanel =          new JPanel();
   JButton      cancelButton =         new JButton(ui.buttonCancel);
   JButton      actionButton =         new JButton(ui.buttonExport);
   JButton[]    buttonList =           { cancelButton, actionButton };

   public ExportDialog() {
      initGUI();
      setModal(true);
      setResizable(false);
      pack();
      }

   void initGUI() {
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setTitle(ui.title);
      configureContols();
      addContols();
      initValues();
      getContentPane().add(exportSettingsPanel);
      }

   void configureContols() {
      exportSettingsPanel.setLayout(new BoxLayout(exportSettingsPanel, BoxLayout.PAGE_AXIS));
      exportSettingsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

      fileNamePanel.setLayout(new BoxLayout(fileNamePanel, BoxLayout.PAGE_AXIS));
   	fileNamePanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createTitledBorder(ui.fileNameTitle),
         BorderFactory.createEmptyBorder(0, 5, 5, 5)));

   	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
      buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionCancel(); } } );
      actionButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { actionExport(); } } );
      UIUtilities.addFastKeys(buttonList);
      UIUtilities.makeBold(actionButton);
      getRootPane().setDefaultButton(actionButton);
      getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	actionCancel(); } },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);
      }

   void addContols() {
   	fileNamePanel.add(fileNamePromptLabel);
   	fileNamePanel.add(fileNameDetailsLabel);
   	exportSettingsPanel.add(fileNamePanel);
      exportSettingsPanel.add(Box.createRigidArea(new Dimension(0,10)));

   	buttonPanel.add(cancelButton);
      buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
   	buttonPanel.add(actionButton);
   	exportSettingsPanel.add(buttonPanel);
      }

   void initValues() {
      //set file name here
      }
   
   //
   // Callback Methods (Event Actions)
   //
   // Buttons

   void actionCancel() {
      this.dispose();
      }

   void actionExport() {
      //UserPreferences.savePref(ExportSettings.prefSpacer, (String)spacerDropDown.getSelectedItem());
      //String profileName =
      //   JOptionPane.showInputDialog(null, ui.profilesAddPrompt, ui.profilesAddTitle, JOptionPane.PLAIN_MESSAGE);

      this.dispose();
      }
   
   }
