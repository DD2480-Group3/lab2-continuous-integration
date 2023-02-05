import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


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
        
        String eventType = request.getHeader("type");
        String reqPayload = request.getParameter("payload");
        if(reqPayload != null && eventType != null){
            if(eventType.equals("push")){
                System.out.println("here");
                Compiler compiler = new Compiler(request);
                MavenBuilder builder = new MavenBuilder();

                boolean successBuild = builder.build(Collections.singletonList("compile"), "/testProjects/test1Success/pom.xml");

                if(successBuild) {
                    System.out.println("Builds success");
                } else {
                    System.out.println("Builds failed");
                }

                boolean successTests = builder.build(Collections.singletonList("test"), "/testProjects/test1Success/pom.xml");

                if(successTests) {
                    System.out.println("Test success");
                } else {
                    System.out.println("Test failed");
                }
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
