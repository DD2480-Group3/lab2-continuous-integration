# Continous integration server
# Group 3

This repository contains the code and tests for a CI server. The CI server clones a repository from github as specified in the payload, and compiles the code using maven. The tests in the repository is also run using maven. 

## Structure of repository

## Requirements

Java version 11  
ngrok  
Maven  

## Set up
1. Clone branch assesement
Windows: in pom.xml set maven path in plugin section  
Mac: comment out that section  

## How to run server
1. run `mvn clean compile exec:java` in terminal
2. run ngrok http 8080 in a new terminal
3. add the url from ngrok as webhook in github repo

## How to run tests for server
1. run `mvn test` in terminal

## Implementation

### Compilation

The first thing that is done before compilation is clonining the repository. The payload of the http request is read as a JSON object, and from that, the repo url and branch is retrived. The repository is then cloned to a specified directory using the JGit api. The code is then compiled using the build function from mavenbuilder. The build function takes in the action to be performed, in this case "compile", and the path to the pom.xml file of the cloned repo. The build is then performed using maven invoker. The build function return true is the build was succesfull, and false if it failed. The directory for the cloning is then deleated. 

The mavenbuilder compilation is unit tested by having two folders containing a pom.xml and a java file. In one of the folders, the java code should compile and in the other one it shouldn't. The unit tests assert that the one that should compile return true, and the other false. 

### Testing

The tests in the cloned repo is run using the build function from mavenbuilder, but with "test" as a parameter and the same path to the pom.xml file. The test is then performed using maven invoker. The build function return true if the test was succesfull, and false if it failed. 

The mavenbuilder testing is unit tested by having two folders containing a pom.xml file and a test file. In one of the folders, the test should suceed, and in the other one it should fail. The unit tests assert that the one with tests that pass return true, and the other false. 

### Notification

### Statement of contribution

Louise Tidestav:  
Implemented cloning of repo, and worked on parts of the compilation and building.
Worked on the unit tests for compilation and builing. 

### Essence