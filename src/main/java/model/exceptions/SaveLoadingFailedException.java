package model.exceptions;

public class SaveLoadingFailedException extends Exception {
    public SaveLoadingFailedException(String errorMessage) {
        super(errorMessage);
    }
}
