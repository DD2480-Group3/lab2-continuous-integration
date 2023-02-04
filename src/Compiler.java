import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.json.JSONObject;

public class Compiler {

    public Compiler(HttpServletRequest request) {
        cloneRepo(request);
    }

    /**
     * Method for cloning the repository from the github payload.
     * Creates a JSON object from the payload
     * Checks that the event is a push
    */
    public void cloneRepo(HttpServletRequest request) {
        String eventType = request.getHeader("type");
        String reqPayload = request.getParameter("payload");
        if (reqPayload != null && eventType.equals("type")) {
            JSONObject payloadJSON = new JSONObject(reqPayload);            // create JSONObject from payload
            JSONObject repoJSON = payloadJSON.getJSONObject("repository");  // get JSONObject for the repository
            String reponame = repoJSON.getString("name");
            String clone_url = repoJSON.getString("clone_url");
            String ref = payloadJSON.getString("ref");                  // branch

            // Try to clone to folder "cloned"
            System.out.println("Trying to clone url: " + clone_url);
            try {
                Git.cloneRepository()
                        .setURI(clone_url)
                        .setDirectory(new File("cloned"))
                        .call();
                System.out.println("Completed cloning");
            } catch (GitAPIException | JGitInternalException e) {
                e.printStackTrace();
            }

        }

    }

}