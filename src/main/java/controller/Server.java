package controller;

import view.socket.ClientHandlerSocket;

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
    private static Controller controller;
    private static int connectionsCount;
    public static void main() throws IOException {
        //TODO: Load configurations
        controller = new Controller();
        controller.start();
        connectionsCount = 0;
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
                controller.addClient(clientHandler);
                connectionsCount++;
                //TODO: Register observers
                //TODO: Lower connections count
            } catch (Exception e) {
                if (socket != null)
                    socket.close();
                e.printStackTrace();
            }
        }
    }
}
