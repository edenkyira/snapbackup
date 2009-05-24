////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// UserGuideDialog.java                                                       //
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
// User Guide Dialog:                                                         //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.userguide;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

import com.snapbackup.ui.UIUtilities;
import edu.jhu.apl.PrintUtilities;

public class UserGuideDialog extends JDialog {

   UserGuideUIProperties ui = new UserGuideUIProperties();

   //Define About Controls
   JPanel      headerPanel =  new JPanel();
   JPanel      buttonPanel =  new JPanel();
   JLabel      titleLabel =   new JLabel(ui.userGuideHeader);
   JLabel      versionLabel = new JLabel(ui.userGuideVersion);
   JScrollPane scrollPane =   new JScrollPane();
   JEditorPane html;
   JButton     printButton =  new JButton(ui.userGuideButtonPrint);
   JButton     closeButton =  new JButton(ui.userGuideButtonClose);
   JButton[]   buttonList =   { printButton, closeButton };

   public void initGUI(Dimension parentSize) {
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setTitle(ui.userGuideTitle);
      configureContols(parentSize.getWidth() * UserGuideUIProperties.userGuideSizeScaleX,
         parentSize.getHeight() * UserGuideUIProperties.userGuideSizeScaleY);
      addContols();
      setResizable(true);
      pack();
      }

   public void configureContols(double width, double height) {
      headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.PAGE_AXIS));
      UIUtilities.makeBold(titleLabel);
      UIUtilities.bumpUpFontSize(titleLabel, 3);
      titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      URL url = getClass().getResource(UserGuideUIProperties.userGuideURL);
      try {
         html = new JEditorPane(url);
         }
      catch (IOException e) {
         html = new JEditorPane(UserGuideUIProperties.userGuideErrMsgMIME,
            ui.userGuideErrMsg + UserGuideUIProperties.userGuideURL);
         }
      html.setEditable(false);
      html.setPreferredSize(new Dimension((int)width, (int)height));
      scrollPane.setViewportView(html);
      printButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) { printButtonAction(); }
          } );
      closeButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) { closeButtonAction(); }
          } );
      UIUtilities.addFastKeys(buttonList);
      UIUtilities.makeBold(closeButton);
      getRootPane().setDefaultButton(closeButton);
      getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               closeButtonAction(); } },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);
      }

   public void addContols() {
      headerPanel.add(titleLabel);
      headerPanel.add(versionLabel);
      getContentPane().add(headerPanel, BorderLayout.PAGE_START);
      getContentPane().add(scrollPane, BorderLayout.CENTER);
      buttonPanel.add(printButton);
      buttonPanel.add(closeButton);
      getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
      }

   public void printButtonAction() {
      new PrintUtilities(html).print();
      }

   public void closeButtonAction() {
      this.dispose();
      }

   }
