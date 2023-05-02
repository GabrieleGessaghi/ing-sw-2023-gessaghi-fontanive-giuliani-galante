package server.view.tcp;

import com.google.gson.JsonObject;
import server.controller.observer.Event;
import server.view.ClientHandler;

import java.io.*;
import java.net.Socket;

/**
 * socket class to handle the Client
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
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        try{
            out.write(event.getJsonMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(in);
        while(true) {
            try{
                if(isThereRequest) {
                    JsonObject jsonObject = new JsonObject();
                    switch (lastRequest) {
                        case NICKNAME -> jsonObject.addProperty("requestNickname", true);
                        case PLAYERSNUMBER -> jsonObject.addProperty("requestPlayerNumber", true);
                        case TOKENS -> jsonObject.addProperty("requestTokens", true);
                        case COLUMN -> jsonObject.addProperty("requestColumn", true);
                    }
                    out.write(jsonObject.toString());
                    isThereRequest = false;
                }
                String line = buffer.readLine();
                if(line != null) {
                    Event event = new Event(buffer.readLine());
                    updateObservers(event);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showOutput(String jsonMessage) {
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        try{
            out.write(jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
