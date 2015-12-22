package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.Operation;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author: Jana Zahradnickova,  UCO 433598
 * @version: 20. 12. 2015
 */
public class CopyCommand implements Operation { //Concrete Command
    private Command cmd;

    public CopyCommand(Command cmd) {
        if (cmd == null)
            throw new NullPointerException();

        this.cmd = cmd;
    }

    /**
     * Execute operation on given path. Command is executed if path matches command's pattern, nothing happens
     * otherwise.
     *
     * @param source path to file on filesystem
     * @return true if pattern matches path and command was executed, false if pattern didn't match.
     * @throws IOException if command execution failed
     */
    @Override
    public boolean execute(Path source) throws IOException {
        if (source == null)
            throw new NullPointerException();

        return cmd.copy(source);
    }

}
