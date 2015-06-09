# Snap Backup Translator Steps for Mac OS X #

Follow these steps on Mac OS X to use Subversion (SVN) to access the appropriate Snap Backup properties file and submit your changes.

## 1. One-Time Environment and SVN Setup ##
**A) Get Your Password**
> To obtain your password (needed for next step), visit:
> > http://code.google.com/hosting/settings

**B) Download Code**

> Run _Terminal_ (it's in `/Applications/Utilities`) and enter the following commands at the Unix shell prompt:
> | `$ mkdir ~/Projects` |
|:---------------------|
> | `$ cd ~/Projects`    |
> | `$ svn checkout https://snapbackup.googlecode.com/svn/trunk/ snapbackup --username <USERNAME HERE>` |
> In the above instructions, replace "`<USERNAME HERE>`" with your Google Code username.  If you get an "Error validating server certificate", you can enter "p" to permanently accept the certificate.

## 2. Core Translator Steps ##
**A) Build Current Project and Verify**
> Open _Finder_ --> Go to the folder `~/Projects/snapbackup/src/tools/` --> Double-click the file _**build.sh.command**_ --> Go to the folder `~/Projects/snapbackup/build/` --> Double-click the file _**snapbackup.jar**_ -->  Verify that the Snap Backup application launches and works properly.

**A) Build Current Project and Verify**
> In _Finder_, go to the folder `~/Projects/snapbackup/src/tools/` and double-click the _**build.sh.command**_ file.

> Now go to the folder `~/Projects/snapbackup/build/` and double-click the _**snapbackup.jar**_ file.  Verify that the Snap Backup application launches and works properly.

**B) Edit Properties File**
> Use your editor to make changes to the appropriate properties file located in the `~/Projects/snapbackup/src/resources/properties/` folder.  Be sure to save the file with Unicode UTF-8 (no BOM) encoding.

**C) Build the Project and Verify**
> Repeat step A above.

**D) Check Changes into SVN Repository (Server)**
> Run _Terminal_ and enter the following commands at the Unix shell prompt:
> | `$ cd ~/Projects/snapbackup` |
|:-----------------------------|
> | `$ svn update`               |
> | `$ svn status`               |
> | `$ svn commit --message "<COMMENT HERE>."` |
> | `$ svn status`               |
> In the above instructions, replace "`<COMMENT HERE>`" with a short description of the changes you made.

**E) Post a Message to the Mailing List**
> Let the other translators know about your updated properties file by sending a quick note to the [Snap Backup Translators Mailing List](http://groups.google.com/group/snapbackup-translate).



---

[Snap Backup Contributors](http://www.snapbackup.com/about/)