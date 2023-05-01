package server.model.exceptions;

/**
 * Thrown when token insertion inside the shelf is not possible due to no column space
 * @author Gabriele Gessaghi
 */
public class IllegalColumnException extends Exception{
    public IllegalColumnException(String errorMessage) {super(errorMessage);}
}
