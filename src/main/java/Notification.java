import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

/**
A class for sending notifications to Github repository.
*/
public class Notification {

    /**
     * Generates the URL for the Github Commit Status API.
     * @param owner the owner of the repository.
     * @param repository the name of the repository.
     * @param shaHash the SHA hash of the commit.
     * @return the URL for the Github Commit Status API 
    */
    public String gitStatusAPI(String owner, String repository, String shaHash) {
        return "https://api.github.com/repos/" + owner + "/" + repository + "/statuses/" + shaHash;
    }

    /**
     * Method for creation JsonObject used to send to the Git, following: "https://docs.github.com/en/rest/commits/statuses?apiVersion=2022-11-28#create-a-commit-status".
     * @param status the status of the commit
     * @param gitStatusAPI the URL for the Github Commit Status API
     * @param description the description of the status
     * @return the JSON object for sending a commit status to Github
     */
    public JsonObject createJSONObj(String status, String gitStatusAPI , String description) {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("url", gitStatusAPI)
                .add("state", status)
                .add("description", description).build();

        return jsonObject;
    }


    /**
     * Method for creating and sending an HTTP POST request to the gitAPI.
     * @param status the status of the commit
     * @param status_url the URL for the Github Commit Status API
     * @param description the description of the status
     * @param githubToken the Github access token
     * @throws IOException if there is an error in sending the request.
     */
    public void postRequest(String status, String status_url ,String description, String githubToken) throws IOException {

        HttpPost httpPost = new HttpPost(status_url);

        //String githubToken = System.getenv("GITHUB_TOKEN");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + githubToken);

        JsonObject jsonObject = createJSONObj(status, status_url, description);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));

        HttpClients.createDefault().execute(httpPost);
        HttpClients.createDefault().close();

    }



 }


