package server.view.rmi;

import client.network.NetworkHandlerRMI;

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
    public void run() {
        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_PORT + 1);
            client = (ServerUsable) registry.lookup(clientName);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
    public void setAvailable(String clientName) {
        this.clientName = clientName;
        available = false;
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
