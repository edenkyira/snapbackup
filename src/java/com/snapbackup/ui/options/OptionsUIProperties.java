////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// OptionsUIProperties.java                                                   //
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
// Options UI Properties:                                                     //
//    This object..                                                           //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.options;

import com.snapbackup.utilities.settings.AppProperties;

class OptionsUIProperties {
   final String title =                     AppProperties.getProperty("OptionsTitle");

   final String fileFormatTitle =           AppProperties.getPropertyPadded("OptionsFileFormatTitle");
   final String fileFormatExample =         AppProperties.getProperty("OptionsFileFormatExample");
   final String fileFormatSpacerPrompt =    AppProperties.getProperty("OptionsFileFormatNameSpacerPrompt");
   final String fileFormatSpacerList =      AppProperties.getProperty("OptionsFileFormatNameSpacerList");
   final String fileFormatOrderPrompt =     AppProperties.getProperty("OptionsFileFormatDateOrderPrompt");
   final String fileFormatOrderYear =       AppProperties.getProperty("OptionsFileFormatDateOrderYear");
   final String fileFormatOrderMonth =      AppProperties.getProperty("OptionsFileFormatDateOrderMonth");
   final String fileFormatOrderDay =        AppProperties.getProperty("OptionsFileFormatDateOrderDay");
   final String fileFormatOrderNone =       AppProperties.getProperty("OptionsFileFormatDateOrderNone");
   final String[] orderList =     {fileFormatOrderYear,  fileFormatOrderMonth, fileFormatOrderDay};
   final String[] orderListNone = {fileFormatOrderYear,  fileFormatOrderMonth, fileFormatOrderDay, fileFormatOrderNone};
   final String fileFormatYearPrompt =      AppProperties.getProperty("OptionsFileFormatDateYearPrompt");
   final String fileFormatYear2Digits =     AppProperties.getProperty("OptionsFileFormatDateYear2Digits");
   final String fileFormatYear4Digits =     AppProperties.getProperty("OptionsFileFormatDateYear4Digits");
   final String fileFormatSeparatorPrompt = AppProperties.getProperty("OptionsFileFormatDateSeparatorPrompt");
   final String fileFormatSeparatorList =   AppProperties.getProperty("OptionsFileFormatDateSeparatorList");

   final String overwriteTitle =            AppProperties.getPropertyPadded("OptionsOverwriteTitle");
   final String overwriteBackupPrompt =     AppProperties.getProperty("OptionsOverwriteBackupFile");
   final String overwriteArchivePrompt =    AppProperties.getProperty("OptionsOverwriteArchiveFile");

   final String numRowsTitle =              AppProperties.getPropertyPadded("OptionsNumberOfRowsTitle");
   final String numRowsSrcPrompt =          AppProperties.getPropertyPadded("SourcePrompt");
   final String numRowsLogPrompt =          " " + AppProperties.getProperty("LogTitle") + ": ";
      
   final String buttonCancel =              AppProperties.getProperty("OptionsButtonCancel");
   final String buttonDefaults =            AppProperties.getProperty("OptionsButtonDefaults");
   final String buttonOk =                  AppProperties.getProperty("OptionsButtonOk");
   }