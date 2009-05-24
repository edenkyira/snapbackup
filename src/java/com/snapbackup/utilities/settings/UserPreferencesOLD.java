package com.snapbackup.utilities.settings;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import com.snapbackup.settings.SystemAttributes;
import com.snapbackup.settings.AppProperties;
import com.snapbackup.settings.UserPreferences;

public class UserPreferencesOLD {

   static Preferences prefsOLD =
      Preferences.userNodeForPackage(UserPreferencesOLD.class);
   static final String prefix = SystemAttributes.prefPrefix;
   static final String prefValueNotFound = "NOT FOUND";

   static String readPrefOLD(String prefName) {
      //Returns user's preference.  If none, default app property is returned.
      return prefsOLD.get(prefix + prefName.toLowerCase(), AppProperties.getProperty(prefName));
      }

   static void savePrefOLD(String prefName, String prefValue) {
      //Stores user's preference.
      prefsOLD.put(prefix + prefName.toLowerCase(), prefValue);
      }

   static String[] getAllKeysOLD() {
      String[] allKeys;
      try {
         allKeys = prefsOLD.keys();
         java.util.Arrays.sort(allKeys);
         }
      catch (BackingStoreException e) {
         allKeys = new String[0];
         System.out.println(e.getLocalizedMessage());
         }
      return allKeys;
      }

   static String readPrefByKeyOLD(String key) {
      return prefsOLD.get(key, prefValueNotFound);
      }

   public static boolean existsOLD(String prefName) {
      //Returns true if the preferene has a value.
      return !prefsOLD.get(prefix + prefName.toLowerCase(),
            prefValueNotFound).equals(prefValueNotFound);
      }

   public static void copyOldPrefsToNewLocation() {
      if (existsOLD("appversion") && !UserPreferences.exists("appversion"))
         for (String key : getAllKeysOLD())
            UserPreferences.savePrefByKey(key, readPrefByKeyOLD(key));
      if (existsOLD("appversion"))
         try {
            prefsOLD.removeNode();
            prefsOLD.flush();
            }
         catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            }
      }

   }
