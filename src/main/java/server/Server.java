package server;

import server.controller.Controller;
import server.controller.utilities.ConfigLoader;
import server.view.rmi.ClientHandlerRMI;
import server.view.tcp.ClientHandlerTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

/**
 * Accepts new connections and starts the game controller.
 * @author Giorgio Massimo Fontanive
 */
public class Server {
    private static Controller controller;
    private static int connectionsCount;
    private final Object lock;

    public static void main(String[] args) throws IOException {
        //TODO: Show player initial state of the game
        connectionsCount = 0;
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        controller = new Controller();
        Server server = new Server();
        new Thread(controller).start();
        new Thread(server::acceptConnectionsTCP).start();
        new Thread(server::acceptConnectionsRMI).start();
    }

    public Server() {
        lock = new Object();
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
                ClientHandlerTCP clientHandler = new ClientHandlerTCP(connectionsCount, socket, dataInputStream, dataOutputStream);
                new Thread(clientHandler).start();
                controller.addClient(clientHandler);
                connectionsCount++;
            } catch (IOException e) {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                e.printStackTrace();
            }
        }
    }

    public synchronized void acceptConnectionsRMI() {
        int connectionsIndex = 0;
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(SERVER_PORT + 1);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ClientHandlerRMI clientHandler = new ClientHandlerRMI(connectionsIndex);
        while (true) {
            while (clientHandler.isAvailable()) {
                try {
                    clientHandler.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            new Thread(clientHandler).start();
            controller.addClient(clientHandler);
            clientHandler = new ClientHandlerRMI(connectionsIndex);
            try {
                registry.rebind("ServerRMI" + connectionsIndex, clientHandler);
                connectionsCount++;
                connectionsIndex++;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
