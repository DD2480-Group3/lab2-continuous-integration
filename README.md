# Continous integration server
# Group 3

This repository contains the code and tests for a CI server. The CI server clones a repository from github as specified in the payload, and compiles the code using maven. The tests in the repository is also run using maven. 

## Structure of repository

## Requirements

## How to set up and run server

## How to run tests for server

## Implementation

### Compilation

The first thing that is done before compilation is clonining the repository. The payload of the http request is read as a JSON object, and from that, the repo url and branch is retrived. The repository is then cloned to a specified directory using the JGit api. The specified directory is first deleted in case there already exists one. The code is then compiled using the build function from mavenbuilder. The build function takes in the action to be performed, in this case "compile", and the path to the pom.xml file of the cloned repo. The build is then performed using maven invoker. The build function return true is the build was succesfull, and false if it failed. 

The mavenbuilder compilation is unit tested by having two folders containing a pom.xml and a java file. In one of the folders, the java code should compile and in the other one it shouldn't. The unit tests assert that the one that should compile return true, and the other false. 

### Testing

The tests in the cloned repo is run using the build function from mavenbuilder, but with "test" as a parameter and the same path to the pom.xml file. The test is then performed using maven invoker. The build function return true if the test was succesfull, and false if it failed. 

The mavenbuilder testing is unit tested by having two folders containing a pom.xml file and a test file. In one of the folders, the test should suceed, and in the other one it should fail. The unit tests assert that the one with tests that pass return true, and the other false. 

### Notification

### Statement of contribution

### Essence