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
// Copyright (c) 2007 Center Key Software                                     //
// Snap Backup is a trademark of Dem Pilafian                                 //
// http://www.snapbackup.com                                                  //
//                                                                            //
// System Attributes:                                                         //
//    This object...                                                          //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.utilities.settings;

import java.util.Locale;
import java.util.Properties;

//Constant values that do not belong in the properties file.
public class SystemAttributes {

   //Bootstrap data needed before reading properties file
   public static final String   appName = "SnapBackup";  //".properties" name
   public static final String   appVersion = "5.0";
   // 5.0 - 4/21/2009 -  Switch to Java 5.0 and new SwingManager
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
   public static final String   appAuthors = "Dem Pilafian";
   public static final String   errMsgHeader = "ERROR -- '";
   public static final String   errMsgMissingResource = "' property not found in: ";
   public static final String[] localeCodes = {
      "en", "eo", "es", "de", "fr", "it", "ko", "nl", "pt", "ru", "sl" };
   public static final String   prefLocale = "locale";
   public static final String   prefChar = ".";
   public static final String   prefPrefix = appName.toLowerCase() + prefChar;
   public static final String   prefProfilePrefix = prefPrefix + "~";
   public static final String   prefProfilePostfix = "~" + prefChar;
   public static final String   trueStr =  "true";  //Used to save pref data as text
   public static final String   falseStr = "false";
   public static final String   splitStr = "~@~";  //Delimitter for multi-line data
   public static final String   cmdLineDefaultProfile = "~";
   public static final String[] appTranslators = {
      //Codes -- http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt
      //Flags -- http://www.paypal.com/be/cgi-bin/webscr?cmd=_display-approved-signup-countries-outside
      //Unicode -- http://www.ling.upenn.edu/~kurisuto/germanic/aa_character_encoding.html
      "Giorgio Ponza",          "http://www.janxes.it/jsp/societa/Giorgio.jsp",  //"it"
      "Pasc\u00e1l Bihler",     "http://www.bi-on.de/pascal/",                   //"de"
      "Carlos Maltzahn",        "http://homepage.mac.com/carlosmalt/",           //"de"
      //"Angelo Brillout",      null,                                            //"fr" [with Pascal]
      "Suzanne Bolduc",         "http://www.esperanto.net/",                     //"eo"
      "Elena Kogan",            "http://speakrussian.report.ru/",                //"ru"
      "Oscar Laurens Schrover", "http://www.macfan.nl/",                         //"nl"
      "Angel Herr\u00e1ez",     "http://www2.uah.es/biomodel/",                  //"es"
      "Antonio de Rezende Jr.", "http://en.wikipedia.org/wiki/Portuguese_language", //"pt"
      "Steve",                  "http://www.transkoreanservices.com/",           //"ko"
      "Sa\u0161o Topolovec",    "http://en.wikipedia.org/wiki/Slovene_language"  //"sl"
      };

   //Useful constants
   public static final String nullStr =         "";
   public static final String space =           " ";
   public static final String comma =           ",";
   public static final String dataPrompt =      ": ";
   public static final String tab =             "     ";
   public static final String dividerStr =      " / ";
   public static final String newLine =         "\n";
   public static final String headerPre =       "<html><center><br><b><font size=+2 color=navy face='comic sans ms, sand, fantasy'>";
   public static final String headerMid =       "</font></b>";
   public static final String headerSplashTag = "<br><br><b><font color=gray>Center Key Software</font></b>";
   public static final String headerPost =      "<br>&nbsp;</center></html>";

   //System information retrieved from JVM
   private static final Properties sysInfo = System.getProperties();
   public static final String userName = sysInfo.getProperty("user.name");
   public static final String userHomeDir = sysInfo.getProperty("user.home");
   public static final String appWorkingDir = sysInfo.getProperty("user.dir");
   public static final String fileSeparator = sysInfo.getProperty("file.separator");
   public static final boolean isMac = sysInfo.getProperty("mrj.version") != null;
   public static final boolean evilWinSys = fileSeparator.equals("\\");
   public static final String evilWinDrive = (userHomeDir.indexOf(":\\") == 1 ?
      userHomeDir.substring(0, 2) : "");   //example: "C:\zzz" --> "C:"
   public static final String osInfo =   //example: "Windows Me\4.90 (x86)\en"
      sysInfo.getProperty("os.name") + " (" +
      sysInfo.getProperty("os.version") + fileSeparator +
      sysInfo.getProperty("os.arch") + fileSeparator +
      Locale.getDefault().getLanguage() + ")";
   public static final String javaVersion = "Java " +
      //example: "Java 1.4.2_05, Sun Microsystems Inc."
      sysInfo.getProperty("java.version") + ", " +
      sysInfo.getProperty("java.vendor");
   public static final String javaHomeDir = sysInfo.getProperty("java.home");
   public static final String javaVMInfo =
      //example: "Java HotSpot(TM) Client VM, 1.4.2_05-b04 [63MB]"
      sysInfo.getProperty("java.vm.name") + ", " +
      sysInfo.getProperty("java.vm.version") + " [" +
      Runtime.getRuntime().maxMemory()/(1024*1024) + " MB]";

   //Profile key names
   //refactor to move profile key names from "DataModel" to here???
   
   }
