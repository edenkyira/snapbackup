////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// DataModel.java                                                             //
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
// Data Model:                                                                //
//    This object is the business logic of MVC.                               //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.business;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.DefaultListModel;

import com.snapbackup.logger.Logger;
import com.snapbackup.ui.SnapBackupFrame;
import com.snapbackup.ui.SwingWorker;
import com.snapbackup.ui.UIProperties;
import com.snapbackup.ui.UIUtilities;
import com.snapbackup.ui.backupprogress.BackupProgressDialog;
import com.snapbackup.ui.options.Options;
import com.snapbackup.utilities.datestamp.DateStamp;
import com.snapbackup.utilities.filesys.FileSys;
import com.snapbackup.utilities.settings.AppProperties;
import com.snapbackup.utilities.settings.SystemAttributes;
import com.snapbackup.utilities.settings.UserPreferences;
import com.snapbackup.utilities.zip.ZipEngine;

public class DataModel {

   //Top-lever user prefereences
   static final String prefAppVersion =     "AppVersion";
   static final String prefShowLanguages =  "ShowLanguages";
   static final String prefFiltersEnabled = "FiltersEnabled";
   static final String prefShowProfiles =   "ShowProfiles";
   public static final String prefCurrentProfile = "CurrentProfile";
   public static final String prefSkinName =       "SkinName";

   //User preferences associated with each profile 
   static final String prefProfileName =    "ProfileName";
   static final String prefSrcDataList =    "SrcDataList";
   static final String prefSrcDataIncludeList =       "SrcDataIncludeList";        //filter
   static final String prefSrcDataExcludeList =       "SrcDataExcludeList";        //filter
   static final String prefSrcDataExcludeFolderList = "SrcDataExcludeFolderList";  //filter
   static final String prefSrcDataExcludeSizeList =   "SrcDataExcludeSizeList";    //filter
   static final String prefBackupDirBase =  "BackupDirBase";
   static final String prefBackupDir =      "BackupDir";
   static final String prefBackupName =     "BackupName";
   static final String prefArchiveChecked = "ArchiveChecked";
   static final String prefArchiveDirBase = "ArchiveDirBase";
   static final String prefArchiveDir =     "ArchiveDir";
   static final String prefArchiveDirWin =  "ArchiveDirWin";

   //Constants
   static final int    kb =                1024;
   static final String nullStr =           SystemAttributes.nullStr;
   static final String space =             SystemAttributes.space;
   static final String tab =               SystemAttributes.tab;
   static final String divider =           SystemAttributes.dividerStr;
   static final String fs =                SystemAttributes.fileSeparator;
   static final String dataPrompt =        SystemAttributes.dataPrompt;
   static final String sizePre =           space + space + "[";
   static final String sizePost =          space + AppProperties.getProperty("FilterMarkerUnits") + "]";
   static final String zipExtension =      AppProperties.getProperty("StandardZipExtension");  //usually: ".zip"
   static final String replacementChar =   AppProperties.getProperty("TextReplacementCharacter");  //usually: "%"
   static final String trueStr =           AppProperties.getProperty("True");  //usually: "true"
   static final String filterMarkerPre =   AppProperties.getProperty("FilterMarkerPre");    //usually: "["
   static final String filterMarkerPost =  AppProperties.getProperty("FilterMarkerPost");   //usually: "]"
   static final String filterMarkerSkip =  AppProperties.getProperty("FilterMarkerSkip");   //usually: "-"
   static final String filterMarkerSize =  AppProperties.getProperty("FilterMarkerSize");   //usually: ">"
   static final String filterMarkerUnits = AppProperties.getPropertyPadded("FilterMarkerUnits");  //usually: "KB"

   
   //User selections not owned by a UI control
   static List<String>
      zipItemList, zipIncludeList, zipExcludeList, zipExcludeFolderList, zipExcludeSizeList;

   //Command-line data
   static String  backupDir;
   static String  backupName;
   static String  backupPath;
   static boolean doArchive;
   static String  archiveDir;
   static String  archivePath;
   static boolean filtersOn;

   public static Vector<String> getProfilesNames() {
      Vector<String> names = UserPreferences.getProfileNames();
      if (names.size() == 0)
         names.add(AppProperties.getProperty(prefCurrentProfile));
      return names;
      }

