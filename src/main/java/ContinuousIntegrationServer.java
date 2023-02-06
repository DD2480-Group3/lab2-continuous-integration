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

        String reqPayload = request.getParameter("payload");
        String event = request.getHeader("X-Github-Event");

        System.out.println(reqPayload);
        if(reqPayload != null && event != null){
            if(event.equals("push")){
                Notification notify = new Notification();
                Compiler compiler = new Compiler(request);

                String status_url = compiler.get_status_url();
                notify.postRequest("pending",status_url,"Not done yet");

                Git git = compiler.cloneRepo();
                MavenBuilder builder = new MavenBuilder();

                boolean successBuild = builder.build(Collections.singletonList("compile"), "/cloned/pom.xml");

                if(successBuild) {
                    System.out.println("Builds success");
                    notify.postRequest("pending",status_url,"Build success");

                } else {
                    System.out.println("Builds failed");
                    notify.postRequest("failure",status_url,"Build failed");
                }

                boolean successTests = builder.build(Collections.singletonList("test"), "/cloned/pom.xml");

                if(successTests) {
                    System.out.println("Test success");
                    notify.postRequest("success",status_url,"Build and test succeeded");
                } else {
                    System.out.println("Test failed");
                    notify.postRequest("failure",status_url,"Test failed");

                }
                compiler.deleteRepo(git);
            }
        }



        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code



        response.getWriter().println("CI job done");
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
