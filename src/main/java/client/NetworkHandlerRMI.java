package client;

import server.controller.Prompt;
import server.view.ClientHandlerRMIInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NetworkHandlerRMI extends Thread implements NetworkHandlerRMIInterface {
   private final Client client;
   ClientHandlerRMIInterface server;
   public  NetworkHandlerRMI(Client client){
       this.server = null;
       this.client = client;
   }

    @Override
    public void receiveInput(Prompt input) {
       client.requestInput(input);
    }


    public void sendInput(String input) {
        server.sendInput(input);
    }

    @Override
    public void showOutput(String output) {
        client.showOutput(output);
    }

    public void run(){
    int i;
    i = 0;
       try {
           Registry registry = LocateRegistry.getRegistry();

           do {
                server = (ClientHandlerRMIInterface) registry.lookup("ServerRMI" + i);
               i++;
           }while(!server.isAvailable());
           registry.rebind("ClientRmi"+i ,this);
           server.setAvailable("ClientRMI"+i);

       }catch(Exception e){
           System.out.println("[System] Server failed: " + e);
       }
    }
}
