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
// Copyright (c) 2009 Center Key Software                                     //
// Snap Backup is a trademark of Dem Pilafian                                 //
// http://www.snapbackup.com                                                  //
//                                                                            //
// Data Model:                                                                //
//    This object is the business logic of MVC.                               //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.business;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.snapbackup.settings.SystemAttributes;

public class CheckForUpdates {

   public static String getMessage() {
      String msg;
      try {
         URLConnection connection =
            new URL(SystemAttributes.updatesURL).openConnection();
         //connection.setReadTimeout(10000);
         BufferedReader reader = new BufferedReader(new InputStreamReader((
            InputStream) connection.getContent()));
         String line = reader.readLine();
         msg = "The current version is: " + line;
         }
      catch (Exception e) { //NoRouteToHostException or ConnectException
         msg = e.toString();
         }
      return msg;
      }

   }
