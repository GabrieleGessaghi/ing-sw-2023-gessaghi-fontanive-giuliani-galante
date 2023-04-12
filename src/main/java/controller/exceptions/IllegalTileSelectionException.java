package controller.exceptions;

public class IllegalTileSelectionException extends Exception{
    public IllegalTileSelectionException(String errorMessage) {
        super(errorMessage);
    }
}
