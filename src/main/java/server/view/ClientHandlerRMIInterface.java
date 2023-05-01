package server.view;


import server.controller.observer.Event;


import java.rmi.Remote;

public interface ClientHandlerRMIInterface extends Remote {
     void updateObservers(Event event);
     void update(Event event);
     boolean isAvailable();
     void setAvailable(String clientName);
     void sendInput(String input);
}
