package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.Operation;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author: Jana Zahradnickova,  UCO 433598
 * @version: 21. 12. 2015
 */
public class RemoteControl {
    private Operation operation;

    public RemoteControl() {
    }

    public void setOp(Operation operation) {
        if (operation == null)
            throw new NullPointerException();

        this.operation = operation;
    }

    /**
     * Execute operation on given path. Command is executed if path matches command's pattern, nothing happens
     * otherwise
     *
     * @param source path to file on filesystem
     * @throws IOException if operation execution failed
     */
    public void processOperation(Path source) throws IOException {
        if (source == null)
            throw new NullPointerException();

        operation.execute(source);
    }
}
