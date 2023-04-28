package server.controller.observer;

/**
 * Lets observable objects send an event to others implementing this.
 * @author Giorgio Massimo Fontanive
 */
public interface Observer {

    /**
     * Called by observable objects, allows for messages to be sent.
     * @param event The message to be sent to this observer.
     */
    void update(Event event);
}
