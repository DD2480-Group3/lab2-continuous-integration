import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.json.JSONObject;


public class Compiler {

    private HashMap<String,String> repoInfo;

    public Compiler(HttpServletRequest request) {
        setRepoInfo(request);

    }


    /**
     * Creates a JSON object from the payload and then reads from it.
     * This info is then stored on a HashMap, to be able to access it
     * easily.
     * @param request
     */
    private void setRepoInfo(HttpServletRequest request) {
        String reqPayload = request.getParameter("payload");
        if (reqPayload != null) {
            JSONObject payloadJSON = new JSONObject(reqPayload);                // create JSONObject from payload

            //Get info from repository
            JSONObject repoJSON = payloadJSON.getJSONObject("repository");
            String repo_name = repoJSON.getString("name");
            String clone_url = repoJSON.getString("clone_url");
            String ref = payloadJSON.getString("ref");  // branch
            String git_api_url = repoJSON.getString("statuses_url");

            //Get owner info
            JSONObject ownerJSON = repoJSON.getJSONObject("owner");
            String owner = ownerJSON.getString("name");

            //Add info to HashMap
            repoInfo.put("repo_name",repo_name);
            repoInfo.put("clone_url",clone_url);
            repoInfo.put("ref",ref);
            repoInfo.put("git_api_url",git_api_url);
            repoInfo.put("owner",owner);


        }

    }

    public String get_status_url() {
        String status_url = repoInfo.get("git_api_url");
        return status_url;

    }


    /**
     * Method for cloning a repository with the help of the info retrieved from the Github payload.
     * @return
     */
    public Git cloneRepo() {

        /*String reqPayload = request.getParameter("payload");
        if (reqPayload != null) {
            JSONObject payloadJSON = new JSONObject(reqPayload);            // create JSONObject from payload
            JSONObject repoJSON = payloadJSON.getJSONObject("repository");  // get JSONObject for the repository
            String reponame = repoJSON.getString("name");
            String clone_url = repoJSON.getString("clone_url");
            String ref = payloadJSON.getString("ref");                  // branch*/
        if(!repoInfo.isEmpty()) {
            String clone_url = repoInfo.get("clone_url");
            String ref = repoInfo.get("ref");

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



    public void deleteRepo(Git git) throws IOException {
        git.close();
        //git = null;
        FileUtils.deleteDirectory(new File("cloned"));
    }

}