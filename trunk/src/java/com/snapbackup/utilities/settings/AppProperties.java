////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// AppProperties.java                                                         //
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
// Application Properties:                                                    //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.utilities.settings;

import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

// Reads values from the poperties file and provides access to those
// values.  Additional properties with values may added at run-time.
public class AppProperties {

   static final Hashtable<String, String> supplimentalProperty =
      new Hashtable<String, String>();

   static ResourceBundle applicationResources =
      ResourceBundle.getBundle(SystemAttributes.appName,
      new Locale(UserPreferences.readLocalePref()));

   public static String getProperty(String propertyName) {
      //Reads a property from the ".properties" file.
      //String propertyValue;
      try {
         return applicationResources.getString(propertyName);
         }
      catch (MissingResourceException e) {
         String propertyValue = supplimentalProperty.get(propertyName);
         if (propertyValue == null)
            propertyValue = SystemAttributes.errMsgHeader + propertyName +
            SystemAttributes.errMsgMissingResource +
            SystemAttributes.appName;
         return propertyValue;
         }
      }

   public static String getPropertyPadded(String propertyName) {
      return " " + getProperty(propertyName) + " ";
      }

   public static String getPropertyHtml(String propertyName) {
      //return "<html>" + getProperty(propertyName).replaceAll("\\(c\\)", "&copy;") + "</html>";
      return "<html>" + getProperty(propertyName) + "</html>";
      }

   public static void addSupplimentalProperty(String propertyName, String propertyValue) {
      supplimentalProperty.put(propertyName, propertyValue);
      //Locale[] locales = Locale.getAvailableLocales();
      //System.out.println("Locale: " + applicationResources.getLocale().getDisplayName() + ", " + locales.length);
      //System.out.println("Locale: " + Locale.getDefault().getDisplayName());
      
      //ListResourceBundle x;
      }

   public static void reload() {
      applicationResources =
           ResourceBundle.getBundle(SystemAttributes.appName,
           new Locale(UserPreferences.readLocalePref()));
      }
   
   }
