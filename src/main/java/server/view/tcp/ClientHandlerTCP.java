package server.view.tcp;

import com.google.gson.JsonObject;
import server.controller.Prompt;
import server.controller.observer.Event;
import server.view.ClientHandler;

import java.io.*;
import java.net.Socket;

/**
 * socket class to handle the ClientTUI
 * @author Niccolò Giuliani
 */
public class ClientHandlerTCP extends ClientHandler {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Socket socket;

    /**
     * Class constructor. The server accepting new connections creates this object for every new connection.
     * @author Niccolò Giuliani
     * @param index The client's unique identifier.
     * @param socket The socket bound to this object.
     * @param inputStream Used to receive input from the client.
     * @param outputStream Used to send input to the client.
     */
    public ClientHandlerTCP(int index, Socket socket, InputStream inputStream, OutputStream outputStream){
        super(index);
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void update(Event event) {
        sendOutput(event.getJsonMessage());
    }

    @Override
    public void run() {
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(in);
        while(true) {
            try {
                String line = buffer.readLine();
                System.out.println(line);
                if (line != null) {
                    Event event = new Event(line);
                    updateObservers(event);
                    System.out.println("Received: " + event.getJsonMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestInput(Prompt prompt) {
        JsonObject jsonObject = new JsonObject();
        switch (prompt) {
            case NICKNAME -> jsonObject.addProperty("requestNickname", true);
            case PLAYERSNUMBER -> jsonObject.addProperty("requestPlayerNumber", true);
            case TOKENS -> jsonObject.addProperty("requestTokens", true);
            case COLUMN -> jsonObject.addProperty("requestColumn", true);
        }
        System.out.println("Sending request");
        sendOutput(jsonObject.toString());
    }

    @Override
    public void sendOutput(String jsonMessage) {
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        try{
            out.write(jsonMessage);
            out.flush();
            System.out.println("Outsput sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
