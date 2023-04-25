package controller.observer;

/**
 * Lets observable objects send an event to others implementing this.
 * @author Giorgio Massimo Fontanive
 */
public interface Observer {
    void update(Event event);
}
