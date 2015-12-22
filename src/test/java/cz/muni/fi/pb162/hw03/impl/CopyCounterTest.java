package cz.muni.fi.pb162.hw03.impl;

import static org.junit.Assert.assertTrue;

import static cz.muni.fi.pb162.hw03.support.TestSupport.subPath;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import cz.muni.fi.pb162.hw03.FileManager;
import cz.muni.fi.pb162.hw03.support.TestSupport;

/**
 * Created by jludvice on 24.8.15.
 */
public class CopyCounterTest {
    public static final String FILE_NAME = "document.odt";
    @Rule
    public TestSupport testSupport = TestSupport.forJob("cp_counter");
    private FileManager fileManager;
    private Path logPath;

    @Before
    public void before() throws Exception {
        fileManager = new FileManagerImpl();
        logPath = Paths.get("logs", "cp_counter.log").toAbsolutePath();
        fileManager.executeJob(TestSupport.JOB_FILE_PATH.toString(), logPath.toString());
    }

    @Test
    public void testFileCopied() {
        String file1 = FILE_NAME;
        String file2 = FILE_NAME.replace("[.]", "_001.");

        assertTrue("File " + file1 + " should exist in dest dir", Files.exists(subPath(testSupport.getJobSrc(), file1)));

        assertTrue("File " + file2 + " should also be in destination directory", Files.exists(subPath(TestSupport.WORK_DEST, "docs", file2)));

        assertTrue("File another_document.odt exists in dest dir", Files.exists(subPath(TestSupport.WORK_DEST, "docs", "another_document.odt")));
    }
}
