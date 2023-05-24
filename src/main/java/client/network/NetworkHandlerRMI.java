package client.network;

import com.google.gson.JsonObject;
import server.view.rmi.ServerUsable;
import server.controller.Prompt;
import server.view.rmi.ClientUsable;

import java.rmi.NoSuchObjectException;
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
            server.sendInput(input);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method to ask to TUI to show the output
     * author Niccolò Giuliani
     * @param output output to show
     */
    @Override
    public synchronized void showOutput(String output) {
        client.showOutput(output);
        isMessageAvailable = true;
        this.notifyAll();
    }

    public void run() {
        int i;
        i = 0;
        isMessageAvailable = false;
        try {
           Registry registry = LocateRegistry.getRegistry(host, SERVER_PORT + 1);
           do {
               server = (ClientUsable) registry.lookup("ServerRMI" + i);
               i++;
           } while(!server.isAvailable());

           System.out.println("Found server");

           ServerUsable stub = (ServerUsable) UnicastRemoteObject.exportObject(this,0);
           registry.rebind("ClientRMI"+ (i - 1),stub);
           server.setAvailable("ClientRMI"+ (i - 1));

           while (true) {
               while (!isMessageAvailable)
                   synchronized (this) {
                       this.wait();
                   }
               isMessageAvailable = false;
           }
        } catch(Exception e) {
            e.printStackTrace();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("connectionError", true);
            client.showOutput(jsonObject.toString());
        }
    }
}
