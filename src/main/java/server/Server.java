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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

//TODO: Warn player of nickname change

//When a new client is being added to controller
//  check if nickname was disconnected, set its connection status and id


/**
 * Accepts new connections and starts the game controller.
 * @author Giorgio Massimo Fontanive
 */
public class Server {
    public static Map<String, Integer> disconnectedClients;
    private static Controller controller;
    private final AtomicInteger connectionsCounter;

    /**
     * Class constructor.
     */
    public Server() {
        connectionsCounter = new AtomicInteger(0);
    }

    /**
     * Loads the configuration file, starts the controller and the connection acceptance methods.
     */
    public static void main(String[] args) throws IOException {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        disconnectedClients = new HashMap<>();
        controller = new Controller();
        Server server = new Server();
        new Thread(controller).start();
        new Thread(server::acceptConnectionsTCP).start();
        new Thread(server::acceptConnectionsRMI).start();
    }

    /**
     * Keeps accepting new TCP connections and creates a new client handler for each.
     * @author Giorgio Massimo Fontanive
     */
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
                ClientHandlerTCP clientHandler = new ClientHandlerTCP(dataInputStream, dataOutputStream);
                clientHandler.index = connectionsCounter.get();
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
                System.out.println("TCP connection issue.");
            }
            connectionsCounter.incrementAndGet();
        }
    }

    /**
     * Keeps accepting new RMI connections by binding a new remote clientHandler every time a new connection is established.
     * @author Giorgio Massimo Fontanive
     */
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
        clientHandlersRMI.get(0).index = connectionsCounter.get();
        while (true) {
            try {
                ClientUsable stub = (ClientUsable) UnicastRemoteObject.exportObject(clientHandlersRMI.get(connectionsIndex),0);
                registry.rebind("ServerRMI" + connectionsIndex, stub);
                clientHandlersRMI.get(connectionsIndex).waitForConnection();

                new Thread(clientHandlersRMI.get(connectionsIndex)).start();
                clientHandlersRMI.get(connectionsIndex).waitForConnection();

                controller.addClient(clientHandlersRMI.get(connectionsIndex));
                clientHandlersRMI.add(new ClientHandlerRMI());
                clientHandlersRMI.get(connectionsIndex).index = connectionsCounter.get();
                connectionsIndex++;
            } catch (RemoteException | InterruptedException ex) {
                System.out.println("RMI connection issue.");
            }
            connectionsCounter.incrementAndGet();
        }
    }
}
