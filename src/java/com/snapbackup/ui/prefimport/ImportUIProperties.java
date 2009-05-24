////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// ImportSettingsUIProperties.java                                                   //
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
// Import Settings UI Properties:                                                     //
//    This object..                                                           //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.prefimport;

import com.snapbackup.settings.AppProperties;

class ImportUIProperties {
   final String title =            AppProperties.getProperty("ImportTitle");

   final String locationTitle =    AppProperties.getPropertyPadded("ImportLocationTitle");
   final String locationPrompt =   AppProperties.getProperty("ImportLocationPrompt");
   final String locationCmd =      AppProperties.getProperty("ImportLocationCommand");
   final String locationDetails =  AppProperties.getProperty("ImportLocationDetails");
   final String locationWarning =  "<html><b>" +
         AppProperties.getProperty("ImportWarning") + "</b> " +
         AppProperties.getProperty("ImportWarningMessage") + "</html>";

   final String buttonCancel =     AppProperties.getProperty("ImportButtonCancel");
   final String buttonImport =     AppProperties.getProperty("ImportButtonAction");

   final String msgSuccess =       AppProperties.getProperty("ImportMsgSuccess");
   }
