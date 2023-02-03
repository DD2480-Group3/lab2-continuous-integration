import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONObject;

public class Compiler {

    public Compiler(HttpServletRequest request) {
        cloneRepo(request);

    }

    public void cloneRepo(HttpServletRequest request) {

        // This shouldn't be called when updating the browser for example, only when
        // something is pushed, I believe that's the reason for nullpointers now
        String reqPayload = request.getParameter("payload"); // get payload from request
        if (reqPayload != null) {
            JSONObject payloadJSON = new JSONObject(req2); // create JSONObject from payload
            System.out.println("Payload as JSONObject");
            System.out.println(payloadJSON);

            JSONObject repoJSON = jo.getJSONObject("repository"); // get JSONObject for the repository

            String reponame = repoJSON.getString("name");
            String clone_url = repoJSON.getString("clone_url");
            String ref = payloadJSON.getString("ref"); // branch
            System.out.println("Reponame");
            System.out.println(reponame);
            System.out.println("Clone url");
            System.out.println(clone_url);
            System.out.println("Ref");
            System.out.println(ref);
            System.out.println("Trying to clone...");

            // Try to clone to folder "cloned"
            try {
                Git.cloneRepository()
                        .setURI(clone_url)
                        .setDirectory(new File("cloned"))
                        .call();
                System.out.println("completed cloning");
            } catch (GitAPIException e) {
                e.printStackTrace();
            }

        }

    }

}