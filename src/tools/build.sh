#!/bin/sh
###########################
##  Snap Backup          ##
##  Build on Unix/Linux  ##
###########################

cd `dirname $0`
ant build
echo "Hit the RETURN key..."
read anykey

