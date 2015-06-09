# Snap Backup Contributor Steps for Ubuntu #

Follow these steps on Ubuntu to use Subversion (SVN) to access the Snap Backup source code and submit your changes.

## 1. One-Time Environment and SVN Setup ##
**A) Install Subversion and Ant**
> Run _Terminal_ (_Applications_ --> _Accessories_ --> _Terminal_) and enter the following commands at the Linux shell prompt:
> | `$ sudu apt-get install subversion` |
|:------------------------------------|
> | `$ sudu apt-get install ant`        |

**B) Get Your Password**
> To obtain your password (needed for the next step), visit:
> > http://code.google.com/hosting/settings

**C) Download Code**

> Back at the Linux shell prompt, enter the following commands:
> | `$ mkdir ~/Projects` |
|:---------------------|
> | `$ cd ~/Projects`    |
> | `$ svn checkout https://snapbackup.googlecode.com/svn/trunk/ snapbackup --username <USERNAME HERE>` |
> In the above instructions, replace "`<USERNAME HERE>`" with your Google Code username.

## 2. Core Contributor Steps ##
**A) Build Current Project and Verify**
  1. Open _File Browser_ (_Places_ --> _Home Folder_)
  1. Go to the folder `~/Projects/snapbackup/src/tools/`
  1. Double-click and the file _**build.sh**_ and choose _Run in Terminal_
  1. Go to the folder `~/Projects/snapbackup/build/`
  1. Bring up the context menu (a.k.a. control-click menu, speed menu, or right-click menu) for _**snapbackup.jar**_ and choose the "Open with OpenJDK Java 6 Runtime" option
  1. Verify that the Snap Backup application launches and works properly

**B) Edit Code**
> The project files are under the `~/Projects/snapbackup` folder.  Use your editor or IDE to make changes to the appropriate files.

**C) Build the Project and Verify**
> Repeat step A above.

**D) Check Changes into SVN Repository (Server)**
> Run _Terminal_ and enter the following commands at the Linux shell prompt:
> | `$ cd ~/Projects/snapbackup` |
|:-----------------------------|
> | `$ svn update`               |
> | `$ svn status`               |
> | `$ svn commit --message "<COMMENT HERE>."` |
> | `$ svn status`               |
> In the above instructions, replace "`<COMMENT HERE>`" with a short description of the changes you made.



---

[Snap Backup Contributors](http://www.snapbackup.com/about/)