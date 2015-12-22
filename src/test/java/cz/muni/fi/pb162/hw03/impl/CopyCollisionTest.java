package cz.muni.fi.pb162.hw03.impl;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static cz.muni.fi.pb162.hw03.support.TestSupport.subPath;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.hamcrest.Matcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import cz.muni.fi.pb162.hw03.FileManager;
import cz.muni.fi.pb162.hw03.impl.FileManagerImpl;
import cz.muni.fi.pb162.hw03.support.TestSupport;

/**
 * This test is very similar to {@link cz.muni.fi.pb162.hw03.impl.CopyCounterTest},
 * but this one also tests log file correctness.
 *
 * @author Josef Ludvicek
 */
public class CopyCollisionTest {
    public static final String FIRST_FILE = "file.txt";
    public static final String SECOND_FILE = "file_001.txt";

    @Rule
    public TestSupport testSupport = TestSupport.forJob("cp_collision");

    private FileManager fileManager;
    private Path logPath;

    @Before
    public void before() throws Exception {
        fileManager = new FileManagerImpl();
        logPath = Paths.get("logs", "cp_collision.log").toAbsolutePath();
        fileManager.executeJob(TestSupport.JOB_FILE_PATH.toString(), logPath.toString());
    }

    @Test
    public void testCollidingCopy() {
        assertTrue(String.format("First file named '%s' should exist in target dir.", FIRST_FILE),
                Files.exists(subPath(TestSupport.WORK_DEST, FIRST_FILE)));

        assertTrue(String.format("Second file named '%s' should exist in target dir.", SECOND_FILE),
                Files.exists(subPath(TestSupport.WORK_DEST, SECOND_FILE)));
    }

    @Test
    public void testLogEntries() {
        try {
            List<String> logEntries = Files.readAllLines(logPath, FileManager.JOB_FILE_CHARSET);

            assertThat("Log should contain entries for both files.",
                    logEntries, allOf(
                            hasSize(2),
                            (Matcher) hasItem(containsString(SECOND_FILE)),
                            (Matcher) hasItem(containsString(FIRST_FILE))
                    ));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
