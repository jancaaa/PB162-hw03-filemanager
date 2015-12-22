package cz.muni.fi.pb162.hw03.impl;

/**
 * @author: Jana Zahradnickova,  UCO 433598
 * @version: 18. 12. 2015
 */
public class WrongLineFormatException extends Exception {
    public WrongLineFormatException() {
    }

    public WrongLineFormatException(String message) {
        super(message);
    }

    public WrongLineFormatException(Throwable cause) {
        super(cause);
    }

    public WrongLineFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
