package server.view.rmi;

import server.controller.Prompt;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Provides the methods to be used by the server in RMI.
 * @author Niccolò Giuliani
 */
public interface ServerUsable extends Remote{

     /**
      * Requests a specific input from the player.
      * @param input The type of prompt the player has to answer to.
      */
     void requestInput(Prompt input) throws RemoteException;

     /**
      * Shows the specified output to the player.
      * @param output A JSON formatted string.
      */
     void showOutput(String output) throws RemoteException;
}
