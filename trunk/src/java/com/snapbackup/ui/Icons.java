////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// Icons.java                                                                 //
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
// Icons:                                                                     //
//    This object holds icons for display in the UI.                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui;

import java.io.File;
import javax.swing.*;
import com.snapbackup.utilities.settings.SystemAttributes;

public class Icons {

   private static final Class iconClass = new Icons().getClass();
   public  static final ImageIcon snapBackupIcon = new ImageIcon(iconClass.getResource(UIProperties.snapBackupIconURL));
   public  static final ImageIcon logoIcon = new ImageIcon(iconClass.getResource(UIProperties.logoIconURL));
   private static final JFileChooser sysAccess =  new JFileChooser();
   private static final File[] rootDir =  File.listRoots();
   public  static final Icon driveIcon =  sysAccess.getIcon(rootDir[0]);
   public  static final Icon zipIcon = driveIcon;
      //sysAccess.getIcon(new File(iconClass.getResource('images/Logo.gif').getPath()));
      //FileSys.makeEmptyFile(UIProperties.zipIconFileName + '.html'));
   public  static final Icon folderIcon =
      //sysAccess.getIcon(new File(ProgramAttributes.appWorkingDir));  //bad on macs
      //sysAccess.getIcon(new File(ProgramAttributes.userHomeDir));  //works on win
      sysAccess.getIcon(new File(SystemAttributes.javaHomeDir));  //works on win

   public static ImageIcon langIcon(String localeCode) {
      return new ImageIcon(iconClass.getResource(
         UIProperties.langIconURLpre + localeCode + UIProperties.langIconURLpost));
      }

   }

