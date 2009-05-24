////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// FileSys.java                                                               //
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
// File System:                                                               //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.utilities;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.snapbackup.logger.Logger;
import com.snapbackup.ui.UIProperties;
import com.snapbackup.settings.SystemAttributes;
import com.snapbackup.utilities.Str;
import com.snapbackup.utilities.ZipEngine;

public class FileSys {

   static String errMsg;
   static String nullStr = SystemAttributes.nullStr;
   static String space =   SystemAttributes.space;

   public static String getErrMsg() {
      return errMsg;
      }

   public static boolean ensureParentFolderExists(String fileName,
         Component frame, String winTitle, String msg, String createButton,
         String abortButton) {
      boolean folderExists = false;
      errMsg = nullStr;
      String[] options = { createButton, abortButton };
      try {
         File f = new File(fileName).getParentFile();
         folderExists = f.isDirectory();
         if (!folderExists && (frame == null ||
            JOptionPane.showOptionDialog(frame,
               Str.macroExpand(msg, f.toString()), winTitle,
               JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
               options, options[0]) == 0))
            folderExists = f.mkdirs();
         }
      catch (Exception e) {
         errMsg = e.getLocalizedMessage();
         }
      return folderExists;
      }

   public static boolean ensureParentFolderExists(String fileName, Component frame,
         String winTitle) {
      return ensureParentFolderExists(fileName, frame, winTitle,
         UIProperties.current.fileUtilCreateFolderMsg,
         UIProperties.current.fileUtilCreateFolderButton,
         UIProperties.current.fileUtilButtonAbort);
      }

   static boolean askOverwrite(String fileName, Component frame,
         String winTitle, String msg, String overwriteButton,
         String abortButton) {
      String[] options = { overwriteButton, abortButton };
      return JOptionPane.showOptionDialog(frame,
         Str.macroExpand(msg, fileName), winTitle,
         JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
         options, options[0]) == 0;
      }
   public static boolean askOverwrite(String fileName, Component frame,
         String winTitle) {
      return askOverwrite(fileName, frame, winTitle,
         UIProperties.current.fileUtilOverwriteMsg,
         UIProperties.current.fileUtilOverwriteButton,
         UIProperties.current.fileUtilButtonAbort);
      }

   public static void copyFile(String sourceFileName, String destFileName, Component frame, ZipEngine zip, boolean autoOverwrite) {
      File sourceFile = new File(sourceFileName);
      File destFile = new File(destFileName);
      FileInputStream source = null;
      FileOutputStream dest = null;
      byte[] buffer;
      int bytesRead;
      //String[] options = { 'Overwrite', 'Abort' };
      try {
         if (!sourceFile.exists() || !sourceFile.isFile() || !sourceFile.canRead())
            zip.abortBackup(UIProperties.current.err04BackupFileMissing, sourceFileName);
         else if (!ensureParentFolderExists(destFileName, frame, UIProperties.current.fileUtilTitleArchiveFolder)) {
            if (getErrMsg().length() == 0)
               zip.abortBackup(UIProperties.current.err05NoArchiveFolder);
            else
               zip.abortBackup(UIProperties.current.err05NoArchiveFolder, destFileName);
            }
         else if (false && !destFile.canWrite())  //seems to malfunction
            zip.abortBackup(UIProperties.current.err06CannotWriteArchive, destFileName);
         else if (false && !destFile.isFile())  //seems to malfunction
            zip.abortBackup(UIProperties.current.err07ArchiveNotFile, destFileName);
         else if (!destFile.exists() || autoOverwrite || askOverwrite(destFileName,
            zip.getBackupProgressDialog(), UIProperties.current.fileUtilTitleArchiveFile)) {
            source = new FileInputStream(sourceFile);
            dest = new FileOutputStream(destFile);
            buffer = new byte[1024];
            while(true) {
               bytesRead = source.read(buffer);
               if (bytesRead == -1) break;
                  dest.write(buffer, 0, bytesRead);
               }
            Logger.logMsg(UIProperties.current.logMsgArchivedBackup + space + destFileName);
            }
         else
            zip.abortBackup(UIProperties.current.err08ArchiveCanceled, destFileName);
         }
      catch (IOException e) {
         zip.abortBackup(UIProperties.current.err09ArchiveProblem, e.getLocalizedMessage());
         }
      finally {
         if (source != null)
            try { source.close(); }
            catch (IOException e) { Logger.logMsg(e.getLocalizedMessage()); }
            if (dest != null)
               try { dest.close(); }
               catch (IOException e) { Logger.logMsg(e.getLocalizedMessage()); }
         }
      }
   
   public static void copyFile(String sourceFileName, String destFileName, ZipEngine zip) {
      copyFile(sourceFileName, destFileName, null, zip, true);
      }

   }
