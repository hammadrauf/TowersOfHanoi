# Towers of Hanoi - A Simulation
It is an open source and free software. It can be used to teach the concept of "Recursion" to computer science students.

## Requirements
Java version 11.
It uses deprecated JavaApplets. That has no ill effects other then the compiler giving warnings during compile time.
Java Applets are deprecated and may not be supported in later versions of Java.

## Getting Help on Usage
    C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar --help
    usage: towersofhanoi [-h] [-s SLOWNESS] [-l {true,false}] N
    
    Perform "Towers of Hanoi" simulation for given inputs, recursivley.
    
    positional arguments:
      N                      number of disks to move from tower. 1 or Higher.
    
    named arguments:
      -h, --help             show this help message and exit
      -s SLOWNESS, --slowness SLOWNESS
                             slowness of animation. 0 or Higher. Default is 0
      -l {true,false}, --logging {true,false}
                             set  if  text  ouput   log  messages  are  needed.
                             Boolean value. Default is false.
				
## Usage Tips
1. Open command prompt and type any of the following:
    C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 6 
    C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 6 -l true
    C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 6 -l true -s 20
    C:\>java -jar TowersOfHanoi-1.0-jar-with-dependencies.jar 12 -s 3			
2. -l, --logging {true, false} option will log messages on the standard output (Console) by default.
3. Close the Window frame to exit the application, otherwise it will keep on executing.
4. You can Zoom in or out using the Mouse roller button.
5. You can save the Window image in a JPEG/PNG/etc. file from File menu.