/**
 * Used to automate a build of a Github repository
 */

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MavenBuilder {

    public MavenBuilder() {

    }

    /**
     * Tries to build & test the repository that is in the given path.
     * Reads the pom.xml file of the given directory then tries to build and test the code with
     * the help of maven.
     */
    public boolean build(List<String> goals, String toCompile) {
        Path currentRelativePath = Paths.get("");
        String current_path = currentRelativePath.toAbsolutePath().toString(); //Get your current dir. path.

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(current_path + toCompile)); //Specify path
        request.setGoals(goals);

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv().get("MAVEN_HOME")));

        /**try {
         InvocationResult result = invoker.execute( request );
         return result.getExitCode() == 0;
         } catch (MavenInvocationException e) {
         return false;
         }**/

        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            return false;
            //throw new RuntimeException("Build failed.", e);
        }

        return true;
    }
}
