# Snap Backup Contributor Steps for Mac OS X #

Follow these steps on Mac OS X to use Subversion (SVN) to access the Snap Backup source code and submit your changes.

## 1. One-Time Environment and SVN Setup ##
**A) Get Your SVN Credentials**
> To obtain your Google Code username and password, visit:
> > http://code.google.com/hosting/settings

**B) Verify Java and Download Code**

> Run _Terminal_ (it's in `/Applications/Utilities`) and enter the following commands at the Unix shell prompt:
> | `$ java -version` |
|:------------------|
> | `$ mkdir ~/projects` |
> | `$ cd ~/projects` |
> | `$ svn checkout https://snapbackup.googlecode.com/svn/trunk/ snapbackup --username <USERNAME>` |
> In the above instructions, replace "`<USERNAME>`" with your Google Code username.  If you get an "Error validating server certificate", you can enter "p" to permanently accept the certificate.

**C) Get Xcode (optional)**
> If you plan to create the installer (see step 3B below), you need to install _**Xcode**_ from the App Store.

## 2. Core Contributor Steps ##
**A) Build Current Project and Verify**
> Open _Finder_ --> Go to the folder `~/projects/snapbackup/src/tools` --> Double-click the file _**build.sh.command**_ --> Go to the folder `~/projects/snapbackup/build` --> Double-click the file _**snapbackup.jar**_ -->  Verify that the Snap Backup application launches and works properly

**B) Edit Code**
> The project files are under the `~/projects/snapbackup` folder.  Use your editor or IDE to make changes to the appropriate files.

**C) Build the Project and Verify**
> Repeat step A above.

**D) Check Changes into SVN Repository (Server)**
> | `$ cd ~/projects/snapbackup` |
|:-----------------------------|
> | `$ svn update`               |
> | `$ svn status`               |
> | `$ svn commit --message "<COMMENT>"` |
> | `$ svn status`               |
> In the above instructions, replace "`<COMMENT>`" with a short description of the changes you made.

## 3. Mac OS X Installer ##
To wrap the Mac application (`~/projects/snapbackup/build/Snap Backup.app`) into a native [Mac OS X installer](http://www.centerkey.com/mac/java/), follow these steps:
  1. Using _Finder_, navigate to the `~/projects/snapbackup/src/installer/mac` folder
  1. Double-click _**snap-backup.pmdoc**_  (the first time you may have to open from PackageMaker)
  1. Click the _**Build**_ hammer icon
  1. Navigate to the `~/projects/snapbackup/build` folder
  1. Deselect the "Hide extension" option
  1. Click the _**Save**_ button
  1. Quit _PackageMaker_ and choose _**Don't Save**_ your changes
The new installer will be at: `~/projects/snapbackup/build/SnapBackupInstaller.mpkg`



---

[Snap Backup Contributors](http://www.snapbackup.com/about/)