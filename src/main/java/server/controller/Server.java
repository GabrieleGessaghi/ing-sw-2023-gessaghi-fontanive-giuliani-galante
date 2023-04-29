package server.controller;

import server.controller.utilities.ConfigLoader;
import server.view.ClientHandlerSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Accepts new connections and starts the game controller.
 * @author Giorgio Massimo Fontanive
 */
public class Server {
    public static void main() throws IOException {
        //TODO: Maybe put on different method
        //TODO: Implement updateObserver calls in model
        ConfigLoader.loadConfiguration("/src/main/resources/configuration.json");
        Controller controller = new Controller();
        controller.start();
        int connectionsCount = 0;
        while (true) {
            Socket socket = null;
            try {
                //TODO: Add port in configuartions file
                ServerSocket serverSocket = new ServerSocket(1234);
                socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                ClientHandlerSocket clientHandler = new ClientHandlerSocket(connectionsCount, socket, dataInputStream, dataOutputStream);
                clientHandler.start();
                controller.addClient(connectionsCount, clientHandler);
                connectionsCount++;
                //TODO: Register observers
            } catch (Exception e) {
                if (socket != null)
                    socket.close();
                e.printStackTrace();
            }
        }
    }
}
