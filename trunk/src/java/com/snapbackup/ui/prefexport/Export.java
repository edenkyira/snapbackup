////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// Export.java                                                                //
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

import com.snapbackup.utilities.settings.SystemAttributes;
import com.snapbackup.utilities.settings.AppProperties;

class Export {
   //Constants
   public static final String prefSettingsFileName = "ExportDefaultFileName";
   public static final int    fileNameCols =         25;

   //Setup
   final String Settings =        AppProperties.getProperty("ExportSettings");
   final String defaultSettingsFileName =
         SystemAttributes.userHomeDir + SystemAttributes.fileSeparator +
         SystemAttributes.appName + Settings + ".xml";
   }
