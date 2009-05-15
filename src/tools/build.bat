@echo off

rem ########################################################
rem ##  Snap Backup                                       ##
rem ##  Build on Microsoft Windows                        ##
rem ########################################################

set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_13
set ANT_HOME=C:\Apps\Ant\apache-ant-1.7.1
set JAVA_HOME
set ANT_HOME
call %ANT_HOME%\bin\ant build
pause
