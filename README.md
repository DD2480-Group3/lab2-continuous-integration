# Continous integration server
# Group 3

This repository contains the code and tests for a CI server. The CI server clones a repository from GitHub as specified in the payload and compiles the code using maven. The tests in the repository are also run using maven.

## Structure of repository

## Requirements

Java version 11  
ngrok  
Maven

## Set up
1. Clone the branch called "assessment"
2. Go to Settings >> Developer settings >> Personal access tokens and generate a Github token
3. Copy this GitHub token key and insert it into the .env file
3. For Windows users: Be sure to have the MAVEN_HOME path configured correctly.

## How to run server
1. Run `mvn clean compile exec:java` in terminal
2. Open up a new terminal and run "./ngrok http 8080"
3. Go to Settings >> Webhooks in your GitHub repository and click on "Add webhook"
4. Add the URL that was generated from ngrok + "/github-webhook/" (e.g. https://81e7-213-64-193-154.ngrok.io/github-webhook/)

## How to run tests for server
1. Run `mvn test` in terminal
2. Go to http://localhost:8080 to check that the CI server is running locally
3. Go to your ngrok forwarding URL (eg. https://81e7-213-64-193-154.ngrok.io/github-webhook/) to check that the CI server is visible from the internet, hence visible from GitHub

## How to view the build history
1. Navigate to the URL that was generated from ngrok + "/history/" (e.g. https://81e7-213-64-193-154.ngrok.io/history/)
2. Click on the build date/time which will redirect to a unique URL which displays relevant build information.

## Implementation

### Compilation

The first thing that is done before compilation is cloning the repository. The payload of the http request is read as a JSON object, and from that, the repository URL and branch is retrived. The repository is then cloned to a specified directory using the JGit API. The code is then compiled using the build function from mavenbuilder. The build function takes in the action to be performed, in this case "compile", and the path to the pom.xml file of the cloned repo. The build is then performed using maven invoker. The build function returns true if the build was successful, and false if it failed. The directory for the cloning is then deleted. 

The mavenbuilder compilation is unit tested by having two folders containing a pom.xml and a java file. In one of the folders, the java code should compile and in the other one it shouldn't. The unit tests assert that the one that should compile return true, and the other false. 

### Testing

The tests in the cloned repo are ran using the build function from mavenbuilder, but with "test" as a parameter and the same path to the pom.xml file. The test is then performed using maven invoker. The build function return true if the test was successful, and false if it failed. 

The mavenbuilder testing is unit tested by having two folders containing a pom.xml file and a test file. In one of the folders, the test should succeed, and in the other one it should fail. The unit tests assert that the one with tests that pass return true, and the other false. 

### Notification
The notification feature was implemented using the REST API to create GitHub commit statuses. This was done by first creating a JSON object with the needed information specified by the GitHub documentation. Then an HTTP Post request containing the JSON object and a GitHub token, which was needed for authorization was created and sent to GitHub, using the REST API.

An .env file was also added so that the users of the CI server can add their own GitHub token without needing to configure it in the code. 

The notification feature was tested by having a separate branch with a simple maven project, where changes were committed to verify that it catches on to 
the successes and failures of the build/tests of the project. Where all three possibilities were tested: build & test success, build failed, build success & test failed.  

### History
The history feature will list every file located in the "./web/history path" of the server as a clickable link (using href) that directs you to a unique URL where the user can view build information. Each time a build is executed a new textfile containing the commit identifier, build date and build logs is generated. The generated files are named after the current date and time (example: 2023-02-30T12-34-56.txt).

The history feature was tested by verifying that files in the "./web/history" directory located on the server can be listed and that new files are generated properly.


### Statement of contribution

Louise Tidestav:  
Implemented cloning of repository, and worked on parts of the compilation and building.
Worked on the unit tests for compilation and building. 

Claudia Berlin: 
Helped to set up some parts of the compilation, building and testing parts. 
Worked on the notification feature that was implemented using REST API to create commit statuses.
Also, tested the whole system to see that it worked properly. 

Robert Scholz:
Implemented the notification feature (P3). Wrote the essence.

Fredrik Jansson:
Implemented the web interface which supports directory listing and browsing/displaying files. Also created code for creating new empty files which are named by the current date and time. 

Houcine Hassani Idrissi: 
Comment all public methods and classes for the Javadoc. 

### Essence

Our team is (almost) in the state of collaborating. The fundament of a team does exist within our group. The basic concepts of Seeded and Formed are fulfilled. The team has confidence in achieving the team missions and trust towards other team members. The communication is straight-forward in addressing project issues and problems, but the communication happens still in a distant and not really open manner. Due to this, we can't say yet that we work as a cohesive unit. Our goal for the remaining two projects is to improve communication and consequently to distribute the work more fairly also in regards of the different skill levels. The output amount might be different, but we will have to distribute the input workload, especially in terms of time investment, more fairly. In summary, we can say that the closer integration of our team will be our top priority, which will help to fill the remaining gaps and reach the next state Performing.