   public static String saveSettings(SnapBackupFrame f) {
      UserPreferences.savePref(prefAppVersion, SystemAttributes.appVersion);
      UserPreferences.savePref(prefCurrentProfile, f.getCurrentProfileName());
      UserPreferences.saveProfilePref(prefProfileName, f.getCurrentProfileName());
      UserPreferences.saveProfilePref(prefSrcDataList, list2StrList(zipItemList));
      UserPreferences.saveProfilePref(prefSrcDataIncludeList, list2StrList(zipIncludeList));
      UserPreferences.saveProfilePref(prefSrcDataExcludeList, list2StrList(zipExcludeList));
      UserPreferences.saveProfilePref(prefSrcDataExcludeFolderList, list2StrList(zipExcludeFolderList));
      UserPreferences.saveProfilePref(prefSrcDataExcludeSizeList, list2StrList(zipExcludeSizeList));
      UserPreferences.saveProfilePref(prefBackupDir, f.getDestBackupDirTextField().getText());
      UserPreferences.saveProfilePref(prefBackupName, f.getDestBackupNameTextField().getText());
      UserPreferences.saveProfilePref(prefArchiveChecked, f.getDestArchivePromptCheckBox().isSelected());
      UserPreferences.saveProfilePref(prefArchiveDir, f.getDestArchiveDirTextField().getText());
      return UIProperties.current.msgSettingsSaved;
      }

   public static void saveShowLanguagesSetting(SnapBackupFrame f) {
      UserPreferences.savePref(prefShowLanguages, f.getShowLanguages());
      }

   public static void saveFiltersEnabledSetting(SnapBackupFrame f) {
      UserPreferences.savePref(prefFiltersEnabled, f.getFiltersEnabled());
      }

   public static void saveShowProfilesSetting(SnapBackupFrame f) {
      UserPreferences.savePref(prefShowProfiles, f.getProfilesEnabled());
      }

   public static void saveSkinSetting(String skinName) {
      UserPreferences.savePref(prefSkinName, skinName);
      }

   public static String restoreDefaultSettings(SnapBackupFrame f) {
      zipItemList = strList2List(AppProperties.getProperty(prefSrcDataList));
      zipIncludeList =       strList2List(nullStr, zipItemList.size());
      zipExcludeList =       strList2List(nullStr, zipItemList.size());
      zipExcludeFolderList = strList2List(nullStr, zipItemList.size());
      zipExcludeSizeList =   strList2List(nullStr, zipItemList.size());
      buildZipListModel(f.getSrcZipListModel(), f);
      f.getDestBackupDirTextField().setText(AppProperties.getProperty(prefBackupDir));
      f.getDestBackupNameTextField().setText(AppProperties.getProperty(prefBackupName));
      f.getDestArchivePromptCheckBox().setSelected(AppProperties.getProperty(prefArchiveChecked).compareTo(trueStr) == 0);
      f.getDestArchiveDirTextField().setText(AppProperties.getProperty(prefArchiveDir));
      updateControls(f);
      return UIProperties.current.msgSettingsRestored;
      }

   public static void updateSrcButtons(SnapBackupFrame f) {
       boolean itemSelected = f.getSrcZipList().getSelectedIndex() != -1;
       f.getSrcRemoveButton().setEnabled(itemSelected);
       f.getSrcFilterButton().setEnabled(itemSelected);
       f.getSrcFilterButton().setVisible(f.getFiltersEnabled());
       }

   public static void updateDestPaths(SnapBackupFrame f) {
      f.getDestBackupPathLabel().setText(calcDestBackupPath(f));
      f.getDestArchivePathLabel().setText(calcDestArchivePath(f));
      }

   public static void updateArchiveDir(SnapBackupFrame f) {
      final boolean archiveOn = f.getDestArchivePromptCheckBox().isSelected();
      f.getDestArchiveDirTextField().setEnabled(archiveOn);
      f.getDestArchiveChooserButton().setEnabled(archiveOn);
      f.getDestArchiveTagLabel().setEnabled(archiveOn || !SystemAttributes.evilWinSys);  //Fix disappearing icon problem on Macs and Linux
      f.getDestArchivePathLabel().setEnabled(archiveOn);
      f.getDestArchiveDirTextField().repaint();
      f.getDestArchiveChooserButton().repaint();
      f.getDestArchiveTagLabel().repaint();
      f.getDestArchivePathLabel().repaint();
      }

