package server.view.rmi;

import client.network.NetworkHandlerRMI;

import server.controller.observer.Event;
import server.view.ClientHandler;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * class for handle the RMI Clients
 * @author Niccolò Giuliani
 */
public class ClientHandlerRMI extends ClientHandler implements ClientUsable {
    private boolean available;
    NetworkHandlerRMI client;
    private String clientName;

    public ClientHandlerRMI(int index){
        super(index);
        this.available = true;
        this.client = null;
    }

    @Override
    public void run() {
        try {
            Registry registry = LocateRegistry.getRegistry();
            client = (NetworkHandlerRMI) registry.lookup(clientName);
        } catch(Exception e) {
            System.out.println("[System] Client failed: " + e);
        }
        while (true) {
            if(isThereRequest) {
                client.receiveInput(lastRequest);
                isThereRequest = false;
            }
        }
    }

    @Override
    public void update(Event event) {
        client.showOutput(event.getJsonMessage());
    }

    /**
     * method to know if the ClientHandler is available
     * @author Niccolò Giuliani
     * @return this.available
     */
    @Override
    public boolean isAvailable(){
        return this.available;
    }

    /**
     * method to set the network-handler's name and put the client-handler not available
     * @author Niccolò Giuliani
     * @param clientName name of the network-handler
     */
    @Override
    public void setAvailable(String clientName) {
        this.clientName = clientName;
        available = false;
    }

    /**
     * method to send the input to the controller
     * @author Niccolò Giuliani
     * @param input input of the Client
     */
    @Override
    public void sendInput(String input){
        updateObservers(new Event(input));
    }

    @Override
    public void showOutput(String jsonMessage) {
        client.showOutput(jsonMessage);
    }
}
