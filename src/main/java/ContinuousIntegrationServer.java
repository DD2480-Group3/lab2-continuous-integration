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
        response.getWriter().println("CI job done");




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
