import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;


public class Notification {

    /**
     * Method for creating the URL for the Git Status API
     */
    public String gitStatusAPI(String owner, String repository, String shaHash) {
        return "https://api.github.com/repos/" + owner + "/" + repository + "/statuses/" + shaHash;
    }

    /**
     * Method for creation JsonObject used to send to the Git, following: "https://docs.github.com/en/rest/commits/statuses?apiVersion=2022-11-28#create-a-commit-status".
     * @param status
     * @param gitStatusAPI
     * @param description
     * @return
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
     * @param status
     * @param status_url
     * @param description
     * @throws IOException
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


