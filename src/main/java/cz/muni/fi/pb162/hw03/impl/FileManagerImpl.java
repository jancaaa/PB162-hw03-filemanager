package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.FileManager;

/**
 * @author: Jana Zahradnickova,  UCO 433598
 * @version: 16. 12. 2015
 */
public class FileManagerImpl implements FileManager {
    /**
     * Execute job operation specified in {@code jobPath}. Atomicity of job execution is NOT guaranteed. It may fail
     * during the execution. <br/>
     * However all file modifications must be included in log file.
     *
     * @param jobPath     path to job file
     * @param logFilePath path where log file should be written
     * @throws Exception Throw appropriate exception in case of error. Feel free to define and throw own exceptions.
     */
    @Override
    public void executeJob(String jobPath, String logFilePath) throws Exception {

    }
}
