
//import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpClient;
//NOT READY YET


public class Notification {

    /**
     * Method for creating the URL for the Git Status API
     */
    /*public String gitStatusAPI(String owner, String repository, String shaHash) {
        return "https://api.github.com/repos/" + owner + "/" + repository + "/statuses/" + shaHash;
    }*/

    /**
     * Method for creation JsonObject used to send to the Git, following: "https://docs.github.com/en/rest/commits/statuses?apiVersion=2022-11-28#create-a-commit-status".
     * @param status
     * @param gitStatusAPI
     * @param description
     * @return
     */
    private JsonObject createJSONObj(String status, String gitStatusAPI , String description) {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("url", gitStatusAPI)
                .add("state", status)
                .add("description", description).build();

        return jsonObject;
    }


     /**
 * Method for creating a HTTP POST request
 */
    public void postRequest(String status,String status_url, String description) throws IOException {

    HttpPost httpPost = new HttpPost(status_url);

    String githubToken = System.getenv("GITHUB_TOKEN");
    httpPost.addHeader("Content-Type", "application/json");
    httpPost.addHeader("Authorization", "Bearer " + githubToken);

    JsonObject jsonObject = createJSONObj(status, status_url, description);
    httpPost.setEntity(new StringEntity(jsonObject.toString()));

    HttpClients.createDefault().execute(httpPost);
    HttpClients.createDefault().close();

    }

    /**
    * Method for sending a HTTP POST request commit status
    */
    /*public static void sendHTTP(String owner, String repository, String shaHash, Status state) {
    HttpPost httppost = requestHTTP(owner, repository, shaHash, state);

    HttpClients.createDefault().execute(httpPost);
    HttpClients.createDefault().close();
    }*/

    /*public HttpPost requestHTTP(String owner, String repository, String shaHash, Status state) {
    String URL = gitStatusAPI(owner, repository, shaHash);

    HttpPost httpPost = new HttpPost(URL);

    String description = stateOfStatus(state);

    JSONObject jsonObject = new JSONObject();
    jsonObject.addProperty("state", state);
    jsonObject.addProperty("target_url", URL);
    jsonObject.addProperty("description", description);
    jsonObject.addProperty("context", "-");

    httpPost.addHeader("Content-Type", "application/vnd.github+json");
    httpPost.addHeader("Authorization", "Bearer " + githubToken);

    httpPost.setEntity(new StringEntity(jsonObject.toString()));

    return httpPost;
 }*/


 }

    /**
     * Method for getting the String state of the status
     *
    public static String stateOfStatus(Status state) {
        String description;
        if (state.equals(failure)) {
            description = "failure";
        } else if (state.equals(pending)) {
            description = "pending";
        } else if (state.equals(success)) {
            description = "success";
        } else {
            description = "error";
        }
        return description;
    }




    public enum Status {
        pending,
        success,
        failure,
        error
    }
}
*/