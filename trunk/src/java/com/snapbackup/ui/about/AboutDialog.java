////////////////////////////////////////////////////////////////////////////////
// Snap Backup(tm)                                                            //
// AboutDialog.java                                                           //
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
// About Dialog:                                                              //
//    This object....                                                         //
////////////////////////////////////////////////////////////////////////////////

package com.snapbackup.ui.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import com.snapbackup.ui.Icons;
import com.snapbackup.ui.UIProperties;
import com.snapbackup.ui.UIUtilities;
import com.snapbackup.utilities.BareBonesBrowserLaunch;
import com.snapbackup.utilities.settings.SystemAttributes;

public class AboutDialog extends JDialog {

   private static final String space =         SystemAttributes.space;
   private static final String nullStr =       SystemAttributes.nullStr;
   private static final String tab =           SystemAttributes.tab;
   private static final String comma =         SystemAttributes.comma;
   private static final String newLine =       SystemAttributes.newLine;
   private static final String homeURL =       "http://www.snapbackup.com";
   private static final String launchURL =     homeURL + "/app?v=" + SystemAttributes.appVersion;
   private static final String downloadURL =   homeURL + "/download";
   private static final String urlKey =        "url";  //used as key to save URL for each translator
   private static final String hey =           "Hey, don't press the '[' key!";
   private static final String[] translators = SystemAttributes.appTranslators;

   private AboutUIProperties ui = new AboutUIProperties();   //make singleton for performance????
   private final String contactInfoStr = ui.aboutContact + newLine + SystemAttributes.postalAddress + newLine + homeURL + newLine + SystemAttributes.feedbackEMail;

   //Define About Controls
   private JPanel    aboutPanel =   new JPanel();
   private JLabel    logo =         new JLabel(Icons.logoIcon);
   private JLabel    productName =  new JLabel(UIProperties.current.header);
   private JLabel    version =      new JLabel(ui.aboutVersion);
   private JLabel    author =       new JLabel(ui.aboutCreatedBy);
   private JPanel    translatorsPanel =  new JPanel();
   private JLabel    translatedBy = new JLabel(ui.aboutTranslatedBy + space);
   private JLabel    copyright =    new JLabel("<html>" + ui.aboutCopyright + space + SystemAttributes.appCopyright + "</html>");
   private JTextArea license =      new JTextArea(ui.aboutLicense + newLine + newLine + ui.aboutDownload + newLine + tab + downloadURL);
   private JTextArea configInfo =   new JTextArea();
   private JTextArea contactInfo =  new JTextArea(contactInfoStr.replaceAll(newLine, newLine + tab));
   private JButton   webButton =    new JButton(ui.aboutButtonWeb);
   private JButton   closeButton =  new JButton(ui.aboutButtonClose);
   private JButton[] buttonList =   { webButton, closeButton };

   public AboutDialog() {
      initGUI();
      setModal(true);
      setResizable(false);
      pack();
      }

   private void initGUI() {
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      setTitle(ui.aboutTitle + UIProperties.current.appTitle);
      configureContols();
      addContols();
      getContentPane().setBackground(Color.white);
      getContentPane().add(logo, BorderLayout.LINE_START);
      getContentPane().add(aboutPanel, BorderLayout.CENTER);
      }

