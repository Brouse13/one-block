package es.noobcraft.oneblock.api.exceptions;

public class FlagException extends Exception {

    public FlagException() {
        super("Error finding the flag");
    }

    public FlagException(String message) {
        super(message);
    }

    public FlagException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlagException(Throwable cause) {
        super(cause);
    }
}
