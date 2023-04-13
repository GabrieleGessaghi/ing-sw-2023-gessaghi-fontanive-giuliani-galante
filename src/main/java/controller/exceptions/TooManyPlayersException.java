package controller.exceptions;

public class TooManyPlayersException extends Exception{

    public TooManyPlayersException (String errorMessage) {
        super(errorMessage);
    }
}
