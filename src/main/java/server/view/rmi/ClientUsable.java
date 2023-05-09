package server.view.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Provides the methods to be used by the ClientTUI in RMI.
 * @author Niccolò Giuliani
 */
public interface ClientUsable extends Remote{

     /**
      * Checks whether this ClientHandler has a connection.
      * @return True if the ClientHandler has not connected to a client yet.
      */
     boolean isAvailable() throws RemoteException;

     /**
      * Sets the ClientHandler to not available and gets the RMI object with the given name.
      * @param clientName The name of the client's NetworkHandler in the RMI registry.
      */
     void setAvailable(String clientName) throws RemoteException;

     /**
      * Sends the player's input to the server.
      * @param input The input from the player to the server.
      */
     void sendInput(String input) throws RemoteException;
}
