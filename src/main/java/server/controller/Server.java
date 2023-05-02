package server.controller;

import server.controller.utilities.ConfigLoader;
import server.view.ClientHandlerSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
        //TODO: Add connection to RMI
        //TODO: Add abstract ClientHandler and maybe NetworkHandler
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
                ClientHandlerSocket clientHandler = new ClientHandlerSocket(connectionsCount, socket, dataInputStream, dataOutputStream);
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
        while (true) {

        }
    }
}
