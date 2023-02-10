/**
 * Used to automate a build of a Github repository
 */

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MavenBuilder {

    /**
    Represents the MavenBuilder class that is used to automate a build of a Github repository.
    */
    public MavenBuilder() {

    }

    /**
     * Tries to build & test the repository that is in the given path.
     * Reads the pom.xml file of the given directory then tries to build and test the code with
     * the help of maven.
     * @param goals the list of goals that are to be executed
     * @param toCompile the path of the pom.xml file of the repository
     *@return a boolean value indicating whether the build was successful (exit code = 0) or not
     *@throws MavenInvocationException if an error occurs during the build process
     */
    public boolean build(List<String> goals, String toCompile) {
        Path currentRelativePath = Paths.get("");
        String current_path = currentRelativePath.toAbsolutePath().toString(); //Get your current dir. path.

        DefaultInvocationRequest request = new DefaultInvocationRequest();
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
            InvocationResult result = invoker.execute(request);
            System.out.println("---------------");
            System.out.println(toCompile);
            System.out.println(result);
            return result.getExitCode() == 0;
        } catch (MavenInvocationException e) {
            return false;
            //throw new RuntimeException("Build failed.", e);
        }

    }
}
