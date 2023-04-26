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
    private static boolean isGameRunning;
    private static ArrayList<ClientHandlerSocket> clientHandlers = new ArrayList<>();
    private static TurnController turnController;
    private static CreationController creationController;
    public static void main() throws IOException {
        reset();
        creationController = new CreationController();
        while (true) {
            Socket socket = null;
            try {
                ServerSocket serverSocket = new ServerSocket(5056);
                socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                ClientHandlerSocket clientHandler = new ClientHandlerSocket(socket, dataInputStream, dataOutputStream);
                clientHandler.start();
                clientHandlers.add(clientHandler);
                if (creationController.getIsGameReady()) {
                    Game game = creationController.createGame();
                    turnController = new TurnController(game);
                    isGameRunning = true;
                }
            } catch (Exception e) {
                if (socket != null)
                    socket.close();
                e.printStackTrace();
            }
        }
    }

    private static void reset() {
        isGameRunning = false;
        clientHandlers = new ArrayList<>();
        creationController = new CreationController();
    }
}
