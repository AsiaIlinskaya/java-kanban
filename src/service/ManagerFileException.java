package service;

public class ManagerFileException extends RuntimeException {

    public ManagerFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerFileException(String message) {
        super(message);
    }

}
