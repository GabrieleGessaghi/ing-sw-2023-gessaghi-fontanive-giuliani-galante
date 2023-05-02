package server.view;

import java.rmi.Remote;

public interface ClientHandlerRMIInterface extends Remote {
     boolean isAvailable();
     void setAvailable(String clientName);
     void sendInput(String input);
}
