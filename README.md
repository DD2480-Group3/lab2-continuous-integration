# Continous integration server
# Group 3

This repository contains the code and tests for a CI server. The CI server clones a repository from github as specified in the payload, and compiles the code using maven. The tests in the repository is also run using maven. 

## Structure of repository

## Requirements

Java version 11  
ngrok  
Maven  

## Set up
1. Clone the branch called "assesement"
2. Configure the pom.xml depending on what OS your using:
 - Windows: In the configuration section of the pom.xml set your MAVEN_HOME, e.g. "<maven.home>C:\Program Files\apache-maven-3.8.7</maven.home>"
 - Mac: Comment out that section  

## How to run server
1. Run `mvn clean compile exec:java` in terminal
2. Open up a new terminal and run "./ngrok http 8080"
3. Go to Settings >> Webhooks in Github and click on Add webhook
4. Add the url that was generated from ngrok + "/github-webhook/" (e.g. https://81e7-213-64-193-154.ngrok.io/github-webhook/)

## How to run tests for server
1. Run `mvn test` in terminal
2. Go to http://localhost:8080 to check that the CI server is running locally
3. Go to your Ngrok forwarding URL (eg. https://81e7-213-64-193-154.ngrok.io/github-webhook/) to check that the CI server is visible from the internet, hence visible from Github

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

Claudia Berlin: 
Worked on parts of the compilation and building of the project.

### Essence
