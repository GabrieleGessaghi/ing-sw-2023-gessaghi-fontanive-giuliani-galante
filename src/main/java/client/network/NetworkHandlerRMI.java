package client.network;

import client.Client;
import server.view.rmi.ServerUsable;
import server.controller.Prompt;
import server.view.rmi.ClientUsable;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

public class NetworkHandlerRMI extends NetworkHandler implements ServerUsable {
   ClientUsable server;

   public  NetworkHandlerRMI(Client client, String host){
       super(client, host);
   }

    /**
     * method for receiving the input from the server and to send to Client
     * @author Niccolò Giuliani
     * @param input input to Send to the client
     */
    @Override
    public void requestInput(Prompt input) {
       client.requestInput(input);
    }

    /**
     * method to send an input to the Sever
     * @author Niccolò Giuliani
     * @param input input to send to the server
     */
    public void sendInput(String input) {
        server.sendInput(input);
    }

    /**
     * method to ask to Client to show the output
     * author Niccolò Giuliani
     * @param output output to show
     */
    @Override
    public void showOutput(String output) {
        client.showOutput(output);
    }

    public void run(){
    int i;
    i = 0;
       try {
           Registry registry = LocateRegistry.getRegistry(host, SERVER_PORT);
           do {
               server = (ClientUsable) registry.lookup("ServerRMI" + i);
               i++;
           } while(!server.isAvailable());
           registry.rebind("ClientRmi"+ (i - 1),this);
           server.setAvailable("ClientRMI"+ (i - 1));
       }catch(Exception e){
           System.out.println("[System] Server failed: " + e);
       }
    }
}
