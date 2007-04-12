////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// ZipEngine.java                                                             //
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
// Zip Engine:                                                                //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.utilities.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import com.snapbackup.logger.Logger;
import com.snapbackup.ui.UIProperties;
import com.snapbackup.ui.backupprogress.BackupProgressDialog;
import com.snapbackup.ui.options.Options;
import com.snapbackup.utilities.filesys.FileSys;
import com.snapbackup.utilities.settings.AppProperties;
import com.snapbackup.utilities.settings.SystemAttributes;
import com.snapbackup.utilities.settings.UserPreferences;
import com.snapbackup.utilities.string.Str;

public class ZipEngine {

   private final int    kb =                 1024;
   private final int    buffSize =           kb * 256;  //optimal size not known
   private final String nullStr =            SystemAttributes.nullStr;
   private final String space =              SystemAttributes.space;
   private final String tab =                SystemAttributes.tab;
   private final String newLine =            SystemAttributes.newLine;
   private final String fileSeparator =      SystemAttributes.fileSeparator;
   private final String dividerStr =         SystemAttributes.dividerStr;
   private final String dataPrompt =         SystemAttributes.dataPrompt;
   private final String zippingLogMsg =      tab + tab + UIProperties.current.logMsgZipping;
   private final String skippingLogMsg =     tab + tab + UIProperties.current.logMsgSkipping;
   private final String folderLogMsg =       tab + UIProperties.current.logMsgFolder;
   private final String regExReservedChars = "-+?()[]{}|$^<=.";
   private final String regExEscape =        "\\";
   private final String exclusionNote =      " -- " + AppProperties.getProperty("FilterRuleExcludeTitle");
   private final String sizePre =            space + space + "[";
   private final String sizePost =           space + AppProperties.getProperty("FilterMarkerUnits") + "]";
   private NumberFormat nf = NumberFormat.getNumberInstance(new Locale(UserPreferences.readLocalePref()));
   private boolean useRelativePaths = true;
   private String rootPath;
   private int rootPathLen;
   private File file;
   private FileOutputStream fileOut = null;
   private ZipOutputStream zipOut = null;
   private ZipEntry zipEntry;
   private boolean abortBackup;
   private int zipCount;
   private int byteCount;
   private byte[] data = new byte[buffSize];  //Data for read/write operations

   private java.lang.Runtime rt = Runtime.getRuntime();  //memory leak!!!
   //private long freeMem = Long.MAX_VALUE;
   //private long totalMem = 0;

   private boolean filterIncludeOn, filterExcludeOn, filterFolderOn, filterSizeOn;
   private Pattern filterIncludeP, filterExcludeP, filterFolderP;
   private int filterSize;
   
   private void initFilterInfo() {
      filterIncludeOn = false;
      filterExcludeOn = false;
      filterFolderOn =  false;
      filterSizeOn =    false;
      }

   String escapeChars(String s, char c) {
   	for (int loc = 0; loc < s.length(); loc++)
   	   if (s.charAt(loc) == c) {
            s = s.substring(0, loc) + regExEscape + s.substring(loc);
   	   	loc = loc + 1;
   	      }
   	return s;
      }

   private Pattern compileRegEx(String regEx) {
      Pattern P = null;
      for (int loc = 0; loc < regExReservedChars.length(); loc++)
         regEx = escapeChars(regEx, regExReservedChars.charAt(loc));
      try {
         P = Pattern.compile(regEx.replaceAll(", ", "|").replaceAll("\\*", ".*"));
         }
      catch (PatternSyntaxException e) {
         JOptionPane.showMessageDialog(null, UIProperties.current.err10CannotParseFilter +
               dataPrompt + regEx + dividerStr + e.getDescription());
         initFilterInfo();
         }
      return P;
      }
   
