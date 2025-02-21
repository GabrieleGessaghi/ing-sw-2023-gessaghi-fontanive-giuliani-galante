package client.network;

import server.view.rmi.ServerUsable;
import server.controller.Prompt;
import server.view.rmi.ClientUsable;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

public class NetworkHandlerRMI extends NetworkHandler implements ServerUsable {
    private ClientUsable server;
    private boolean isMessageAvailable;

    /**
     * method for receiving the input from the server and to send to TUI
     * @author Niccolò Giuliani
     * @param input input to Send to the client
     */
    @Override
    public synchronized void requestInput(Prompt input) {
        client.requestInput(input);
        isMessageAvailable = true;
        this.notifyAll();
    }

    /**
     * method to send an input to the Sever
     * @author Niccolò Giuliani
     * @param input input to send to the server
     */
    public void sendInput(String input) {
        try {
            if (server != null)
                server.sendInput(input);
        } catch (RemoteException e) {
            disconnect();
        }
    }

    /**
     * method to ask TUI to show the output
     * author Niccolò Giuliani
     * @param output output to show
     */
    @Override
    public synchronized void showOutput(String output) {
        client.showOutput(output);
        isMessageAvailable = true;
        this.notifyAll();
    }

    /**
     * Connects to the server and waits for its messages.
     * @author Giorgio massimo Fontanive
     */
    public void run() {
        super.run();
        int i;
        i = 0;
        isMessageAvailable = false;
        try {
           Registry registry = LocateRegistry.getRegistry(host, SERVER_PORT + 1);
           do {
               server = (ClientUsable) registry.lookup("ServerRMI" + i);
               i++;
           } while(!server.isAvailable());

           ServerUsable stub = (ServerUsable) UnicastRemoteObject.exportObject(this,0);
           registry.rebind("ClientRMI"+ (i - 1),stub);
           server.setAvailable("ClientRMI"+ (i - 1));

           while (isConnected) {
               while (!isMessageAvailable)
                   synchronized (this) {
                       this.wait();
                   }
               isMessageAvailable = false;
           }
        } catch(Exception e) {
            disconnect();
        }
    }
}
