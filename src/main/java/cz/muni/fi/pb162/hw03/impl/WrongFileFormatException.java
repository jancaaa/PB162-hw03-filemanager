package cz.muni.fi.pb162.hw03.impl;

/**
 * @author: Jana Zahradnickova,  UCO 433598
 * @version: 18. 12. 2015
 */
public class WrongFileFormatException extends Exception {
    public WrongFileFormatException() {
    }

    public WrongFileFormatException(String message) {
        super(message);
    }

    public WrongFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongFileFormatException(Throwable cause) {
        super(cause);
    }
}
