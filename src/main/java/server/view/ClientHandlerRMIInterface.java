package server.view;

import server.controller.Prompt;
import server.controller.observer.Event;
import server.controller.observer.Observer;

import java.rmi.Remote;

public interface ClientHandlerRMIInterface extends Remote {
    public void updateObservers(Event event);
    public void update(Event event);
    public void requestInput(Prompt prompt);
    public void showOutput(String jsonMessage);
    public boolean isAvailable();
    public void setAvailable(String nickname);

}