   public static void updateControls(SnapBackupFrame f) {
      updateSrcButtons(f);
      updateDestPaths(f);
      updateArchiveDir(f);
      }

   private static String list2StrList(List list) {
   	  //Converts multi-line data (List) into a single long string using the "splitStr" delimiter
      StringBuffer strList = new StringBuffer();
      ListIterator iter = list.listIterator();
      if (iter.hasNext())
         strList.append((String)iter.next());
      while (iter.hasNext()) {
         strList.append(SystemAttributes.splitStr + iter.next());
         }
      return strList.toString();
      }

   private static List<String> strList2List(String strList, int minSize) {
   	  //Creates a List of lines from a string of multi-line data delimited with "splitStr"
      List<String> list = 
            new ArrayList<String>(Arrays.asList(strList.split(SystemAttributes.splitStr)));
      if (strList.equals(nullStr))
         list = new ArrayList<String>();
      while (list.size() < minSize)
         list.add(nullStr);
      return list;
      }

   private static List<String> strList2List(String strList) {
       return strList2List(strList, 0);
       }

   public static void addZipItem(String zipItem, SnapBackupFrame f) {
      zipItemList.add(zipItem);  //add to data store
      zipIncludeList.add(nullStr);
      zipExcludeList.add(nullStr);
      zipExcludeFolderList.add(nullStr);
      zipExcludeSizeList.add(nullStr);
      f.getSrcZipListModel().addElement(zipItem);  //add to UI control
      f.getSrcZipList().setSelectedIndex(zipItemList.size() - 1);
      }           

   public static void removeCurrentZipItem(SnapBackupFrame f) {
      int loc = f.getSrcZipList().getSelectedIndex();
      zipItemList.remove(loc);  //remove from data store
      zipIncludeList.remove(loc);
      zipExcludeList.remove(loc);
      zipExcludeFolderList.remove(loc);
      zipExcludeSizeList.remove(loc);
      f.getSrcZipListModel().remove(loc);  //remove from UI control
      if (zipItemList.size() == 0)
         f.getSrcRemoveButton().setEnabled(false);
      else
         f.getSrcZipList().setSelectedIndex(
            Math.min(loc, zipItemList.size() - 1));
      }

   /*
   private static boolean elemExists(List list, int loc) {
      return !((String)list.get(loc)).equals(nullStr);
      }
   */

   public static String buildZipListLine(String zipItem, String includeFilter,
   		String excludeFilter, String excludeFoldersFilter, String sizeFilter,
   		SnapBackupFrame f) {
      String line = zipItem;
      if (f.getFiltersEnabled()) {
         line = line + tab;
         if (!includeFilter.equals(nullStr))
            line = line + filterMarkerPre + includeFilter + filterMarkerPost + space + space;
         String exclude = nullStr;
         boolean first = true;
         if (!excludeFilter.equals(nullStr)) {
            exclude = exclude + (first ? nullStr : ", ") + excludeFilter;
            first = false;
            }
         if (!excludeFoldersFilter.equals(nullStr)) {
            exclude = exclude + (first ? nullStr : ", ") + fs + excludeFoldersFilter + fs;
            first = false;
            }
         if (!sizeFilter.equals(nullStr)) {
            exclude = exclude + (first ? nullStr : ", ") + filterMarkerSize +
               sizeFilter + filterMarkerUnits;
            first = false;
            }
         if (!first)
            line = line + filterMarkerSkip + filterMarkerPre + exclude + filterMarkerPost;
         }
      return line;
      }

   public static void buildZipListModel(DefaultListModel zipListModel, SnapBackupFrame f) {
      int loc;
      zipListModel.clear();
      ListIterator iter = zipItemList.listIterator();
      while (iter.hasNext()) {
         loc = iter.nextIndex();
         zipListModel.addElement(buildZipListLine((String)iter.next(),
            zipIncludeList.get(loc), zipExcludeList.get(loc),
            zipExcludeFolderList.get(loc), zipExcludeSizeList.get(loc), f));
         }
      }

