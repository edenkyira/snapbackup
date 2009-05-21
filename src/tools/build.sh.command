#!/bin/sh
########################################################
##  Snap Backup                                       ##
##  Build on Mac OS X                                 ##
##  Setup: Use <Command-I> in Finder to open the      ##
##     "Info" window for this file and set the "Open  ##
##     with:" field to "Terinal".  Now, just          ##
##     double-click this file to build Snap Backup.   ##
########################################################

cd `dirname $0`
ant build
