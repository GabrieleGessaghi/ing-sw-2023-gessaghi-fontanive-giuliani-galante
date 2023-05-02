package server.view;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.Prompt;
import server.view.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * socket class to handle the Client
 * @author Niccolò Giuliani
 */
public class ClientHandlerSocket extends ClientHandler {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Socket socket;

    public ClientHandlerSocket(int index, Socket socket, InputStream inputStream, OutputStream outputStream){
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
