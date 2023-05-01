package server.view;

import server.controller.Prompt;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.observer.Event;

/**
 * Handler of the Client
 * @author Niccolò Giuliani
 */
public abstract class ClientHandler implements Observer, Observable, Runnable {
    protected int index;

    public abstract void update(Event event);

    public abstract void registerObserver(Observer observer);



    /**
     * method to request an input to the Client
     * @author Niccolò Giuliani
     * @param prompt type of request
     */
    public abstract  void requestInput(Prompt prompt);

    public abstract  void run();

    /**
     * method to show the output
     * @author Niccolò Giuliani
     * @param jsonMessage String to show
     */
    public abstract void showOutput(String jsonMessage);

    public int getIndex() {
        return index;
    }
}
