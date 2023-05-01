package client;

import server.view.ClientHandlerRMIInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NetworkHandlerRMI extends Thread implements NetworkHandlerRMIInterface {
   private Client client;
   public void NetworkHandlerRMI(Client client){
       this.client = client;
   }

    @Override
    public void receiveInput(String input) {
    }

    @Override
    public void sendInput(String Input) {

    }

    public void run(){
    int i;
    i = 0;
       try {
           Registry registry = LocateRegistry.getRegistry();
           ClientHandlerRMIInterface server;
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
