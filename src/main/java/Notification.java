import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

//NOT READY YET

public class Notification {
    /**
     * Method for creating the URL for the Git Status API
     */
    public String gitStatusAPI(String owner, String repository, String shaHash) {
        return "https://api.github.com/repos/" + owner + "/" + repository + "/statuses/" + shaHash;
    }

    /**
     * Method for getting the String state of the status
     */
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

    /**
     * Method for creating a HTTP POST request
     */
    public static HttpPost requestHTTP(String owner, String repository, String shaHash, Status state) {
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
    }

    /**
     * Method for sending a HTTP POST request commit status
     */
    public static void sendHTTP(String owner, String repository, String shaHash, Status state) {
        HttpPost httppost = requestHTTP(owner, repository, shaHash, state);

        HttpClients.createDefault().execute(httpPost);
        HttpClients.createDefault().close();
    }

    public enum Status {
        pending,
        success,
        failure,
        error
    }
}