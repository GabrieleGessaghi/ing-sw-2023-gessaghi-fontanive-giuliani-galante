package server.controller.exceptions;

/**
 * Gets thrown when there is an issue creating a json message.
 */
public class JsonCreationException extends Exception{
    public JsonCreationException(String errorMessage) {
        super(errorMessage);
    }
}
