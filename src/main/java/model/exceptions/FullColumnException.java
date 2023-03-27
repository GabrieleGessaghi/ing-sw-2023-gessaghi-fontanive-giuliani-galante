package model.exceptions;

/**
 * Thrown when token insertion inside the shelf is not possible due to no column space
 * @author Gabriele Gessaghi
 */
public class FullColumnException extends Exception{
    public FullColumnException (String errorMessage) {super(errorMessage);}
}
