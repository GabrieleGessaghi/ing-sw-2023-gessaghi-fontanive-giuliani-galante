package server.view.tcp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Prompt;
import server.controller.observer.Event;
import server.view.ClientHandler;

import java.io.*;
import java.net.Socket;

/**
 * socket class to handle the TUI
 * @author Niccolò Giuliani
 */
public class ClientHandlerTCP extends ClientHandler {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    //private final Socket socket;

    /**
     * Class constructor. The server accepting new connections creates this object for every new connection.
     * @author Niccolò Giuliani
     * @param socket The socket bound to this object.
     * @param inputStream Used to receive input from the client.
     * @param outputStream Used to send input to the client.
     */
    public ClientHandlerTCP(Socket socket, InputStream inputStream, OutputStream outputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        isConnected = false;
        //this.socket = socket;
    }

    @Override
    public void update(Event event) {

        //Skips this event if it does not involve the client
        JsonObject jsonObject = JsonParser.parseString(event.jsonMessage()).getAsJsonObject();

        if (jsonObject.has("privateMessageSender") || jsonObject.has("privateMessageReceiver"))
            if (!jsonObject.get("privateMessageSender").getAsString().equals(nickname) &&
                    !jsonObject.get("privateMessageReceiver").getAsString().equals(nickname))
                return;

        sendOutput(event.jsonMessage());
    }

    @Override
    public void run() {
        isConnected = true;
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(in);
        while(true) {
            try {
                String line = buffer.readLine();
                if (line != null) {
                    Event event = new Event(line);
                    updateObservers(event);
                }
            } catch (IOException e) {
                System.out.println("Error reading TCP message!");
                isConnected = false;
            }

            if (!isConnected)
                break;
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
        sendOutput(jsonObject.toString());
    }

    @Override
    public void sendOutput(String jsonMessage) {
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        try{
            out.write(jsonMessage + "\n");
            out.flush();
        } catch (IOException e) {
            System.out.println("Error while sending TCP message.");
            isConnected = false;
        }
    }
}
