package server.view.rmi;

import server.controller.Prompt;
import server.controller.observer.Event;
import server.view.ClientHandler;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
        this.available = true;
        this.client = null;
    }

    /**
     * Looks for the client's NetworkHandler in the registry and starts sending requests.
     * @author Niccolò Giuliani
     */
    @Override
    public synchronized void run() {
        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_PORT + 1);
            client = (ServerUsable) registry.lookup(clientName);
            available = false;
            this.notifyAll();
        } catch(Exception e) {
            e.printStackTrace();
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
        available = true;
    }

    @Override
    public void update(Event event) {
        try {
            client.showOutput(event.jsonMessage());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
        try {
            client.showOutput(jsonMessage);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestInput(Prompt prompt) {
        try {
            client.requestInput(prompt);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
