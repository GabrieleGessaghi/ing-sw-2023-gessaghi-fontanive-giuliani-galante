package controller;

import controller.observer.Event;
import controller.observer.Observer;
import model.Game;
import view.socket.ClientHandlerSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//TODO: Add port in configuartions file

/**
 *
 * @author Giorgio Massimo Fontanive
 */
public class Server {
    private static Controller controller;
    public static void main() throws IOException {
        controller = new Controller();
        while (true) {
            Socket socket = null;
            try {
                ServerSocket serverSocket = new ServerSocket(1234);
                socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                ClientHandlerSocket clientHandler = new ClientHandlerSocket(socket, dataInputStream, dataOutputStream);
                clientHandler.start();
                controller.addClient(clientHandler);
                //TODO: Register observers
            } catch (Exception e) {
                if (socket != null)
                    socket.close();
                e.printStackTrace();
            }
        }
    }
}
