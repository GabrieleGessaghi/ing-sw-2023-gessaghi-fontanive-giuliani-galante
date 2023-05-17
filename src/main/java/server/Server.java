package server;

import server.controller.Controller;
import server.controller.utilities.ConfigLoader;
import server.view.ClientHandler;
import server.view.rmi.ClientHandlerRMI;
import server.view.rmi.ClientUsable;
import server.view.tcp.ClientHandlerTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

/**
 * Accepts new connections and starts the game controller.
 * @author Giorgio Massimo Fontanive
 */
public class Server {
    private static Controller controller;

    public static void main(String[] args) throws IOException {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        controller = new Controller();
        Server server = new Server();
        new Thread(controller).start();
        new Thread(server::acceptConnectionsTCP).start();
        new Thread(server::acceptConnectionsRMI).start();
    }

    public void acceptConnectionsTCP() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                ClientHandlerTCP clientHandler = new ClientHandlerTCP(socket, dataInputStream, dataOutputStream);
                new Thread(clientHandler).start();
                controller.addClient(clientHandler);
            } catch (IOException e) {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                System.out.println("One TCP connection lost.");
            }
        }
    }

    public void acceptConnectionsRMI() {
        int connectionsIndex = 0;
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(SERVER_PORT + 1);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        List<ClientHandlerRMI> clientHandlersRMI = new ArrayList<>();
        clientHandlersRMI.add(new ClientHandlerRMI());
        while (true) {
            try {
                ClientUsable stub = (ClientUsable) UnicastRemoteObject.exportObject(clientHandlersRMI.get(connectionsIndex),0);
                registry.rebind("ServerRMI" + connectionsIndex, stub);
                clientHandlersRMI.get(connectionsIndex).waitForConnection();

                new Thread(clientHandlersRMI.get(connectionsIndex)).start();
                clientHandlersRMI.get(connectionsIndex).waitForConnection();

                controller.addClient(clientHandlersRMI.get(connectionsIndex));
                clientHandlersRMI.add(new ClientHandlerRMI());
                connectionsIndex++;
            } catch (RemoteException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            //TODO: Check if any of the RMI connections are closed and unbind them
        }
    }
}
