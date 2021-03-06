package cz.muni.fi.pb162.hw03.impl;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: Jana Zahradnickova,  UCO 433598
 * @version: 20. 12. 2015
 */
public class Command { //reciver
    private Operator op;
    private String ext;
    private Path dest;
    private File logFile;

    public Command(Operator op, String ext, File logFile) {
        if (op == null || ext == null || logFile == null)
            throw new NullPointerException();

        if (!op.equals(Operator.DEL))
            throw new IllegalArgumentException("argument is missing.");

        this.op = op;
        this.ext = ext;
        this.logFile = logFile;
    }

    public Command(Operator op, String ext, String arg, File logFile) {
        if (op == null || ext == null || arg == null || logFile == null)
            throw new NullPointerException();

        this.op = op;
        this.ext = ext;
        this.dest = Paths.get(arg);
        this.logFile = logFile;
    }

    public Operator getOp() {
        return op;
    }

    public String getExt() {
        return ext;
    }

    public Path getDest() {
        return dest;
    }

    public File getLogFile() {
        return logFile;
    }

    /**
     * Delete file given in source when source matches command's pattern
     *
     * @param source file to delete
     * @return true if pattern matches path and command was executed, false if pattern did not match
     * @throws IOException if move failed or event can not be logged
     */
    public boolean delete(Path source) throws IOException {
        if (source == null)
            throw new NullPointerException();

        String fileExt = FilenameUtils.getExtension(source.toString());
        if (fileExt.equals(this.getExt())) {
            Files.delete(source); //ex
            logEvent(source.toString()); //ex
            return true;
        } else
            return false;
    }

    /**
     * Copy file given in source when source matches command's pattern
     *
     * @param source file to copy
     * @return true if pattern matches path and command was executed, false if pattern did not match
     * @throws IOException if move failed or event can not be logged
     */
    public boolean copy(Path source) throws IOException {
        if (source == null)
            throw new NullPointerException();

        String fileExt = FilenameUtils.getExtension(source.toString());
        if (fileExt.equals(this.getExt())) {
            String dest = prepareDestination(source);

            Files.copy(source, Paths.get(dest));
            logEvent(source.toString(), dest);
            return true;
        } else
            return false;

    }

    /**
     * Move file given in source when source matches command's pattern
     *
     * @param source file to move
     * @return true if pattern matches path and command was executed, false if pattern did not match
     * @throws IOException if move failed or event can not be logged
     */
    public boolean move(Path source) throws IOException {
        if (source == null)
            throw new NullPointerException();

        String fileExt = FilenameUtils.getExtension(source.toString());
        if (fileExt.equals(this.getExt())) {

            String dest = prepareDestination(source);

            Files.move(source, Paths.get(dest));
            logEvent(source.toString(), dest);
            return true;
        } else
            return false;
    }

    /**
     * Create path to given file in destination folder
     *
     * @param source path to file
     * @return path to file in destination folder
     */
    private String prepareDestination(Path source) {
        if (source == null)
            throw new NullPointerException();

        createDestDir(Paths.get(this.getDest().toString()));
        String dest = createDest(this.getDest(), source);
        if (new File(dest).exists()) {
            dest = getUniqueFilename(dest); //soubor jiz v cilove slozce existuje, nutno prejmenovat

        }
        return dest;
    }

    /**
     * Create path for given file in destination directory
     *
     * @param destDir destination directory
     * @param source  path to file
     * @return path to file in destination directory as string
     */
    private String createDest(Path destDir, Path source) {
        if (destDir == null || source == null)
            throw new NullPointerException();

        File f = source.toFile();
        String dest = destDir.toString();
        String baseName = FilenameUtils.getBaseName(f.getName());
        String extension = FilenameUtils.getExtension(f.getName());
        dest = dest + File.separator + baseName + "." + extension;
        return dest;
    }

    /**
     * Create destination folder
     *
     * @param dest path to destination folder
     */
    private void createDestDir(Path dest) {
        if (dest == null)
            throw new NullPointerException();
        if (!dest.toFile().exists())
            new File(dest.toString()).mkdirs();
    }

    /**
     * Generate unique name of given file using 3-digit counter.
     *
     * @param dest path to file in destination folder
     * @return unique file name as path to file in destination folder
     * @see <a href="https://gitlab.com/munijava/pb162-2015-hw03-filemanager#multiple-files-with-same-name">Multiple files with same name</a>
     */
    private String getUniqueFilename(String dest) {
        if (dest == null)
            throw new NullPointerException();

        File file = new File(dest);
        String baseName = FilenameUtils.getBaseName(file.getName());
        String extension = FilenameUtils.getExtension(file.getName());
        int i = 0;
        while (file.exists()) {
            i++;
            String counter = Integer.toString(i);
            while (counter.length() < 3) {
                counter = "0" + counter;
            }
            file = new File(file.getParent(), baseName + "_" + counter + "." + extension);
        }
        return file.toString();
    }

    /**
     * Log event in specified format.
     *
     * @param source path to file in source folder
     * @param dest   path to file in destination folder
     * @throws IOException IOException if command can not be logged
     * @see <a href="https://gitlab.com/munijava/pb162-2015-hw03-filemanager#log-file">Log file</a>
     */
    private void logEvent(String source, String dest) throws IOException {
        if (source == null || (!this.getOp().equals(Operator.DEL) && dest == null))
            throw new NullPointerException();

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(this.getLogFile(), true)))) {
            if (this.getOp().equals(Operator.DEL))
                out.println(getOp().toString() + ";" + source);
            else
                out.println(this.getOp().toString() + ";" + source + ";" + dest);
        }
    }

    /**
     * Log event in specified format.
     *
     * @see <a href="https://gitlab.com/munijava/pb162-2015-hw03-filemanager#log-file">Log file</a>
     * @param source path to file in source folder
     * @throws IOException IOException if command can not be logged
     */
    private void logEvent(String source) throws IOException {
        if (source == null)
            throw new NullPointerException();

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(this.getLogFile(), true)))) {
            if (this.getOp().equals(Operator.DEL))
                out.println(getOp().toString() + ";" + source);
            else
                throw new IllegalArgumentException();
        }
    }
}