   public static String getCurrentZipItem(SnapBackupFrame f) {
      return zipItemList.get(f.getSrcZipList().getSelectedIndex());
      }
   
   public static String getCurrentZipIncludeFilter(SnapBackupFrame f) {
      return zipIncludeList.get(f.getSrcZipList().getSelectedIndex());
      }
    
   public static String getCurrentZipExcludeFilter(SnapBackupFrame f) {
      return zipExcludeList.get(f.getSrcZipList().getSelectedIndex());
      }
    
   public static String getCurrentZipExcludeFolderFilter(SnapBackupFrame f) {
      return zipExcludeFolderList.get(f.getSrcZipList().getSelectedIndex());
      }
    
   public static String getCurrentZipExcludeSizeFilter(SnapBackupFrame f) {
      return zipExcludeSizeList.get(f.getSrcZipList().getSelectedIndex());
      }

   public static void setCurrentZipFilter(String includeFilter,
   		String excludeFilter, String excludeFolderFilter, String sizeFilter, SnapBackupFrame f) {
      int loc = f.getSrcZipList().getSelectedIndex();
      zipIncludeList.set(loc, includeFilter);
      zipExcludeList.set(loc, excludeFilter);
      zipExcludeFolderList.set(loc, excludeFolderFilter);
      zipExcludeSizeList.set(loc, sizeFilter);
      f.getSrcZipListModel().set(loc, buildZipListLine(getCurrentZipItem(f),
         includeFilter, excludeFilter, excludeFolderFilter, sizeFilter, f));
      }
    
   private static void loadZipList(SnapBackupFrame f) {
      //Zip list data store
      zipItemList = strList2List(UserPreferences.readProfilePref(prefSrcDataList));
      zipIncludeList = strList2List(UserPreferences.readProfilePref(prefSrcDataIncludeList), zipItemList.size());
      zipExcludeList = strList2List(UserPreferences.readProfilePref(prefSrcDataExcludeList), zipItemList.size());
      zipExcludeFolderList = strList2List(UserPreferences.readProfilePref(prefSrcDataExcludeFolderList), zipItemList.size());
      zipExcludeSizeList = strList2List(UserPreferences.readProfilePref(prefSrcDataExcludeSizeList), zipItemList.size());

      //Zip list ui control
      if (f != null)
         buildZipListModel(f.getSrcZipListModel(), f);
      }

   public static void loadProfile(SnapBackupFrame f) {
      loadZipList(f);
      f.getDestBackupDirTextField().setText(UserPreferences.readProfilePref(prefBackupDir));
      f.getDestBackupNameTextField().setText(UserPreferences.readProfilePref(prefBackupName));
      f.getDestArchivePromptCheckBox().setSelected(UserPreferences.readProfilePref(prefArchiveChecked).compareTo(trueStr) == 0);
      f.getDestArchiveDirTextField().setText(UserPreferences.readProfilePref(prefArchiveDir));
      updateControls(f);
      }
   
   public static void initSettings(SnapBackupFrame f) {
      initSupplimentalSettings();
      //SnapBackupFrame.setProfilesDropDown(UserPreferences.readPref(prefProfileList).split(SystemAttributes.splitStr));
      f.initLanguagesUI(UserPreferences.readBooleanPref(prefShowLanguages));
      f.initFiltersUI(UserPreferences.readBooleanPref(prefFiltersEnabled));
      f.initProfilesUI(UserPreferences.readBooleanPref(prefShowProfiles));
      f.selectProfile(UserPreferences.readPref(prefCurrentProfile));
      loadProfile(f);  //usually redudent but needed in case profile happens to be 1st on the list
      }

   public static void initSettings() {
   	//Command-line
      initSupplimentalSettings();
      loadZipList(null);
      }