   private void setupTranslatorsPanel() {
      translatorsPanel.setLayout(new BoxLayout(translatorsPanel, BoxLayout.PAGE_AXIS));
      JPanel row = null;
      for (int count = 0; count*2 < translators.length; count++) {
         if (count % 3 == 0) {  //three names per line
            row = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setBackground(Color.white);
            row.add(count == 0 ? translatedBy : new JLabel(tab));
            translatorsPanel.add(row);
            }
         JLabel translatorLabel = new JLabel(translators[count*2]);
         translatorLabel.putClientProperty(urlKey, translators[count*2+1]);
         if (translators[count*2+1] != null)
            translatorLabel.addMouseListener(new MouseListener() {
               public void mouseClicked(MouseEvent e) { BareBonesBrowserLaunch.openURL((String)((JLabel)e.getSource()).getClientProperty(urlKey)); }
               public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
               public void mouseExited(MouseEvent e)  { setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); }
               public void mousePressed(MouseEvent e) { }
               public void mouseReleased(MouseEvent e) { }
               });
         row.add(translatorLabel);
         String join = comma + space;
         if (count*2+4 == translators.length) join = join + ui.aboutAnd + space;
         if (count*2+2 == translators.length) join = nullStr;
         row.add(new JLabel(join));
         }       
      }

   private void configureContols() {
      logo.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
      aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.PAGE_AXIS));
      aboutPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 30));
      aboutPanel.setBackground(Color.white);
      setupTranslatorsPanel();
      license.setEditable(false);
      license.setFont(UIProperties.standardFont);
      license.setBackground(Color.white);  //needed for Motif
      configInfo.setEditable(false);
      configInfo.setBackground(Color.white);  //needed for Motif
      configInfo.setFont(UIProperties.standardFont);
      configInfo.setText(
         UIProperties.current.appTitle + space + SystemAttributes.appVersion + newLine +
         SystemAttributes.osInfo + newLine +
         SystemAttributes.javaVersion + newLine +
         SystemAttributes.javaHomeDir + newLine +
         SystemAttributes.javaVMInfo);
      configInfo.setBorder(BorderFactory.createTitledBorder(ui.aboutConfigurationTitle));
      contactInfo.setEditable(false);
      contactInfo.setFont(UIProperties.standardFont);
      contactInfo.setBackground(Color.white);  //needed for Motif
      webButton.setBackground(Color.white);
      webButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            BareBonesBrowserLaunch.openURL(launchURL); }
         } );
      closeButton.setBackground(Color.white);
      closeButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             closeButtonAction(); }
          } );
      UIUtilities.addFastKeys(buttonList);
      UIUtilities.makeBold(closeButton);
      getRootPane().setDefaultButton(closeButton);
      getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               closeButtonAction(); } },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);
      getRootPane().registerKeyboardAction(actionListener,  //Easter egg for '['
            KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);  //registerKeyboardAction is now obsolete
      }

   public void addContols() {
      aboutPanel.add(productName);
      aboutPanel.add(version);
      aboutPanel.add(author);
      aboutPanel.add(translatorsPanel);
      aboutPanel.add(Box.createRigidArea(new Dimension(0,10)));
      aboutPanel.add(license);
      aboutPanel.add(Box.createRigidArea(new Dimension(0,10)));
      aboutPanel.add(configInfo);
      aboutPanel.add(Box.createRigidArea(new Dimension(0,10)));
      aboutPanel.add(contactInfo);
      aboutPanel.add(Box.createRigidArea(new Dimension(0,10)));
      aboutPanel.add(copyright);
      aboutPanel.add(Box.createRigidArea(new Dimension(0,10)));
      aboutPanel.add(webButton);
      aboutPanel.add(Box.createRigidArea(new Dimension(0,10)));
      aboutPanel.add(closeButton);
      for (int count = 0; count < aboutPanel.getComponentCount(); count++)
         ((JComponent)aboutPanel.getComponent(count)).setAlignmentX(Component.LEFT_ALIGNMENT);
      }

   //
   // Callback Methods (Event Actions)
   //
   public void closeButtonAction() {
      this.dispose();
      }
   ActionListener actionListener = new ActionListener() {
       public void actionPerformed(ActionEvent actionEvent) {
           String msg = hey;
           JOptionPane.showMessageDialog(null, msg);
           try {
              URLConnection connection = new URL("http://www.snapbackup.com/version/").openConnection();
              //connection.setReadTimeout(10000);
              BufferedReader reader = new BufferedReader(new InputStreamReader((
                 InputStream) connection.getContent()));
              String line = reader.readLine();
              JOptionPane.showMessageDialog(null, line);
              }
           catch (Exception e) {
              //java.net.NoRouteToHostException or java.net.ConnectException
              JOptionPane.showMessageDialog(null, "Update check error: " + e.getLocalizedMessage() + " > " + e.getClass().getName());
              }
           }
       };

   }
