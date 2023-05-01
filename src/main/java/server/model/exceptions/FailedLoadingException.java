package server.model.exceptions;

public class FailedLoadingException extends Exception {
    public FailedLoadingException(String errorMessage) {
        super(errorMessage);
    }
}
