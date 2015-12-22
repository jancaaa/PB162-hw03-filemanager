package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.FileManager;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

/**
 * @author: Jana Zahradnickova,  UCO 433598
 * @version: 15. 12. 2015
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
        if (jobPath == null || logFilePath == null)
            throw new NullPointerException("Parameters can not be null.");

        if (!new File(jobPath).exists()) {
            throw new IllegalArgumentException("jobPath file does not exist.");
        }

        File logFile = createLogFile(logFilePath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(jobPath)))) {
            String root = processFirstLine(reader.readLine()); //process first line (required)
            File rootDirectory = new File(root); //rootDictionary - dir to clean

            String line = reader.readLine(); //read second line (required)
            if (line == null)
                throw new WrongFileFormatException("Wrong format of jobPath file - second line is required.");

            do { //process line
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; //empty line or comment -> skip this line
                } else {
                    Command cmd = createCommand(line, logFile);
                    walkRecursively(rootDirectory, cmd);
                }
            } while ((line = reader.readLine()) != null); //read other lines (optional)
        }
    }

    /**
     * Create logFile and directories on the path
     *
     * @param logFilePath path to logFile
     * @return logFile as File
     * @throws IllegalArgumentException logPath contains illegal character/s
     */
    private File createLogFile(String logFilePath) throws IllegalArgumentException {
        if (logFilePath == null)
            throw new NullPointerException();

        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            logFile.getParentFile().mkdirs(); //create nonexistent dirs on logFile path

            try {
                logFile.createNewFile(); //create logFile
            } catch (IOException e) {
                throw new IllegalArgumentException("logFilePath contains illegal character/s.");
            }
        }
        return logFile;
    }

    /**
     * Create Command from line which contains a command.
     *
     * @param line line which contains a command
     * @return Command created from specified line
     * @throws WrongLineFormatException line is not in required format
     * @see <a href="https://gitlab.com/munijava/pb162-2015-hw03-filemanager#job-file-syntax">Job file syntax</a>
     */
    private Command createCommand(String line, File logFile) throws WrongLineFormatException {
        if (line == null || logFile == null)
            throw new NullPointerException();

        //required format: <command>;<file extension>;<optional argument>
        String[] tokens = line.split(";");
        Operator op = convertOperator(tokens[0]);
        if (tokens.length < 2 || tokens.length > 3 || (!op.equals(Operator.DEL) && tokens.length != 3))
            throw new WrongLineFormatException("Wrong format of line in jobPath file");

        String fileExtension = tokens[1];
        Command cmd;
        if (tokens.length == 3) {
            String argument = tokens[2]; //optional argument
            cmd = new Command(op, fileExtension, argument, logFile);
        } else {
            cmd = new Command(op, fileExtension, logFile);
        }
        return cmd;
    }

    /**
     * Process first line of jobFile given in firstLine.
     *
     * @param firstLine first line of jobFile
     * @return path to directory to clean up
     * @throws WrongLineFormatException first line is not in required format
     * @throws WrongFileFormatException file is empty (do not contain anything)
     * @see <a href="https://gitlab.com/munijava/pb162-2015-hw03-filemanager#job-file-syntax">Job file syntax</a>
     */
    private String processFirstLine(String firstLine) throws WrongLineFormatException, WrongFileFormatException {
        //required format: root;<path>     <path> - path to folder which should be cleaned up
        if (firstLine == null)
            throw new WrongFileFormatException("Wrong format of jobPath file - file is empty");

        String[] path = firstLine.split(";");
        if (path.length != 2 || !path[0].equals("root")) //first argument must be "root"
            throw new WrongLineFormatException("Wrong format of first line in jobPath file - first line is not in required format");
        return path[1];
    }

    /**
     * Convert operator given as String to Operator.
     *
     * @param op operator as String to convert
     * @return operator as enum Operator value
     * @throws WrongLineFormatException given operation is not supported
     */
    private Operator convertOperator(String op) throws WrongLineFormatException {
        if (op == null)
            throw new NullPointerException();

        switch (op) {
            case "DEL":
                return Operator.DEL;
            case "MV":
                return Operator.MV;
            case "CP":
                return Operator.CP;
            default:
                throw new WrongLineFormatException("Wrong format of line in jobPath file - unsupported operation");
        }
    }

    /**
     * Recursively get through specified folder
     *
     * @param root folder to be processed
     * @param cmd  command to process on given folder
     * @throws IOException if command process failed
     */
    private void walkRecursively(File root, Command cmd) throws IOException {
        if (root == null || cmd == null)
            throw new NullPointerException();

        File[] content = root.listFiles(); //dir content
        if (content == null)
            return; //everything (all files and subfolders) processed
        for (File f : content) {
            if (f.isDirectory()) {
                walkRecursively(f, cmd); //process subfolder
            } else {
                String fileExt = FilenameUtils.getExtension(f.getPath());
                if (fileExt.equals(cmd.getExt())) {
                    fileProcess(f, cmd); //vyhazuje vyjimku
                } else
                    continue;
            }
        }
    }

    /**
     * Process command on specified file
     *
     * @param f   file to process
     * @param cmd command to process on given file
     * @throws IOException if operation processed by control failed
     */
    private void fileProcess(File f, Command cmd) throws IOException {
        if (f == null || cmd == null)
            throw new NullPointerException();

        Path source = f.toPath();
        RemoteControl control = new RemoteControl();
        switch (cmd.getOp()) {
            case MV: {
                control.setOp(new MoveCommand(cmd));
                break;
            }
            case DEL: {
                control.setOp(new DeleteCommand(cmd));
                break;
            }

            case CP: {
                control.setOp(new CopyCommand(cmd));
                break;
            }
        }
        control.processOperation(source);
    }
}
