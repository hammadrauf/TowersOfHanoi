# Towers of Hanoi - A Simulation
It is an open source and free software. It can be used to teach the concept of "Recursion" or "Towers of Hanoi - Solutions" to computer science students.
It shows on screen a window with animated solution to Towers of Hanoi, optionally can produce text output of all disk movements. Students can edit or modify
the code according to their study objectives. One can also perform a comparision between recursive and iterative algorithm.

##### Note: It uses Java Applets.

## For Teachers and Students
The code has been partitioned in easy to use classes. 
Teachers/Students needing to focus on using this software as a simulation tool in Java for "Towers of Hanoi" problem,
may just need to edit the class 'Main.java' only. In particular they may want to delete/edit/recreate the method 'Main.towersOfHanoi'.
Some other useful methods are:

- Main.java
  - public void recursiveTowersOfHanoi(int diskNo, Towers.Pole sourcePole, Towers.Pole destinationPole, Towers.Pole otherPole)
    - Recursive towersOfHanoi method. Learning tip: Hide/delete this method if you want someone to learn to solve the classic problem.
  - public void iterativeTowersOfHanoi(int diskNo, Towers.Pole sourcePole, Towers.Pole destinationPole, Towers.Pole otherPole)
    - Iterative towersOfHanoi method. Learning tip: Hide/delete this method if you want someone to learn to solve the classic problem.
- Towers.java
  - public boolean moveSingleDisk(Pole fromPole, Pole toPole) throws Exception
    - Move a Single Disk from fromPole to toPole. Throws exception if Sound beep (speaker) issue or memory outage. Returns true if move is successful. Returns false if invalid move, also plays a beep if beep is enabled.
  - public boolean isPoleAllDiskMoved(Pole p, int totalDisks)
    - Used to verify that the "Towers Of Hanoi" problem has been solved. Returns true is all disks are present on the given pole parameter "p".

## Executable Binary JAR files
Executable binary JAR file is included in "./binary-download" folder for convenience. Download via Browser may corrupt this file. If the file is corrupted,
 you can use GIT commands to copy the entire repository, using the command:
```
git clone https://github.com/hammadrauf/TowersOfHanoi.git
cd TowersOfHanoi
cd binary-download
java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 3
```
You can also download the binary file with 'curl' or 'wget' command.
```
curl -H "Accept: application/zip" https://github.com/hammadrauf/TowersOfHanoi/tree/main/binary-download/TowersOfHanoi-1.0-jar-with-dependencies.jar -o TowersOfHanoi-1.0-jar-with-dependencies.jar
OR
wget https://github.com/hammadrauf/TowersOfHanoi/tree/main/binary-download/TowersOfHanoi-1.0-jar-with-dependencies.jar
THEN
java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 3
```


## Screen Shots

![Screen capture 1 of Output from Towers of Hanoi](https://github.com/hammadrauf/TowersOfHanoi/blob/main/binary-download/ScreenShot.png)

![Screen capture 2 of Output from Towers of Hanoi](https://github.com/hammadrauf/TowersOfHanoi/blob/main/binary-download/ScreenShot2.png)

![Screen capture 3 of Output from Towers of Hanoi](https://github.com/hammadrauf/TowersOfHanoi/blob/main/binary-download/ScreenShot3.png)

## Requirements
- Java version 11
  - It uses deprecated JavaApplets. That has no ill effects other then the compiler giving warnings during compile time. Java Applets are deprecated and may not be supported in later versions of Java.
- Tested Okay with Java (JDK) version 16.

## Getting Help on Usage
    C:\TowersOfHanoi\binary-download>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar -h
    usage: towersofhanoi [-h] [-s SLOWNESS] [-l {true,false}] [-i {true,false}]
                         [-n {true,false}] [-d {true,false}] N
    
    Perform  "Towers  of  Hanoi"  simulation   for  given  inputs,  recursivley
    (default) or iteratively.
    
    positional arguments:
      N                      number of disks to move from tower. 1 or Higher.
    
    named arguments:
      -h, --help             show this help message and exit
      -s SLOWNESS, --slowness SLOWNESS
                             slowness of animation. 0 or Higher. Default is 0
      -l {true,false}, --logging {true,false}
                             set  if  text  ouput   log  messages  are  needed.
                             Boolean value. Default is false.
      -i {true,false}, --iterative {true,false}
                             set if iterative method  is needed. Boolean value.
                             Default is false.
      -n {true,false}, --nobeep {true,false}
                             set if silent mode  (no  beep)  is needed. Boolean
                             value. Default is false (There will be beeps).
      -d {true,false}, --displayoff {true,false}
                             set if no GUI  display  is  needed. Boolean value.
                             Default is false (There  will be display). Logging
                             is enabled if this is set.

## Usage Tips
1. Open command prompt and type any of the following:
```
C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 6 
C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 6 -l true
C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 6 -l true -s 20
C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 12 -s 3
```	
2. -l, --logging {true, false} option will log messages on the standard output (Console) by default.
3. Close the Window frame to exit the application, otherwise it will keep on executing.
4. You can Zoom in or out using the Mouse roller button.
5. You can save the Window image in a JPEG/PNG/etc. file from File menu.
6. To use Iterative alogorithm instead of recursive, use switch "-i true"
7. For really large number of disks (More than 20) you can either:
  a. Use a slowness value in hundreds (for example 300).
```
C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 25 -s 300 -l true
C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 25 -s 300 -l true -i true
```
  b. Turn-off the display.
```
C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 25 -d true
C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 25 -d true -i true
```
8. To disable beep sound, on invalid moves, use switch "-n true"

## Building the Source Code
1. You will need:
  - Java Development Kit (JDK) version 11.
  - Either one of:
    - Apache Maven (Command line build), 
    - Apache NetBeans IDE (GUI Builder), 
	- Eclipse IDE (GUI Builder), Not tested by Author,
    - Visual Studio Code (GUI Builder), Not tested by Author, OR
    - IntelliJ IDEA (GUI Builder), Not tested by Author.
2. Maven build tools should automatically get all binary dependencies and produce 2 jar files in "./target" folder.
```
mvn clean
mvn package
```
