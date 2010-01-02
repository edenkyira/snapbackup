////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// CheckForUpdates.java                                                       //
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
// Copyright (c) 2010 Center Key Software and Individual Contributors         //
// Snap Backup is a registered trademark of Center Key Software               //
// http://www.snapbackup.com                                                  //
//                                                                            //
// Data Model:                                                                //
//    This object is the business logic of MVC.                               //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.snapbackup.logger.Logger;
import com.snapbackup.settings.SystemAttributes;

public class CheckForUpdates {

   public static String getLatestVersion() {
      String version = null;
      try {
         BufferedReader reader = new BufferedReader(
            new InputStreamReader(new URL(SystemAttributes.updatesURL).openStream()));
         version = reader.readLine();
         reader.close();
         }
      catch (Exception e) { //NoRouteToHostException or ConnectException
         Logger.logMsg(e.toString());
         }
      return version;
      }

   }
