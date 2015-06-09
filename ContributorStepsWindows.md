# Snap Backup Contributor Steps for Microsoft Windows #

Follow these steps on Windows to use Subversion (SVN) to access the Snap Backup source code and submit your changes.

## 1. One-Time Environment and SVN Setup ##
**A) Download and Install JDK**
> Click the _**Download JDK**_ button for the current version of _**Java Platform, Standard Edition**_:
> > http://www.oracle.com/technetwork/java/javase/downloads/

> Install the JDK with all the default options.

**B) Download and Install Apache Ant**
> Obtain the file _**apache-ant-X.Y.Z-bin.zip**_ (where _**X.Y.Z**_ is the current version, such as _1.8.2_):
> > http://ant.apache.org/bindownload.cgi

> Unzip the file into the folder `C:\Apps\Ant` so that you end up with a folder named: `C:\Apps\Ant\apache-ant-X.Y.Z`

**C) Download and Install SVN Client**
> Click the _**Silk SVN**_ download icon (most likely you'll need the 32-bit edition) at:
> > http://www.sliksvn.com/en/download

> Install with the default "Typical" options.

**D) Get Your GoogleCode.com Password**
> To obtain your password, visit:
> > http://code.google.com/hosting/settings

**E) Download Code**

> Launch _**Command Prompt**_ (_**Start** -->  **All Programs**  --> **Accessories**  -->  **Command Prompt**_), and enter the following commands:
> | `> mkdir \Projects` |
|:--------------------|
> | `> cd \Projects`    |
> | `> svn checkout https://snapbackup.googlecode.com/svn/trunk/ snapbackup --username <USERNAME HERE>` |
> In the above instructions, replace `<USERNAME HERE>` with your Google Code username.

**F) Download and Install NSIS**
> Click the _**Download**_ link to get Nullsoft Scriptable Install System (NSIS) from:
> > http://nsis.sourceforge.net/Download

> Install with the default options.


## 2. Core Contributor Steps ##
**A) Build Current Project and Verify**
> In _Windows Explorer_, go to the `\Projects\snapbackup\src\tools` folder and double-click the _**build.cmd**_ file.

> Now go to the `\Projects\snapbackup\build` folder and double-click _**snapbackup.jar**_.  Verify the Snap Backup application is working properly.

**B) Edit Code**
> The project files are all under the `\Projects\snapbackup` folder.  Use your editor or IDE to make changes to the appropriate files.

**C) Build the Project and Verify**
> Repeat step A above.

**D) Check Changes into SVN Repository (Server)**
> | `> cd \Projects\snapbackup` |
|:----------------------------|
> | `> svn update`              |
> | `> svn status`              |
> | `> svn commit --message "<COMMENT HERE>."` |
> | `> svn status`              |
In the above instructions, replace `<COMMENT HERE>` with a short description of the changes you made.



---

[Snap Backup Contributors](http://www.snapbackup.com/about/)