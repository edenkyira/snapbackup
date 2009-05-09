////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// BackupProgressUIProperties.java                                            //
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
// Backup Progress UI Properties:                                             //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.backupprogress;

import com.snapbackup.utilities.settings.AppProperties;

public class BackupProgressUIProperties {

   //Constants
   public static final int progressMax = 20;

   //Backup Progress
   public static final String backupProgressTitle =      AppProperties.getProperty("BackupProgressTitle");
   public static final String backupProgressTag =        AppProperties.getPropertyPadded("BackupProgressTag");
   public static final String backupProgressFiles =      AppProperties.getPropertyPadded("BackupProgressFiles");
   public static final String backupProgressMemoryFree = AppProperties.getPropertyPadded("BackupProgressMemoryFree");
   public static final String backupProgressCancel =     AppProperties.getProperty("BackupProgressCancel");
   public static final String backupProgressDone =       AppProperties.getProperty("BackupProgressDone");
   public static final String backupProgressAborting =   AppProperties.getProperty("BackupProgressAborting");

   }
