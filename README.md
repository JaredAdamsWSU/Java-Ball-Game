# Java-Ball-Game
My machine during developement is running on these specifications:

#### Java Version:

java version "1.8.0_151" <br />
Java(TM) SE Runtime Environment (build 1.8.0_151-b12) <br />
Java HotSpot(TM) 64-Bit Server VM (build 25.151-b12, mixed mode) <br />

#### Operating System:

Microsoft Windows [Version 10.0.15063] <br />
(c) 2017 Microsoft Corporation. All rights reserved. <br />

#### IDE:

Eclipse IDE for Java Developers <br />
Version: Oxygen.1a Release (4.7.1a)
Build id: 20171005-1200
(c) Copyright Eclipse contributors and others 2000, 2017.  All rights reserved. 

## Compiling and Running The Project
Download and install Java Software Development Kit (JDK) from the link:
http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html
On Windows: Before you run your code on command line, make sure that Windows can find the Java
compiler and interpreter by adding the Java installation directory to the Windows ‘Path’ environment
variable.
(Go to Computer -> System Properties -> Advanced system settings -> Environment Variables -> System
variables and Edit Path variable under System Variables. Add the Java installation path (e.g. C:\Program
Files\Java\jdk-9.0.1\bin ) to the beginning of the ‘Path’ variable. )
On Macs: If the installation of Eclipse fails try the following:
https://stackoverflow.com/questions/46570624/mac-os-x-eclipse-ide-installation-metadata-log-error

## To run your code on the command line:
Put all java files, .jar files for the modules you use, applet.policy file, and the config.XML file in
a single directory. You can then compile the program with the following command:
 javac *.java
and run the applet with
 appletviewer Main.java -J-Djava.security.policy=applet.policy
applet.policy grants all permissions to the applet to access the local resources.
You may alternatively use an IDE (e.g., Eclipse). (You can download Eclipse at https://www.eclipse.org/downloads/)
On Mac or Linux: Change line #99 as follows before you compile and run your code:
 File fXmlFile = new File("./config.xml");
When you open the skeleton code on Eclipse if there are compile time errors, the environment for
Eclipse may not be set directly. Go to Window>Preferences, click Java/InstalledJREs. Make sure that the
correct JDK is selected.

## To get your java version:
In a terminal simply run the command - <br />
java -version [Enter] <br />