   public static void initSupplimentalSettings() {
      String backupDir = fs +
         AppProperties.getProperty(prefBackupDirBase) + fs +
         SystemAttributes.userName + fs;
      String archiveDir = fs + AppProperties.getProperty(prefArchiveDirBase).replace(
         replacementChar.charAt(0), fs.charAt(0)) + fs;
      if (SystemAttributes.evilWinSys) {
         backupDir = SystemAttributes.evilWinDrive + backupDir;
         archiveDir = AppProperties.getProperty(prefArchiveDirWin);
         }
      AppProperties.addSupplimentalProperty(prefSrcDataList, SystemAttributes.userHomeDir);
      AppProperties.addSupplimentalProperty(prefSrcDataIncludeList, nullStr);
      AppProperties.addSupplimentalProperty(prefSrcDataExcludeList, nullStr);
      AppProperties.addSupplimentalProperty(prefSrcDataExcludeFolderList, nullStr);
      AppProperties.addSupplimentalProperty(prefSrcDataExcludeSizeList, nullStr);
      AppProperties.addSupplimentalProperty(prefBackupDir, backupDir);
      AppProperties.addSupplimentalProperty(prefArchiveDir, archiveDir);
      AppProperties.addSupplimentalProperty(Options.prefSpacer, Options.spacerDefault);
      AppProperties.addSupplimentalProperty(Options.prefOrder, Options.orderDefault);
      AppProperties.addSupplimentalProperty(Options.prefYear, Options.yearDefault);
      AppProperties.addSupplimentalProperty(Options.prefSeparator, Options.separatorDefault);
      AppProperties.addSupplimentalProperty(Options.prefAskBackup, Options.askBackupDefault);
      AppProperties.addSupplimentalProperty(Options.prefAskArchive, Options.askArchiveDefault);
      AppProperties.addSupplimentalProperty(Options.prefNumRowsSrc, Options.numRowsSrcDefault);
      AppProperties.addSupplimentalProperty(Options.prefNumRowsLog, Options.numRowsLogDefault);
      }

   private static String calcDestPath(String backupDir, String backupName) {
      if (!backupDir.endsWith(fs))
         backupDir = backupDir + fs;
      return backupDir + backupName + UserPreferences.readPref(Options.prefSpacer) +
         DateStamp.todaysDateStamp() + zipExtension;
      }

   private static String calcDestPath(SnapBackupFrame f, String dir) {
      return calcDestPath(dir, f.getDestBackupNameTextField().getText());
      }

   private static String calcDestBackupPath(SnapBackupFrame f) {
      return calcDestPath(f, f.getDestBackupDirTextField().getText());
      }

   private static String calcDestBackupPath() {
      return calcDestBackupPath(SnapBackupFrame.current);
      }

   private static String calcDestArchivePath(SnapBackupFrame f) {
      return calcDestPath(f, f.getDestArchiveDirTextField().getText());
      }

   private static String calcDestArchivePath() {
      return calcDestArchivePath(SnapBackupFrame.current);
      }

   /*
   public static void copyFile(String fromFileName, String toFileName) {
      File to =   new File(toFileName);
      File from = new File(fromFileName);
      Logger.logMsg(UIProperties.logMsgCopyArchive + toFileName);
      if (false)
         Logger.logMsg(UIProperties.err08ArchiveProblem);
      }
   */

   public static void exit() {
      System.exit(0);
      }

   static Calendar startBackup;
   public static void logTimeStart() {
      startBackup = Calendar.getInstance();
      }
   public static void logTimeEnd() {
      long elapsedMills =
         Calendar.getInstance().getTimeInMillis() - startBackup.getTimeInMillis();
      long minutes = elapsedMills/60000;
      Locale locale = new Locale(UserPreferences.readLocalePref());
      NumberFormat nf = NumberFormat.getNumberInstance(locale);
      NumberFormat nfp = NumberFormat.getPercentInstance(locale);
      NumberFormat nfkb = NumberFormat.getNumberInstance(locale);
      nf.setMaximumFractionDigits(2);
      nfkb.setMaximumFractionDigits(0);
      nfp.setMinimumFractionDigits(1);
      nfp.setMaximumFractionDigits(1);
      String seconds = nf.format((elapsedMills - 60000*(elapsedMills/60000))/1000.0);
      String memory = nfp.format(1.0*Runtime.getRuntime().totalMemory()/Runtime.getRuntime().maxMemory());
      String memoryKB = nfkb.format(Runtime.getRuntime().totalMemory()/kb);
      Logger.logMsg(UIProperties.current.logMsgMemoryUsed + memory +
         sizePre + memoryKB + sizePost + space + divider + space +
         UIProperties.current.logMsgElapsedTime + space +
         minutes + UIProperties.current.logMsgMinutes +
         seconds + UIProperties.current.logMsgSeconds
         );
      }

