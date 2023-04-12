package controller.exceptions;

public class IllegalColumnSelectionException extends Exception{
    public IllegalColumnSelectionException(String errorMessage) {
        super(errorMessage);
    }
}
