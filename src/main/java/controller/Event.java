package controller;

/**
 * Holds information for a data update.
 * @author Giorgio Massimo Fontanive
 */
public class Event {
    private final String jsonMessage;

    public Event(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }
}
