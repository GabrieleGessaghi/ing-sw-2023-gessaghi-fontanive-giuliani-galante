package server.controller;

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
    public static void main() throws IOException {
        //TODO: Add javadoc
        //TODO: Add abstract ClientHandler and maybe NetworkHandler
        //TODO: Add observable to personalcard
        connectionsCount = 0;
        ConfigLoader.loadConfiguration("/src/main/resources/configuration.json");
        controller = new Controller();
        new Thread(controller).start();
        new Thread(Server::acceptConnectionsTCP).start();
        new Thread(Server::acceptConnectionsRMI).start();
    }

    public static void acceptConnectionsTCP() {
        while (true) {
            Socket socket = null;
            try {
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
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

    public static void acceptConnectionsRMI() {
        int connectionsIndex = 0;
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(SERVER_PORT);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ClientHandlerRMI clientHandler = null;
        while (true) {
            if (clientHandler == null || !clientHandler.isAvailable()) {
                clientHandler = new ClientHandlerRMI(connectionsIndex);
                try {
                    registry.rebind("ServerRMI" + connectionsIndex, clientHandler);
                    new Thread(clientHandler).start();
                    controller.addClient(clientHandler);
                    connectionsCount++;
                    connectionsIndex++;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
