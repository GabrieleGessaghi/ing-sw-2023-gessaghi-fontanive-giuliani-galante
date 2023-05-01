package client;

import java.rmi.Remote;

public interface NetworkHandlerRMIInterface extends Remote {
    public void receiveInput(String Input);
    public void sendInput(String Input);

}
