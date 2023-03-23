package model.exceptions;

/**
 * Thrown when a player tries to play an illegal move.
 * @author Giorgio Massimo Fontanives
 */
public class IllegalMoveException extends Exception {
    public IllegalMoveException(String errorMessage) {
        super(errorMessage);
    }
}
