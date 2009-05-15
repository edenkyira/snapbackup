@echo off
::::::::::::::::::::::::::::::::
:: Snap Backup                ::
:: Build on Microsoft Windows ::
::::::::::::::::::::::::::::::::

:: Set JAVA_HOME to current JDK (note: delim ends in a tab)
set KeyName=HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Development Kit
set Cmd=reg query "%KeyName%" /v CurrentVersion
for /f "tokens=2 delims=REG_SZ	" %%T in ('%Cmd%') do set VersionJDK=%%T
set Cmd=reg query "%KeyName%\%VersionJDK%" /v JavaHome
for /f "tokens=2,3 delims=REG_SZ	" %%i in ('%Cmd%') do set JAVA_HOME=%%i_%%j

set ANT_HOME=C:\Apps\Ant\apache-ant-1.7.1
set JAVA_HOME
set ANT_HOME
call %ANT_HOME%\bin\ant build
pause
