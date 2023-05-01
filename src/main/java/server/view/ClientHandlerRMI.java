package server.view;

import client.NetworkHandlerRMI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Prompt;
import server.controller.observer.Event;
import server.controller.observer.Observer;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * class for handle the RMI Clients
 * @author Niccolò Giuliani
 */
public class ClientHandlerRMI extends ClientHandler implements ClientHandlerRMIInterface{

    private boolean available;

    private Prompt lastRequest;
    private boolean isThereRequest;
    private final ArrayList<Observer> observers;
    NetworkHandlerRMI client;
    private String clientName;
    public ClientHandlerRMI(int index){
        this.isThereRequest = true;
        this.index = index;
        this.lastRequest = Prompt.NICKNAME;
        this.available = true;
        this.client = null;
        this.observers = new ArrayList<>();
    }

    /**
     * method to update the observers
     * @author Niccolò Giuliani
     * @param event The event to be sent to the observers.
     */
    @Override
    public void updateObservers(Event event) {
        String jsonMessage = event.getJsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
        jsonObject.addProperty("clientIndex", index);
        Event indexedEvent = new Event(jsonObject.toString());
        for(Observer o : observers)
            o.update(indexedEvent);
    }

    @Override
    public void update(Event event) {
        client.showOutput(event.getJsonMessage());
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }


    @Override
    public void requestInput(Prompt prompt) {
        this.lastRequest = prompt;
        this.isThereRequest = true;
    }

    @Override
    public void run() {

        try {
            Registry registry = LocateRegistry.getRegistry();
            client = (NetworkHandlerRMI) registry.lookup(clientName);
        }catch(Exception e){
            System.out.println("[System] Client failed: " + e);
        }
        while(true){
            if(isThereRequest) {
                client.receiveInput(lastRequest);
                isThereRequest = false;
            }


        }
    }

    public void showOutput(String jsonMessage) {
        client.showOutput(jsonMessage);
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
     * method to send the input to the class Server
     * @author Niccolò Giuliani
     * @param input input of the Client
     */
    public void sendInput(String input){
        updateObservers(new Event(input));
    }

}
