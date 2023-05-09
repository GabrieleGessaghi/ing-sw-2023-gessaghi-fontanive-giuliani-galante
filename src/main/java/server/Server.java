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

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

/**
 * Accepts new connections and starts the game controller.
 * @author Giorgio Massimo Fontanive
 */
public class Server {
    private static Controller controller;
    private static int connectionsCount;

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

    public void acceptConnectionsRMI() {
        int connectionsIndex = 0;
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(SERVER_PORT + 1);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ClientHandlerRMI clientHandler = new ClientHandlerRMI();
        while (true) {
            try {
                ClientUsable stub = (ClientUsable) UnicastRemoteObject.exportObject(clientHandler,0);
                registry.rebind("ServerRMI" + connectionsIndex, stub);

                clientHandler.waitForConnection();
                new Thread(clientHandler).start();

                clientHandler.waitForConnection();
                System.out.println("Two way connection completed!");
                controller.addClient(clientHandler);

                clientHandler = new ClientHandlerRMI();
                connectionsCount++;
                connectionsIndex++;
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
