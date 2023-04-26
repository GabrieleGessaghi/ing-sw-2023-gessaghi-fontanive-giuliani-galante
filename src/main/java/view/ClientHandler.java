package view;

import controller.observer.Observable;
import controller.observer.Observer;
import controller.observer.Event;
public abstract class ClientHandler extends Thread implements Observer, Observable {
    private String playerNickname;

    public abstract void update(Event event);

    public abstract void registerObserver(Observer observer);

    public abstract  void notifyObservers(Event event);

    public abstract  void requestinput(Promt promt);

    public abstract  void run();

    protected abstract void showOutput(String jsonMessage);


}
