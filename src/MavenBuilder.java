/**
 * Used to automate a build of a Github repository
 */

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class MavenBuilder {

    public MavenBuilder() {
        build();
    }

    /**
     * Tries to build & test the repository that is in the given path.
     * Reads the pom.xml file of the given directory then tries to build and test the code with
     * the help of maven.
     */
    private void build() {
        Path currentRelativePath = Paths.get("");
        String current_path = currentRelativePath.toAbsolutePath().toString(); //Get your current dir. path.

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( current_path + "/cloned/pom.xml" ) ); //Specify path
        //System.out.println(current_path + "/cloned/pom.xml");
        request.setGoals(Collections.singletonList("test"));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv().get("MAVEN_HOME")));

        try {
            invoker.execute( request );
        } catch (MavenInvocationException e) {
            throw new RuntimeException("Build failed.", e);
        }
    }



}
