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
    public static void main() throws IOException {
        //TODO: Add javadoc
        //TODO: Add connection to RMI
        ConfigLoader.loadConfiguration("/src/main/resources/configuration.json");
        Controller controller = new Controller();
        new Thread(controller).start();
        int connectionsCount = 0;
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
            } catch (Exception e) {
                if (socket != null)
                    socket.close();
                e.printStackTrace();
            }
        }
    }
}
