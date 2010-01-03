#!/bin/sh
#################################
##  Snap Backup                ##
##  Diff for Properties Files  ##
#################################

cd `dirname $0`
echo
echo "Diff for Properties Files"
echo "========================="

# Line Count
echo "Line Count:"
for file in SnapBackup*.properties; do
   wc -l $file
   done

# Compare Names of Properties
grep = < SnapBackup.properties | sed s/\=.*// > base-properies.txt
for file in SnapBackup_*.properties; do
   echo "\n*** $file ***"
   grep = < $file | sed s/\=.*//	 | diff - base-properies.txt
   done
rm base-properies.txt