   private void updateFilterInfo(List[] filterInfo, int loc) {
      filterIncludeOn = !filterInfo[0].get(loc).equals(nullStr);
      filterIncludeP =  compileRegEx((String)filterInfo[0].get(loc));
      filterExcludeOn = !filterInfo[1].get(loc).equals(nullStr);
      filterExcludeP =  compileRegEx((String)filterInfo[1].get(loc));
      filterFolderOn =  !filterInfo[2].get(loc).equals(nullStr);
      filterFolderP =   compileRegEx((String)filterInfo[2].get(loc));
      filterSizeOn =    !filterInfo[3].get(loc).equals(nullStr);
      if (filterSizeOn)
         filterSize = Integer.parseInt((String)filterInfo[3].get(loc)) * kb;
      }

   public void zipFile(String filePath) {
      if ((zipCount/1000)*1000 == zipCount)
          rt.gc();
      try {
         file = new File(filePath);
         String displayPath = useRelativePaths ? filePath.substring(rootPathLen) : filePath;
         if (filterIncludeOn && !filterIncludeP.matcher(file.getName()).matches())
             Logger.logMsg(skippingLogMsg + displayPath);
         else if (filterExcludeOn && filterExcludeP.matcher(file.getName()).matches())
             Logger.logMsg(skippingLogMsg + displayPath + exclusionNote);
         else if (filterSizeOn && file.length() > filterSize)
             Logger.logMsg(skippingLogMsg + displayPath + exclusionNote + sizePre +
                nf.format(1.0 * file.length() / kb) + sizePost);
         else if (!SystemAttributes.isMac || !file.getName().equals(".DS_Store")) {
         	if (backupProgress != null)
               BackupProgressDialog.current.updateProgress(zipCount);
            Logger.logMsg(Str.macroExpand(zippingLogMsg, zipCount++) + displayPath);
            BufferedInputStream input =  //Create buffered input stream from file input stream
               new BufferedInputStream(new FileInputStream(file));
            zipEntry = new ZipEntry(displayPath);
            zipEntry.setTime(file.lastModified());
            zipOut.putNextEntry(zipEntry);  //Put empty zip entry into archive

            // Create a byte array object named data and declare byte count variable.
            while ((byteCount = input.read(data, 0, buffSize)) > -1)  //Until input entirely read
               zipOut.write(data, 0, byteCount);
            }
         }
      catch (IOException e) {
         abortBackupAsk(UIProperties.current.err03ZippingFile + dataPrompt + filePath +
            dividerStr + e.getLocalizedMessage());
         }
      }
   
   public void zipDirectory(String dirName, File dir) {
      String displayPath = useRelativePaths ? dirName.substring(rootPathLen) : dirName;
      if (filterFolderOn && filterFolderP.matcher(dir.getName()).matches())
         Logger.logMsg(skippingLogMsg + displayPath + fileSeparator + exclusionNote);
      else {
         Logger.logMsg(folderLogMsg + displayPath);
         File[] itemList = dir.listFiles();  //List folder's sub-items
         for (int count = 0; count < itemList.length; count ++)
            if (!abortBackup)
               zipItem(itemList[count].getPath());
         }   	
      }

   public void zipItem(String item) {
      File itemObj = new File(item);
      if (itemObj.exists()) {
         if (itemObj.isDirectory())
         	zipDirectory(item, itemObj);
         else
            zipFile(itemObj.getPath());
         }
      else
         abortBackupAsk(UIProperties.current.err02ItemNotFound, item);
      }

   public void calcRootPath(List zipItemList) {
      String root = nullStr;
      ListIterator iter = zipItemList.listIterator();
      if (iter.hasNext())
         root = (String)iter.next();
      while (iter.hasNext()) {
         String zipItem = (String)iter.next();
         if (zipItem.length() < root.length())
            root = root.substring(0, zipItem.length());
         for (int loc = 0; loc < root.length(); loc++)
            if (zipItem.charAt(loc) != root.charAt(loc))
               root = root.substring(0, loc);
         }
      rootPath = root.substring(0, root.lastIndexOf(fileSeparator) + 1);
      rootPathLen = rootPath.length();
      }

