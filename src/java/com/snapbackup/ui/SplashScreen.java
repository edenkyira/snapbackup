////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// SplashScreen.java                                                          //
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
// Copyright (c) 2010 Center Key Software and Individual Contributors         //
// Snap Backup is a registered trademark of Center Key Software               //
// http://www.snapbackup.com                                                  //
//                                                                            //
// Splash Screen:                                                             //
//    This object is the small dialog box displayed at startup while the      //
//    application is loading.                                                 //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui;

import java.awt.Color;
import javax.swing.*;
import com.snapbackup.settings.SystemAttributes;
import com.snapbackup.settings.AppProperties;

public class SplashScreen extends JDialog {

   final String appTitle = AppProperties.getProperty("ApplicationTitle");
   final String splashHeader = SystemAttributes.headerPre + appTitle +
      SystemAttributes.headerMid + SystemAttributes.headerSplashTag +
      SystemAttributes.headerPost;

   public SplashScreen() {
      JPanel splashPanel = new JPanel();
      splashPanel.setLayout(new BoxLayout(splashPanel, BoxLayout.Y_AXIS));
      splashPanel.setBackground(Color.white);
      splashPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLineBorder(Color.black),
         BorderFactory.createEmptyBorder(0, 20, 5, 20)
         ));
      splashPanel.add(new JLabel(splashHeader));
      getContentPane().add(splashPanel);
      setResizable(false);
      setUndecorated(true);
      pack();
      setLocationRelativeTo(null);
      setVisible(true);
      //UIUtilities.centerDialog(this);
      }

   public void delete() {
      this.setVisible(false);
      this.dispose();
      }

   }
