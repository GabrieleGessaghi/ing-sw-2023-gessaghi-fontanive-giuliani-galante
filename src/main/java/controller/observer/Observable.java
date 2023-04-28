package controller.observer;

/**
 * Sends an event to every object subscribed to this.
 * @author Giorgio Massimo Fontanive
 */
public interface Observable {

    /**
     * Registers a new Observer for this object.
     * @param observer The observer to be registered.
     */
    void registerObserver(Observer observer);

    /**
     * Updates every observer via their update function.
     * @param event The event to be sent to the observers.
     */
    void updateObservers(Event event);
}
