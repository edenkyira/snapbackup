////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// UserPreferences.java                                                       //
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
// User Preferences:                                                          //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.utilities.settings;

import java.util.Locale;
import java.util.Vector;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import com.snapbackup.ui.SnapBackupFrame;

public class UserPreferences {

   static Preferences prefs =
      Preferences.userNodeForPackage(UserPreferences.class);
   //   Preferences.userNodeForPackage(new UserPreferences().getClass());
   static final String prefix = SystemAttributes.prefPrefix;
   static final String locale = SystemAttributes.prefLocale;
   static final String prefProfileName = "profilename";
   static final String prefValueNotFound = "NOT FOUND";
   static String cmdLineProfileName = null;

   static String boolean2String (boolean tf) {
      return (tf ? SystemAttributes.trueStr : SystemAttributes.falseStr);
      }

   static boolean string2Boolean (String tf) {
      return (tf.compareTo(SystemAttributes.trueStr) == 0);
      }

   public static String readLocalePref() {
      return prefs.get(prefix + locale, Locale.getDefault().getLanguage());
      }

   public static void saveLocalePref(String localeCode) {
      prefs.put(prefix + locale, localeCode);
      }

   public static String readPref(String prefName) {
      //Returns user's preference.  If none, default app property is returned.
      return prefs.get(prefix + prefName.toLowerCase(), AppProperties.getProperty(prefName));
      }

   public static boolean readBooleanPref(String prefName) {
      //Returns user's preference.  If none, default app property is returned.
      //return string2Boolean(readPref(prefName));
      String fallback = boolean2String(AppProperties.getProperty(prefName).equals(
         AppProperties.getProperty("True")));
      return string2Boolean(prefs.get(prefix + prefName.toLowerCase(), fallback));
      }

   public static void savePref(String prefName, String prefValue) {
      //Stores user's preference.
      prefs.put(prefix + prefName.toLowerCase(), prefValue);
      }

   public static void savePref(String prefName, boolean prefValue) {
      //Stores user's preference.
      savePref(prefName, boolean2String(prefValue));
      }

   public static void deletePref(String prefName) {
      //Erases user's preference, if it exists.
      prefs.remove(prefix + prefName.toLowerCase());
      }

   
   // Profiles

   public static void setCmdLineProfileName(String profileName) {
      cmdLineProfileName = profileName.toLowerCase();
      }

   static String profilePrefix(String profileName) {
      return SystemAttributes.prefProfilePrefix + profileName.toLowerCase() +
         SystemAttributes.prefProfilePostfix;
      }

   static String profilePrefix() {
      String profileName = cmdLineProfileName;
      if (profileName == null)
         profileName = SnapBackupFrame.current.getCurrentProfileName();
      return profilePrefix(profileName);
      }

   public static String getCurrentProfileName() {
      return cmdLineProfileName == null ?
         SnapBackupFrame.current.getCurrentProfileName() : cmdLineProfileName;
      }

   public static boolean profileInDB() {
      return prefs.get(profilePrefix() + prefProfileName, null) != null;
      }
    
   public static boolean profileInDB(String profileName) {
      return prefs.get(profilePrefix(profileName) + prefProfileName, null) != null;
      }
    
   public static String readProfilePref(String prefName) {
      //Returns user's preference.  If none, default app property is returned.
      return prefs.get(profilePrefix() + prefName.toLowerCase(),
              AppProperties.getProperty(prefName));
      }

   public static void saveProfilePref(String prefName, String prefValue) {
       //Stores user's preference.
       prefs.put(profilePrefix() + prefName.toLowerCase(), prefValue);
       }

   public static void saveProfilePref(String prefName, boolean prefValue) {
       //Stores user's preference.
       prefs.put(profilePrefix() + prefName.toLowerCase(), boolean2String(prefValue));
       }

   public static void deleteProfilePref(String prefName) {
      //Erases user's preference, if it exists.
      prefs.remove(profilePrefix() + prefName.toLowerCase());
      }

   public static String[] getAllKeys() {
      String[] allKeys;
      try {
         allKeys = prefs.keys();
         java.util.Arrays.sort(allKeys);
         }
      catch (BackingStoreException e) {
         allKeys = new String[0];
         }
      return allKeys;
      }

   public static Vector<String> getProfileNames() {
      Vector<String> names = new Vector<String>();
      for (String key : getAllKeys())
         if (key.endsWith(SystemAttributes.prefChar + prefProfileName))
            names.add(prefs.get(key, prefValueNotFound));
      //String[] allKeys = getAllKeys();
      //for (int count = 0; count < allKeys.length; count++)
      //   if (allKeys[count].endsWith(SystemAttributes.prefChar + prefProfileName))
      //      names.add(prefs.get(allKeys[count], prefValueNotFound));
      return names;
      }

   public static void deleteProfile() {
      for (String key : getAllKeys())
         if (key.startsWith(profilePrefix()))
            prefs.remove(key);
      //String[] keys = getAllKeys();
      //for (int count = 0; count < keys.length; count++)
      //   if (keys[count].startsWith(profilePrefix()))
      //      prefs.remove(keys[count]);
      }

   public static void upgradeOldPrefs() {
      if (prefs.get("SnapBackup~AppVersion", "").startsWith("2.5")) {
         prefs.remove("SnapBackup~AppVersion");                //old
         savePref("AppVersion", SystemAttributes.appVersion);  //new
         for (String key : getAllKeys())
            if (key.startsWith("SnapBackup~")) {
               // "SnapBackup~%Main~SrcDataList" --> "snapbackup.~main~.srcdatalist"
               prefs.put(key.replaceFirst("~%Main~", ".~main~.").toLowerCase(),
                     prefs.get(key, ""));
               prefs.remove(key);
               }
         /*
         String[] keys = getAllKeys();
         for (int count = 0; count < keys.length; count++)
            if (keys[count].startsWith("SnapBackup~")) {
               // "SnapBackup~%Main~SrcDataList" --> "snapbackup.~main~.srcdatalist"
               prefs.put(keys[count].replaceFirst("~%Main~", ".~main~.").toLowerCase(),
                     prefs.get(keys[count], ""));
               prefs.remove(keys[count]);
               }
         */
         savePref("CurrentProfile",     prefs.get("snapbackup.~main~.profilecurrent", ""));
         savePref("~main~.ProfileName", prefs.get("snapbackup.~main~.profilecurrent", ""));
         prefs.remove("snapbackup.~main~.profilecurrent");
         }
      }

   //Export/Import
   public static String readPrefByKey(String key) {
      return prefs.get(key, prefValueNotFound);
      }

   public static void deleteAllPrefs() {
      for (String key : getAllKeys())
         prefs.remove(key);
       }

   public static void savePrefByKey(String key, String prefValue) {
      prefs.put(key, prefValue);
      }

   }
