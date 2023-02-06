package src.test.java;

import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Test;

public class NotificationTest {
    /**
     * Method for testing if the GitHub API URL is correct.
     */
    public void testGitStatusAPI() {
        String owner = "Tester";
        String repository = "TestRepository";
        String shaHash = "22jho06ifbqtiq8k2oq2rc4xh5wphi3nwd1sbpmo";
        assertEquals("https://api.github.com/repos/Tester/TestRepository/statuses/22jho06ifbqtiq8k2oq2rc4xh5wphi3nwd1sbpmo", gitStatusAPI(owner, repository, shaHash));
    }
    /**
     * Method for testing if failure status is correct.
     */
    public void failureStatus() {
        String owner = "Tester";
        String repository = "TestRepository";
        String shaHash = "22jho06ifbqtiq8k2oq2rc4xh5wphi3nwd1sbpmo";
        assertEquals("failure", stateOfStatus(failure));
    }
    /**
     * Method for testing if pending status is correct.
     */
    public void pendingStatus() {
        String owner = "Tester";
        String repository = "TestRepository";
        String shaHash = "22jho06ifbqtiq8k2oq2rc4xh5wphi3nwd1sbpmo";
        assertEquals("pending", stateOfStatus(pending));
    }
    /**
     * Method for testing if success status is correct.
     */
    public void successStatus() {
        String owner = "Tester";
        String repository = "TestRepository";
        String shaHash = "22jho06ifbqtiq8k2oq2rc4xh5wphi3nwd1sbpmo";
        assertEquals("success", stateOfStatus(success));
    }
    /**
     * Method for testing if sending a HTTP POST request commit status works
     */
    public void sendHTTPTest() {
        String owner = "Tester";
        String repository = "TestRepository";
        String shaHash = "22jho06ifbqtiq8k2oq2rc4xh5wphi3nwd1sbpmo";
        String state = "failure";
        HttpPost httpPost = sendHTTP(owner, repository, shaHash, state);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        assertEquals(201, httpResponse.getStatusLine().getStatusCode());
    }

}
