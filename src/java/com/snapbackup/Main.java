////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// Main.java                                                                  //
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
// Copyright (c) 2007 Center Key Software                                     //
// Snap Backup is a trademark of Dem Pilafian                                 //
// http://www.snapbackup.com                                                  //
//                                                                            //
// Main Startup:                                                              //
//    This object launches the command line or GUI version of appliction.     //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.snapbackup.business.DataModel;
import com.snapbackup.ui.Application;
import com.snapbackup.ui.UIProperties;
import com.snapbackup.utilities.settings.AppProperties;
import com.snapbackup.utilities.settings.UserPreferences;

public class Main {

   //Run Snap Backup in either command line mode where the parameter is the profile name
   //or in GUI (Swing) mode if there are no parameters.
   public static void main(String[] argv) {
      if (argv.length > 0)
         DataModel.doCmdLineBackup(argv[0], argv.length > 1 ? argv[1] : null);
      else {
         AppProperties.addSupplimentalProperty(DataModel.prefSkinName,
            UIManager.getSystemLookAndFeelClassName());  //make system l&f the default
         try {
            UIManager.setLookAndFeel(UserPreferences.readPref(DataModel.prefSkinName));
            }
         catch (Exception e) {
            JOptionPane.showMessageDialog(null, UIProperties.skinErrMsg);
            UserPreferences.deletePref(DataModel.prefSkinName);
            }
         new Application();
         }
      }

   }

