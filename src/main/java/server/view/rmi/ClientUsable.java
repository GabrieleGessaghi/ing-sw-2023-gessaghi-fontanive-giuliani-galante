package server.view.rmi;

import java.rmi.Remote;

public interface ClientUsable extends Remote {
     boolean isAvailable();
     void setAvailable(String clientName);
     void sendInput(String input);
}
