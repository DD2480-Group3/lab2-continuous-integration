import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Collections;
import io.github.cdimascio.dotenv.Dotenv;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;


/**
 * Skeleton of a ContinuousIntegrationServer which acts as webhook.
 * See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    /** 
    Handles incoming HTTP requests.
    When a "push" event is received from GitHub, this method triggers continuous integration tasks, including:
    cloning the repository, compiling the code, running tests, and updating the commit status on GitHub.
    @param target The target of the request.
    @param baseRequest The original unwrapped request.
    @param request The request object.
    @param response The response object.
    @throws IOException If an input/output error occurs.
    @throws ServletException If a servlet error occurs.
    */
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

        Dotenv dotenv = Dotenv.load();
        String gitHubToken = dotenv.get("GITHUB_TOKEN");

        String reqPayload = request.getParameter("payload");
        String event = request.getHeader("X-Github-Event");

        BuildHistory history = new BuildHistory();
        history.navigateHistory(response,target);

        System.out.println(reqPayload);
        if(reqPayload != null && event != null){
            if(event.equals("push")){
                Notification notify = new Notification();
                Compiler compiler = new Compiler();

                Git git = compiler.cloneRepo(request);
                MavenBuilder builder = new MavenBuilder();

                boolean successBuild = builder.build(Collections.singletonList("compile"), "/cloned/pom.xml");
                String status_url = notify.gitStatusAPI(compiler.getOwner(), compiler.getRepository(), compiler.getShaHash());

                //Only test if build succeeded.
                if(successBuild) {
                    boolean successTests = builder.build(Collections.singletonList("test"), "/cloned/pom.xml");
                    if(successTests) {
                        String description = "Build & Tests succeeded";
                        notify.postRequest("success", status_url, description, gitHubToken);
                        history.createHistoryFile(response,compiler.getShaHash(),description);
                        response.getWriter().println("Build & Test succeeded");
                    } else {
                        String description = "Build succeeded, but tests failed";
                        notify.postRequest("failure", status_url, description, gitHubToken);
                        history.createHistoryFile(response,compiler.getShaHash(),description);
                        response.getWriter().println("Build succeeded, but tests failed");
                    }

                } else {
                    String description = "Build failed";
                    notify.postRequest("failure",status_url,description, gitHubToken);
                    history.createHistoryFile(response,compiler.getShaHash(),description);
                    response.getWriter().println("Build failed");
                }


                compiler.deleteRepo(git);
            }

        }





        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code



    }
 
    // used to start the CI server in command line

    /**
    Main method to start the ContinuousIntegrationServer.
    @param args The command line arguments.
    @throws Exception If an error occurs while starting the server.
    */
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
