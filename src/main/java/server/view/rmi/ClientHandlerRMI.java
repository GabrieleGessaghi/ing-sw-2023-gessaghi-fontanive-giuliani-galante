package server.view.rmi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Prompt;
import server.controller.observer.Event;
import server.view.ClientHandler;


import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

/**
 * Class for handling the RMI clients.
 * @author Niccolò Giuliani
 */
public class ClientHandlerRMI extends ClientHandler implements ClientUsable {
    private boolean available;
    ServerUsable client;
    private String clientName;

    /**
     * Class constructor.
     */
    public ClientHandlerRMI(){
        available = true;
        client = null;
        clientName = "";
    }

    /**
     * Looks for the client's NetworkHandler in the registry and starts sending requests.
     * @author Niccolò Giuliani
     */
    @Override
    public synchronized void run() {
        super.run();
        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_PORT + 1);
            client = (ServerUsable) registry.lookup(clientName);
            available = false;
            this.notifyAll();
        } catch(Exception e) {
            disconnect();
        }
    }

    /**
     *
     * @author Giorgio Massimo Fontanive
     */
    public synchronized void waitForConnection() throws InterruptedException {
        while (available) {
            this.wait();
        }
        if (client == null)
            available = true;
    }

    @Override
    public boolean isAvailable(){
        return available;
    }

    @Override
    public synchronized void setAvailable(String clientName) {
        this.clientName = clientName;
        available = false;
        this.notifyAll();
    }

    @Override
    public void sendInput(String input){
        updateObservers(new Event(input));
    }

    @Override
    public void sendOutput(String jsonMessage) {
        if (isConnected)
            new Thread(() -> {
                try {
                    if (client != null)
                        client.showOutput(jsonMessage);
                } catch (RemoteException e) {
                    disconnect();
                }
            }).start();
    }

    @Override
    public void requestInput(Prompt prompt) {
        if (isConnected)
            new Thread(() -> {
                try {
                    if (client != null)
                        client.requestInput(prompt);
                } catch (RemoteException e) {
                    disconnect();
                }
            }).start();
    }
}
