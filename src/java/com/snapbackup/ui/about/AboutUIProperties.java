////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// AboutUIProperties.java                                                     //
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
// About UI Properties                                                        //
//    This object....                                                         //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.about;

import com.snapbackup.utilities.settings.AppProperties;
import com.snapbackup.utilities.settings.SystemAttributes;

public class AboutUIProperties {
   private static final String space = SystemAttributes.space;
   //
   //From .properties File
   //
   public final String aboutTitle =              AppProperties.getPropertyPadded("AboutTitle");
   public final String aboutAppTitle =           AppProperties.getProperty("ApplicationTitle");
   public final String aboutVersion =            AppProperties.getProperty("AboutVersion") + space + SystemAttributes.appVersion;
   public final String aboutCreatedBy =          AppProperties.getProperty("AboutCreatedBy") + space + SystemAttributes.appAuthors;
   public final String aboutTranslatedBy =       AppProperties.getProperty("AboutTranslatedBy");
   public final String aboutAnd =                AppProperties.getProperty("AboutAnd");
   public final String aboutCopyright =          AppProperties.getPropertyHtml("AboutCopyright");
   public final String aboutLicense =            AppProperties.getProperty("AboutLicense");
   public final String aboutConfigurationTitle = AppProperties.getPropertyPadded("AboutConfigurationTitle");
   public final String aboutDownload =           AppProperties.getProperty("AboutDownload");
   public final String aboutContact =            AppProperties.getProperty("AboutContactInfo");
   public final String aboutButtonWeb =          AppProperties.getProperty("AboutButtonWeb");
   public final String aboutButtonClose =        AppProperties.getProperty("AboutButtonClose");
   }
