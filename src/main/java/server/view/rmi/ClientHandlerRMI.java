package server.view.rmi;

import client.network.NetworkHandlerRMI;

import server.controller.Prompt;
import server.controller.observer.Event;
import server.view.ClientHandler;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class for handling the RMI clients.
 * @author Niccolò Giuliani
 */
public class ClientHandlerRMI extends ClientHandler implements ClientUsable {
    private boolean available;
    NetworkHandlerRMI client;
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
            Registry registry = LocateRegistry.getRegistry();
            client = (NetworkHandlerRMI) registry.lookup(clientName);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Event event) {
        client.showOutput(event.jsonMessage());
    }

    @Override
    public boolean isAvailable(){
        return this.available;
    }

    @Override
    public synchronized void setAvailable(String clientName) {
        System.out.println("Connected to client"); //
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
        client.showOutput(jsonMessage);
    }

    @Override
    public void requestInput(Prompt prompt) {
        client.requestInput(prompt);
    }
}