   public static void doBackupNow() {
      final ZipEngine zip = new ZipEngine();
      final BackupProgressDialog backupProgress = new BackupProgressDialog(zip);
      zip.setBackupProgressDialog(backupProgress);
      final SwingWorker worker = new SwingWorker() {
         public Object construct() {
         	logTimeStart();
         	Logger.spacer();
            Logger.logMsg(UIProperties.current.logMsgStart);
            List[] filterInfo = {
               zipIncludeList, zipExcludeList, zipExcludeFolderList, zipExcludeSizeList };
            zip.zipItems(zipItemList, calcDestBackupPath(),
               SnapBackupFrame.current.getFiltersEnabled(), filterInfo);
            if (SnapBackupFrame.current.getDestArchivePromptCheckBox().isSelected() && !zip.isAbortSet())
               FileSys.copyFile(calcDestBackupPath(), calcDestArchivePath(), backupProgress,
                  zip, UserPreferences.readPref(Options.prefAskArchive).equals(Options.askNo));
            if (zip.isAbortSet())
            	Logger.logMsg(UIProperties.current.logMsgAborted);
         	logTimeEnd();
            Logger.logMsg(UIProperties.current.logMsgEnd);
            SnapBackupFrame.current.getLogScrollPane().getHorizontalScrollBar().setValue(0);
            return UIProperties.current;  //return object not used
            }
         public void finished() {
            if (!zip.isAbortSet()) {
               backupProgress.done();
               SnapBackupFrame.current.getExitButton().grabFocus();
               }
            TimerTask task = new TimerTask() {
               public void run() { backupProgress.dispose(); };
               };
            new Timer().schedule(task, 800);  //0.8 second delay
            }
         };
      worker.start();
      UIUtilities.centerDialog(backupProgress);
      }

   static void loadCmdLineData() {
      backupDir =   UserPreferences.readProfilePref(prefBackupDir);
      backupName =  UserPreferences.readProfilePref(prefBackupName);
      backupPath =  calcDestPath(backupDir, backupName);
      doArchive =   UserPreferences.readProfilePref(prefArchiveChecked).compareTo(trueStr) == 0;
      archiveDir =  UserPreferences.readProfilePref(prefArchiveDir);
      archivePath = calcDestPath(archiveDir, backupName);
      filtersOn =   UserPreferences.readBooleanPref(prefFiltersEnabled);
      }

   public static void doCmdLineBackup(String profileName, String logFile) {
      new UIProperties();
   	final ZipEngine zip = new ZipEngine();
   	if (profileName.equals(SystemAttributes.cmdLineDefaultProfile))
   		profileName = UserPreferences.readPref(prefCurrentProfile);
      UserPreferences.setCmdLineProfileName(profileName);
      initSettings();
      Logger.initOutput();
      Logger.logMsg(UIProperties.current.headerCmdLine);
   	logTimeStart();
      Logger.logMsg(UIProperties.current.logMsgStart);
      Logger.logMsg(UIProperties.current.logMsgProfile + profileName);
      loadCmdLineData();
      List[] filterInfo = {
         zipIncludeList, zipExcludeList, zipExcludeFolderList, zipExcludeSizeList };
      if (UserPreferences.profileInDB())
         zip.zipItems(zipItemList, backupPath, filtersOn, filterInfo);
      else
      	zip.abortBackup(UIProperties.current.err30ProfileNotFound + dataPrompt + profileName);
      if (doArchive && !zip.isAbortSet())
         FileSys.copyFile(backupPath, archivePath, zip);
      if (zip.isAbortSet())
      	Logger.logMsg(UIProperties.current.logMsgAborted);
   	logTimeEnd();
      Logger.logMsg(UIProperties.current.logMsgEnd);
      }

   }
