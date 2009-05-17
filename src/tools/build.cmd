@echo off
::::::::::::::::::::::::::::::::
:: Snap Backup                ::
:: Build on Microsoft Windows ::
::::::::::::::::::::::::::::::::

:: Set JAVA_HOME
for /d %%i in ("\Program Files\Java\jdk*") do set JAVA_HOME=%%i

:: set ANT_HOME
for /d %%i in ("\Apps\Ant\apache-ant*") do set ANT_HOME=%%i

:: Display Variables and Launch Ant
set JAVA_HOME
set ANT_HOME
call %ANT_HOME%\bin\ant build
pause
