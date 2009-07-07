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
// Copyright (c) 2009 Center Key Software                                     //
// Snap Backup is a trademark of Dem Pilafian                                 //
// http://www.snapbackup.com                                                  //
//                                                                            //
// Zip Engine:                                                                //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.utilities;

import java.io.*;
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
import com.snapbackup.settings.AppProperties;
import com.snapbackup.settings.SystemAttributes;
import com.snapbackup.settings.UserPreferences;

public class ZipEngine {

   final static int kb =             1024;
   final static int buffSize =       kb * 256;  //optimal size not known
   final String nullStr =            SystemAttributes.nullStr;
   final String space =              SystemAttributes.space;
   final String tab =                SystemAttributes.tab;
   final String newLine =            SystemAttributes.newLine;
   final String fileSeparator =      SystemAttributes.fileSeparator;
   final String dividerStr =         SystemAttributes.dividerStr;
   final String dataPrompt =         SystemAttributes.dataPrompt;
   final String zippingLogMsg =      tab + tab + UIProperties.current.logMsgZipping;
   final String skippingLogMsg =     tab + tab + UIProperties.current.logMsgSkipping;
   final String folderLogMsg =       tab + UIProperties.current.logMsgFolder;
   final static String regExResChars = "-+?()[]{}|$^<=.";
   final static String regExEscCode =  "\\";
   final String exclusionNote =      " -- " + AppProperties.getProperty("FilterRuleExcludeTitle");
   final String sizePre =            space + space + "[";
   final String sizePost =           space + AppProperties.getProperty("FilterMarkerUnits") + "]";
   NumberFormat nf = NumberFormat.getNumberInstance(new Locale(UserPreferences.readLocalePref()));
   boolean useRelativePaths = true;
   String rootPath;
   int rootPathLen;
   File file;
   FileOutputStream fileOut = null;
   ZipOutputStream zipOut = null;
   ZipEntry zipEntry;
   boolean abortBackup;
   int zipCount;
   int byteCount;
   byte[] data = new byte[buffSize];  //Data for read/write operations

   java.lang.Runtime rt = Runtime.getRuntime();  //memory leak!!!
   //long freeMem = Long.MAX_VALUE;
   //long totalMem = 0;

   boolean filterIncludeOn, filterExcludeOn, filterFolderOn, filterSizeOn;
   Pattern filterIncludeP, filterExcludeP, filterFolderP;
   int filterSize;
   
   void initFilterInfo() {
      filterIncludeOn = false;
      filterExcludeOn = false;
      filterFolderOn =  false;
      filterSizeOn =    false;
      }

   /*
   String escapeChars(String s, char c) {
      String str = s;
      for (int loc = 0; loc < str.length(); loc++)
         if (str.charAt(loc) == c) {
            str = str.substring(0, loc) + regExEscape + str.substring(loc);
            loc = loc + 1;
            }
      return str;
      }
   */

   Pattern compileRegEx(String regEx) {
      String regExEsc = regEx;
      Pattern pattern = null;
      for (char regExReservedChar : regExResChars.toCharArray())
         regExEsc = regExEsc.replace(
            String.valueOf(regExReservedChar), regExEscCode + regExReservedChar);
      //   expression = escapeChars(expression, regExReservedChar);
      //for (int loc = 0; loc < regExReservedChars.length(); loc++)
      //   regEx = escapeChars(regEx, regExReservedChars.charAt(loc));
      try {
         pattern = Pattern.compile(regExEsc.replaceAll(", ", "|").replaceAll("\\*", ".*"));
         }
      catch (PatternSyntaxException e) {
         JOptionPane.showMessageDialog(null, UIProperties.current.err10CannotParseFilter +
               dataPrompt + regExEsc + dividerStr + e.getMessage(), regExEsc,
               JOptionPane.ERROR_MESSAGE);
         initFilterInfo();
         }
      return pattern;
      }
   
   void updateFilterInfo(List<List<String>> filterInfo, int loc) {
      filterIncludeOn = !filterInfo.get(0).get(loc).equals(nullStr);
      filterIncludeP =  compileRegEx(filterInfo.get(0).get(loc));
      filterExcludeOn = !filterInfo.get(1).get(loc).equals(nullStr);
      filterExcludeP =  compileRegEx(filterInfo.get(1).get(loc));
      filterFolderOn =  !filterInfo.get(2).get(loc).equals(nullStr);
      filterFolderP =   compileRegEx(filterInfo.get(2).get(loc));
      filterSizeOn =    !filterInfo.get(3).get(loc).equals(nullStr);
      if (filterSizeOn)
         filterSize = Integer.parseInt(filterInfo.get(3).get(loc)) * kb;
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
            dividerStr + e.getMessage());
         }
      }
   
   public void zipDirectory(String dirName, File dir) {
      String displayPath = useRelativePaths ? dirName.substring(rootPathLen) : dirName;
      if (filterFolderOn && filterFolderP.matcher(dir.getName()).matches())
         Logger.logMsg(skippingLogMsg + displayPath + fileSeparator + exclusionNote);
      else {
         Logger.logMsg(folderLogMsg + displayPath);
         File[] itemList = dir.listFiles();  //List folder's sub-items
         for (File item : itemList)
            if (!abortBackup)
               zipItem(item.getPath());
         //for (int count = 0; count < itemList.length; count ++)
         //   if (!abortBackup)
         //      zipItem(itemList[count].getPath());
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

   public void calcRootPath(List<String> zipItemList) {
      String root = nullStr;
      boolean firstItem = true;
      for (String zipItem : zipItemList) {
         if (firstItem) root = zipItem;
         firstItem = false;
         if (zipItem.length() < root.length())
            root = root.substring(0, zipItem.length());
         for (int loc = 0; loc < root.length(); loc++)
            if (zipItem.charAt(loc) != root.charAt(loc))
               root = root.substring(0, loc);
         }
      /*
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
      */
      rootPath = root.substring(0, root.lastIndexOf(fileSeparator) + 1);
      rootPathLen = rootPath.length();
      }

   public void zipItems(List<String> zipItemList, String zipFileName,
         boolean filtersOn, List<List<String>> filterInfo) {
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
            /*
            for (String zipItem : zipItemList)
               if (!abortBackup) {
                  if (filtersOn)
                     updateFilterInfo(filterInfo, iter.nextIndex());  // nextIndex?
                  zipItem(zipItem);
                  }
            */
            ListIterator iter = zipItemList.listIterator();
            while (iter.hasNext() && !abortBackup) {
               if (filtersOn)
                  updateFilterInfo(filterInfo, iter.nextIndex());
               zipItem((String)iter.next());
               }
            Locale locale = new Locale(UserPreferences.readLocalePref());
            NumberFormat zipNF = NumberFormat.getNumberInstance(locale);
            zipNF.setMaximumFractionDigits(0);  //whole numbers only
            Logger.logMsg(UIProperties.current.logMsgFilesZipped + space + zipNF.format(zipCount - 1));
            zipOut.flush();
            zipOut.close();
            fileOut.close();
            Logger.logMsg(UIProperties.current.logMsgBackupCreated + space +
               zipFileName + sizePre +
               zipNF.format(1.0 * new File(zipFileName).length() / kb) + sizePost);
            }
         catch (IOException e) {
            abortBackup(UIProperties.current.err01CreatingBackupFile,
               e.getMessage());
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

   BackupProgressDialog backupProgress = null;
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
