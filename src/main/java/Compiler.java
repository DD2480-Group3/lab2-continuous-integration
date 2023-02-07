import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.io.FileUtils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.json.JSONObject;


public class Compiler {
    private String repository;
    private String owner;
    private String shaHash;

    public Compiler() {
        repository = "";
        owner = "";
        shaHash = "";

    }

    /**
     * Method for cloning the repository from the github payload.
     * Creates a JSON object from the payload
     * Checks that the event is a push
     * @return
     */
    public Git cloneRepo(HttpServletRequest request) {

        String reqPayload = request.getParameter("payload");
        if (reqPayload != null) {
            JSONObject payloadJSON = new JSONObject(reqPayload);            // create JSONObject from payload
            String sha = payloadJSON.getString("after");                //Get the after commit SHA
            JSONObject repoJSON = payloadJSON.getJSONObject("repository");  // get JSONObject for the repository
            String repo_name = repoJSON.getString("name");
            String clone_url = repoJSON.getString("clone_url");
            String ref = payloadJSON.getString("ref");                  // branch

            //Get owner info
            JSONObject ownerJSON = repoJSON.getJSONObject("owner");
            String owner_name = ownerJSON.getString("name");

            //Store info
            repository = repo_name;
            shaHash = sha;
            owner = owner_name;


            // Try to clone to folder "cloned"
            System.out.println("Trying to clone url: " + clone_url);
            try {

                File directory = new File("cloned");
                if(directory.exists()){

                    try{
                        FileUtils.deleteDirectory(directory);
                    }catch(IOException e){
                        e.printStackTrace();
                    }


                }
                Git git = Git.cloneRepository()
                        .setURI(clone_url)
                        .setDirectory(directory)
                        .setBranchesToClone(Collections.singletonList(ref))
                        .setBranch(ref)
                        .call();
                System.out.println("Completed cloning");
                return git;
            } catch (GitAPIException | JGitInternalException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepository() {
        return repository;
    }

    public String getShaHash() {
        return shaHash;
    }

    public void deleteRepo(Git git) throws IOException {
        git.close();
        git = null;
        FileUtils.deleteDirectory(new File("cloned"));
    }

}