   public void zipItems(List zipItemList, String zipFileName,
         boolean filtersOn, List[] filterInfo) {
      abortBackup = false;
      initFilterInfo();
      nf.setMaximumFractionDigits(1);
      Logger.logMsg(UIProperties.current.logMsgCreating + zipFileName);
      if (useRelativePaths) {
         calcRootPath(zipItemList);
         Logger.logMsg(UIProperties.current.logMsgRootPath + space + rootPath);
         }
      zipCount = 1;
   	boolean invalidFolder =
   		!FileSys.ensureParentFolderExists(zipFileName, backupProgress,
            UIProperties.current.fileUtilTitleBackupFolder);
   	boolean writeDenided =
   		UserPreferences.readPref(Options.prefAskBackup).equals(Options.askYes) &&
         new File(zipFileName).exists() &&
         !FileSys.askOverwrite(zipFileName, backupProgress,
            UIProperties.current.fileUtilTitleBackupFile);
      if (invalidFolder)
         abortBackup(UIProperties.current.err01CreatingBackupFile, FileSys.getErrMsg());  //better mesg?: 'No folder for backup file.'
      else if (writeDenided)
         abortBackup(UIProperties.current.err01CreatingBackupFile, FileSys.getErrMsg());  //better mesg?: 'No folder for backup file.'
      else
         try {
            fileOut = new FileOutputStream(zipFileName);
            zipOut = new ZipOutputStream(fileOut);
            ListIterator iter = zipItemList.listIterator();
            while (iter.hasNext() && !abortBackup) {
               if (filtersOn)
                  updateFilterInfo(filterInfo, iter.nextIndex());
               zipItem((String)iter.next());
               }
            Locale locale = new Locale(UserPreferences.readLocalePref());
            NumberFormat nf = NumberFormat.getNumberInstance(locale);
            nf.setMaximumFractionDigits(0);  //whole numbers only
            Logger.logMsg(UIProperties.current.logMsgFilesZipped + space + nf.format(zipCount - 1));
            zipOut.flush();
            zipOut.close();
            fileOut.close();
            Logger.logMsg(UIProperties.current.logMsgBackupCreated + space +
               zipFileName + sizePre +
               nf.format(1.0 * new File(zipFileName).length() / kb) + sizePost);
            }
         catch (IOException e) {
            abortBackup(UIProperties.current.err01CreatingBackupFile,
               e.getLocalizedMessage());
            }
      }

   public boolean isAbortSet() {
      return abortBackup;
      }

   public void abortBackup() {
      abortBackup = true;
      }
   public void abortBackup(String errMsg) {
      abortBackup(errMsg, "");
      }
   public void abortBackup(String errMsg, String supplimentalInfo) {
      abortBackup = true;
      if (backupProgress != null)
         JOptionPane.showMessageDialog(backupProgress, errMsg + newLine + supplimentalInfo);
      Logger.logMsg(errMsg);
      if (supplimentalInfo.length() > 0)
         Logger.logMsg(tab + supplimentalInfo);
      }

   private BackupProgressDialog backupProgress = null;
   public void setBackupProgressDialog (BackupProgressDialog x) {
      backupProgress = x;
      }
   public BackupProgressDialog getBackupProgressDialog() {
      return backupProgress;
      }
   public void abortBackupAsk(String errMsg) {
      Logger.logMsg(errMsg);
      String[] options = { UIProperties.current.errPromptContinue,
              UIProperties.current.errPromptAbort };
      if (backupProgress != null)
         abortBackup = JOptionPane.showOptionDialog(backupProgress, errMsg,
            UIProperties.current.errPromptTitle,
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
            options, options[0]) == 1;
      }
   public void abortBackupAsk(String errMsg, String supplimentalInfo) {
      abortBackupAsk(errMsg + newLine + supplimentalInfo);
      if (supplimentalInfo.length() > 0)
         Logger.logMsg(tab + supplimentalInfo);
      }

   }
