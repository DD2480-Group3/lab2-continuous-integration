import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;

//Imports for the web interface
import java.io.File;
import java.nio.file.*;

/**
 * Skeleton of a ContinuousIntegrationServer which acts as webhook
 * See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);
        System.out.println(baseRequest);
        System.out.println(request);
        System.out.println(response);

        String gitHubToken = "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"; //Set up your Github Token!!!

        String reqPayload = request.getParameter("payload");
        String event = request.getHeader("X-Github-Event");

        System.out.println(reqPayload);
        if(reqPayload != null && event != null){
            if(event.equals("push")){
                Notification notify = new Notification();
                Compiler compiler = new Compiler();

                Git git = compiler.cloneRepo(request);
                MavenBuilder builder = new MavenBuilder();

                boolean successBuild = builder.build(Collections.singletonList("compile"), "/cloned/test/pom.xml");
                String status_url = notify.gitStatusAPI(compiler.getOwner(), compiler.getRepository(), compiler.getShaHash());

                //Only test if build succeeded.
                if(successBuild) {
                    boolean successTests = builder.build(Collections.singletonList("test"), "/cloned/test/pom.xml");
                    if(successTests) {
                        notify.postRequest("success", status_url, "Build & Tests succeeded", gitHubToken);
                        response.getWriter().println("Build & Test succeeded");
                    } else {
                        notify.postRequest("failure", status_url, "Build succeeded, but tests failed", gitHubToken);
                        response.getWriter().println("Build succeeded, but tests failed");
                    }

                } else {
                    notify.postRequest("failure",status_url,"Build failed", gitHubToken);
                    response.getWriter().println("Build failed");
                }


                compiler.deleteRepo(git);
            }

        }


        //Code for the web interface
        if (target.equals("/history")) { //If the user navigates to /history
            String historyDirectoryPath = "web/history"; //Set the path to the history directory
            File historyDirectory = new File(historyDirectoryPath);
            File[] historyFiles = historyDirectory.listFiles();
            
            response.getWriter().println("<h1>CI history explorer</h1><br><br>");
            for (File file : historyFiles) { //For each file in the history directory
                if (file.isFile()) { //If the file is a file and not a directory
                    //Basic html code is used to display the files
                    response.getWriter().println("<a href=/history/" + file.getName() + ">" + file.getName() + "</a><br><br>");
                }
            }
            
        } else if (target.startsWith("/history/")) { //If the user navigates to /history/...
            String historyDirectoryPath = "web/history"; //Set the path to the history directory
            File historyDirectory = new File(historyDirectoryPath);
            File[] historyFiles = historyDirectory.listFiles();

            String fileName = target.substring(9); //Get the file name from the url (starts at position 9), will be used to check if the file exists

            //If the file exists, display it otherwise display an error message
            if (new File(historyDirectoryPath + "/" + fileName).exists()) { //Check if the file exists
                response.getWriter().println("<h1>CI history explorer</h1>");
                response.getWriter().println("<h2>Currently at: " + fileName + "</h2>"); //Print the file name
                response.getWriter().println("<h2><a href=/history>Back</a></h2><br><br>");
                //Read the file into a string and display it
                String fileContent2 = new String(Files.readAllBytes(Paths.get(historyDirectoryPath + "/" + fileName)));
                response.getWriter().println(fileContent2);
            } else { //If the file does not exist display an error message
                response.getWriter().println("<h1>CI history explorer</h1>");
                response.getWriter().println("<h2><a href=/history>Back</a></h2><br><br>");
                response.getWriter().println("<b>ERROR: File not found</b>");
            }

        }

         else {
            response.getWriter().println("CI job done");
        }




        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code



    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
