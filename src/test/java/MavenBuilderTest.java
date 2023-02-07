import static org.junit.Assert.*;

import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for build function in class MavenBuilder
 * */
public class MavenBuilderTest {

    /**
     * Tests that build is successful when there are no errors in code
     * */
    @Test
    public void testBuildSuccess() {
        MavenBuilder mv = new MavenBuilder();

        boolean resBuildTrue = mv.build(Collections.singletonList("compile"), "/testProjects/test1Success/pom.xml");

        //boolean resBuildTrue = mv.build(Collections.singletonList("compile"), "/testProjects/test4BuildSuccess/pom.xml");
        Assert.assertTrue(resBuildTrue);
    }

    /**
     * Tests that build is not succesful when there are errors in code
     * */
    @Test
    public void testBuildFail() {
        MavenBuilder mv = new MavenBuilder();
        boolean resBuildFalse = mv.build(Collections.singletonList("compile"), "/testProjects/test2BuildFail/pom.xml");
        Assert.assertFalse(resBuildFalse);
    }

    /**
     * Tests that tests returns correct values when tests succeed.
     * This test is not passing right now
     * */
   @Test
    public void testTestsCorrect() {
        MavenBuilder mv = new MavenBuilder();
        boolean resTestsTrue = mv.build(Collections.singletonList("test"), "/testProjects/test1Success/pom.xml");
        Assert.assertTrue(resTestsTrue);
    }

    /**
     * Tests that tests returns correct values when tests not succeed
     * This test is passing, but from the wrong reason
     * */
    @Test
    public void testTestsIncorrect() {
        MavenBuilder mv = new MavenBuilder();
        boolean resTestsFalse = mv.build(Collections.singletonList("test"), "/testProjects/test3TestFail/pom.xml");
        Assert.assertFalse(resTestsFalse);
    }

}
