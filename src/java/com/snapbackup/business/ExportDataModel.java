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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.snapbackup.utilities.settings.UserPreferences;

public class ExportDataModel {




   void writeXmlFile(Document doc, String filename) {
      //Output DOM to an XML file
      try {
         Source source = new DOMSource(doc);
         Result result = new StreamResult(new File(filename));
         Transformer xformer = TransformerFactory.newInstance().newTransformer();
         xformer.setOutputProperty(OutputKeys.INDENT, "yes");  //Human readable
         xformer.transform(source, result);
         }
      catch (Exception e) {
         System.out.println(e.getLocalizedMessage());
         }
      }

   Document createDOM (String topLevelNodeName) {
      Document xmlDoc = null;
      try {
         DocumentBuilder xmlBuilder =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
         xmlDoc = xmlBuilder.newDocument();
         xmlDoc.appendChild(xmlDoc.createElement(topLevelNodeName));
         }
      catch (Exception e) {
         System.out.println("Error: " + e.getLocalizedMessage());
         }
      return xmlDoc;
      }

   public boolean doExport(String fileName) {

         // Create DOM with Top Level Node
         Document xmlDoc = null;
         DocumentBuilder xmlBuilder = null;
         try {
         xmlBuilder =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
         }
      catch (Exception e) {
         System.out.println("Error: " + e.getLocalizedMessage());
         }
         xmlDoc = xmlBuilder.newDocument();
         Element topNode = xmlDoc.createElement("SnapBackupSettings");  ///!!!!!!
         xmlDoc.appendChild(topNode);

         //Add Sample Data to DOM
      String[] data = UserPreferences.getAllKeys();
      Element settingsNode = xmlDoc.createElement("UserSettings");  ///!!!!!!
      topNode.appendChild(settingsNode);
      Element setting;
      for (String key : data) {
            setting = xmlDoc.createElement("Setting");
            setting.setAttribute("Type", key);
            setting.appendChild(xmlDoc.createTextNode(UserPreferences.readPrefByKey(key)));
            settingsNode.appendChild(setting);
            }

         //Action
         writeXmlFile(xmlDoc, fileName);
      return true;
      }

   }
