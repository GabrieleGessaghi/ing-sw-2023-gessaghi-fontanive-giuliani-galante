package controller.observer;

/**
 * Sends an event to every object subscribed to this.
 * @author Giorgio Massimo Fontanive
 */
public interface Observable {
    void registerObserver(Observer observer);
    void updateObservers(Event event);
}
