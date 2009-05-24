////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// UserGuideUIProperties.java                                                 //
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
// User Guide UI Properties:                                                  //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.userguide;

import com.snapbackup.settings.AppProperties;
import com.snapbackup.settings.SystemAttributes;

public class UserGuideUIProperties {

   // Constants for the user guide.
   static final String userGuideURL =         "html/SnapBackupUserGuide.html";  // Folder: com/snapbackup/ui/userguid/html/
   static final String userGuideErrMsgMIME =  "text/html";
   static final double userGuideSizeScaleX =  0.75;  //75% of main window's width
   static final double userGuideSizeScaleY =  1.00;  //100% of main window's hieght
   static final String space =                SystemAttributes.space;
   static final String appVersion =           SystemAttributes.appVersion;

   // Data from the .properties file.
   final String userGuideHeader =      AppProperties.getProperty("UserGuideHeader");
   final String userGuideTitle =       AppProperties.getProperty("ApplicationTitle") + space + userGuideHeader;
   final String userGuideVersion =     AppProperties.getProperty("UserGuideVersion") + space + appVersion;
   final String userGuideButtonPrint = AppProperties.getProperty("UserGuideButtonPrint");
   final String userGuideButtonClose = AppProperties.getProperty("UserGuideButtonClose");
   final String userGuideErrMsg =      AppProperties.getProperty("UserGuideErrMsg");

   }
