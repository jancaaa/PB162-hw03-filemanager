package cz.muni.fi.pb162.hw03.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import cz.muni.fi.pb162.hw03.FileManager;
import cz.muni.fi.pb162.hw03.support.TestSupport;

/**
 * Created by jludvice on 16.8.15.
 */
public class Job1Test {

    @Rule
    public TestSupport testSupport = TestSupport.forJob("job1");

    private FileManager fileManager;
    private Path logPath;

    public static final String DOC_NAME = "awesome doc.odt";
    public static final String PICTURE_NAME = "jpg.gif";

    @Before
    public void before() {
        fileManager = new FileManagerImpl();
        logPath = Paths.get("logs", "job1.log").toAbsolutePath();
    }

    @Test
    public void testJob1() {
        try {
            fileManager.executeJob(TestSupport.JOB_FILE_PATH.toString(), logPath.toString());
        } catch (Exception e) {
            fail("Execution of job1 should pass. Do you handle comments and other things in jobFile correctly?");
        }

        // awesome doc.odt
        assertTrue("File " + DOC_NAME + " exists in original place", Files.exists(
                        testSupport.subPath(testSupport.getJobSrc(), DOC_NAME)
                )
        );
        assertTrue("File " + DOC_NAME + " exists in destination place", Files.exists(
                        testSupport.subPath(TestSupport.WORK_DEST, "docs", DOC_NAME)
                )
        );

        // a.png
        assertFalse("File a.png should be deleted.", Files.exists(
                        testSupport.subPath(testSupport.getJobSrc(), "a.png")
                )
        );

        // picture "jpg.gif"
        assertFalse("File " + PICTURE_NAME + " should be moved", Files.exists(
                        testSupport.subPath(testSupport.getJobSrc(), "sub", "folder", PICTURE_NAME)
                )
        );
        assertTrue("File " + PICTURE_NAME + " exists in destination place", Files.exists(
                        testSupport.subPath(TestSupport.WORK_DEST, "pictures", PICTURE_NAME)
                )
        );
    }
}
