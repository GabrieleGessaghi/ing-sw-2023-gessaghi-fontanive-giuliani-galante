package view;

import controller.observer.Observable;
import controller.observer.Observer;
import controller.observer.Event;

/**
 * Handler of the Client
 * @author Niccolò Giuliani
 */
public abstract class ClientHandler extends Thread implements Observer, Observable {
    private String playerNickname;

    public abstract void update(Event event);

    public abstract void registerObserver(Observer observer);



    /**
     * method to request an input to the Client
     * @author Niccolò Giuliani
     * @param promt type of request
     */
    public abstract  void requestInput(Promt promt);

    public abstract  void run();

    /**
     * method to show the output
     * @author Niccolò Giuliani
     * @param jsonMessage String to show
     */
    protected abstract void showOutput(String jsonMessage);


}
