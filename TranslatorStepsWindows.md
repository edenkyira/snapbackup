# Snap Backup Translator Steps for Microsoft Windows #

Follow these steps on Windows to use Subversion (SVN) to access the appropriate Snap Backup properties file and submit your changes.

## 1. One-Time Environment and SVN Setup ##
**A) Download and Install JDK**
> Click the _**Download**_ button for _**JDK 6 Update X**_ (where _**X**_ is the current update version) at:
> > http://java.sun.com/javase/downloads/

> Install the JDK with all the default options.

**B) Download and Install Apache Ant**
> Obtain the file _**apache-ant-X.Y.Z-bin.zip**_ (where _**X.Y.Z**_ is the current version, such as _1.7.1_):
> > http://ant.apache.org/bindownload.cgi

> Unzip the file into the folder `C:\Apps\Ant` so that you end up with a folder named: `C:\Apps\Ant\apache-ant-X.Y.Z`

**C) Download and Install SVN Client**
> Click the _**Silk SVN**_ download icon (there's a 32-bit edition and a 64-bit edition) at:
> > http://www.sliksvn.com/en/download

> Install with the default options.

**D) Get Your GoogleCode.com Password**
> To obtain your password, visit:
> > http://code.google.com/hosting/settings

**E) Download Code**

> Launch _**Command Prompt**_ (_**Start** -->  **Programs**  --> **Accessories**  -->  **Command Prompt**_), and enter the following commands:
> | `> mkdir \Projects` |
|:--------------------|
> | `> cd \Projects`    |
> | `> svn checkout https://snapbackup.googlecode.com/svn/trunk/ snapbackup --username <USERNAME HERE>` |
> In the above instructions, replace `<USERNAME HERE>` with your Google Code username.


## 2. Core Contributor Steps ##
**A) Build Current Project and Verify**
> In _Windows Explorer_, go to the `\Projects\snapbackup\src\tools` folder and double-click the _**build.cmd**_ file.

> Now go to the `\Projects\snapbackup\build` folder and double-click _**snapbackup.jar**_.  Verify the Snap Backup application is working properly.

**B) Edit Code**
> Use your editor to make changes to the appropriate properties file located in the `\Projects\snapbackup\src\resources\properties` folder.  Be sure to save the file with Unicode UTF-8 (no BOM) encoding.

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

**E) Post a Message to the Mailing List**
> Let the other translators know about your updated properties file by sending a quick note to the [Snap Backup Translators Mailing List](http://groups.google.com/group/snapbackup-translate).



---

[Snap Backup Contributors](http://www.snapbackup.com/about/)