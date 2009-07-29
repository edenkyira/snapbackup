////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// SystemAttributes.java                                                      //
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
// System Attributes:                                                         //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.settings;

import java.util.Locale;
import java.util.Properties;
import javax.swing.filechooser.FileSystemView;

//Constant values that do not belong in the properties file.
public class SystemAttributes {

   //Release constants
   public static final String   appName = "SnapBackup";  //".properties" name
   public static final String   appCopyright = "&copy; 2009 Center Key Software";
   public static final String   appVersion = "5.1";
   // 5.1 - 6/2/2009 -   Added Check for Updates feature (and moved prefs)
   // 5.0 - 5/6/2009 -   Added import/export and switched to Java 5.0
   // 4.5 - 9/7/2008 -   Slovene (Slovenian)
   // 4.4 - 6/9/2006 -   Korean, user guide print button, and exclude .DS_Store on Macs
   // 4.3 - 5/7/2006 -   Portuguese
   // 4.2 - 4/22/2006 -  Spanish
   // 4.1 - 1/18/2006 -  Dutch
   // 4.0 - 12/18/2005 - Configurable source and log height (options)
   // 3.8 - 12/1/2005 -  Skins (save user selection in preferences)
   // 3.7 - 10/23/2005 - Russian
   // 3.6 - 9/22/2005 -  Esperanto and command-line
   // 3.5 - 9/??/2005 -  Options feature
   // 3.4 - 9/2/2005 -   Folder support for filters
   // 3.3 - 7/5/2005 -   German
   // 3.2 - 6/17/2005 -  Italian
   // 3.1 - 6/1/2005 -   Filters
   // 3.0 - 5/9/2005 -   Multiple profiles
   public static final String     appAuthors = "Dem Pilafian";  //Names of code contributor go here
   public static final String[][] appTranslators = {
      //Codes -- http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt
      //Flags -- http://www.paypal.com/be/cgi-bin/webscr?cmd=_display-approved-signup-countries-outside
      //Unicode -- http://www.ling.upenn.edu/~kurisuto/germanic/aa_character_encoding.html
      { "Giorgio Ponza",          "http://www.janxes.it/jsp/societa/Giorgio.jsp" },  //"it"
      { "Pasc&aacute;l Bihler",   "http://www.bi-on.de/pascal/" },                   //"de"
      { "Carlos Maltzahn",        "http://homepage.mac.com/carlosmalt/" },           //"de"
      //{ "Angelo Brillout",      null },                                            //"fr" [with Pascal]
      { "Suzanne Bolduc",         "http://www.esperanto.net/" },                     //"eo"
      { "Elena Kogan",            "http://speakrussian.report.ru/" },                //"ru"
      { "Oscar Laurens Schrover", "http://www.macfan.nl/" },                         //"nl"
      { "Angel Herr&aacute;ez",   "http://www2.uah.es/biomodel/" },                  //"es"
      { "Antonio de Rezende Jr.", "http://en.wikipedia.org/wiki/Portuguese_language" },  //"pt"
      { "Steve",                  "http://www.transkoreanservices.com/" },           //"ko"
      { "Sa&#353;o Topolovec",    "http://en.wikipedia.org/wiki/Slovene_language" }  //"sl" [&scaron;]
      };
   public static final String[] localeCodes = {
      "en", "eo", "es", "de", "fr", "it", "ko", "nl", "pt", "ru", "sl" };

   //Useful constants
   public static final String nullStr =         "";
   public static final String space =           " ";
   public static final String comma =           ",";
   public static final String atSign =           "@";
   public static final String dataPrompt =      ": ";
   public static final String tab =             "     ";
   public static final String dividerStr =      " / ";
   public static final String newLine =         "\n";
   public static final String startHtml =       "<html>";
   public static final String endHtml =         "</html>";
   public static final String headerPre =       startHtml + "<center><br><b><font size=+2 color=navy face='comic sans ms, sand, fantasy'>";
   public static final String headerMid =       "</font></b>";
   public static final String headerSplashTag = "<br><br><b><font color=gray>Center Key Software</font></b>";
   public static final String headerPost =      "<br>&nbsp;</center>" + endHtml;
   public static final String feedbackEMail =   "feedback" + atSign + "snapbackup.com";
   public static final String postalAddress =   "Center Key Software\nBox 21\nSan Carlos, California  94070\nUSA";
   public static final String homeURL =         "http://www.snapbackup.com";  //on About Box
   public static final String updatesURL =      homeURL + "/version/";  //not displayed
   public static final String downloadURL =     homeURL + "/download";  //on About Box
   public static final String visitURL =        homeURL + "/app?v=" + appVersion;  //not displayed

   //Bootstrap data needed before reading properties file
   public static final String   errMsgHeader = "ERROR -- '";
   public static final String   errMsgMissingResource = "' property not found in: ";
   public static final String   prefLocale = "locale";
   public static final String   prefChar = ".";
   public static final String   prefPrefix = appName.toLowerCase() + prefChar;
   public static final String   prefProfilePrefix = prefPrefix + "~";
   public static final String   prefProfilePostfix = "~" + prefChar;
   //public static final String   trueStr =  "true";  //Used to save pref data as text
   //public static final String   falseStr = "false";
   public static final String   splitStr = "~@~";  //Delimitter for multi-line data
   public static final String   cmdLineDefaultProfile = "~";

   //System information retrieved from JVM
   static final Properties sysInfo = System.getProperties();
   public static final String userName = sysInfo.getProperty("user.name");
   public static final String userHomeDir = sysInfo.getProperty("user.home");
   public static final String fileSeparator = sysInfo.getProperty("file.separator");
   public static final boolean isMac = sysInfo.getProperty("mrj.version") != null;
   public static final boolean evilWinSys = fileSeparator.equals("\\");
   public static final String evilWinDrive = (userHomeDir.indexOf(":\\") == 1 ?
      userHomeDir.substring(0, 2) : "");  //example: "C:\zzz" --> "C:"
   public static final String osInfo =  //example: "Mac OS X (10.5.7/x86_64/en)"
      sysInfo.getProperty("os.name") + " (" +
      sysInfo.getProperty("os.version") + fileSeparator +
      sysInfo.getProperty("os.arch") + fileSeparator +
      Locale.getDefault().getLanguage() + ")";
   public static final String javaVersion =  //example: "Java 1.6.0_13, Apple Inc."
      "Java " + sysInfo.getProperty("java.version") + ", " +
      sysInfo.getProperty("java.vendor");
   public static final String javaHomeDir = sysInfo.getProperty("java.home");
   public static final String javaVMInfo =
      //example: "Java HotSpot(TM) 64-Bit Server VM, 11.3-b02-83 [81 MB]"
      sysInfo.getProperty("java.vm.name") + ", " +
      sysInfo.getProperty("java.vm.version") + " [" +
      Runtime.getRuntime().maxMemory()/(1024*1024) + " MB]";
   static final String docsExtra =  //Mac OS X localization changes display name
      isMac ? fileSeparator + "Documents" : "";
   static final String desktopExtra =  //Mac OS X localization changes display name
      isMac ? fileSeparator + "Desktop" : "";
   static final FileSystemView fileSysView = FileSystemView.getFileSystemView();
   public static final String userDocsDir =
      fileSysView.getDefaultDirectory().getAbsolutePath() + docsExtra;
   public static final String userDesktopDir =
      fileSysView.getHomeDirectory().getAbsolutePath() + desktopExtra;
   }
