package es.noobcraft.oneblock.api.exceptions;

public class IslandFullException extends Exception {

    public IslandFullException() {
        super("This island has reached the max people");
    }

    public IslandFullException(String message) {
        super(message);
    }

    public IslandFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public IslandFullException(Throwable cause) {
        super(cause);
    }
}
