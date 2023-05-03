package client.network;

import client.Client;
import client.tui.ClientTUI;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import server.controller.Prompt;

import java.io.*;
import java.net.Socket;

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

public class NetworkHandlerTCP extends NetworkHandler {
    private Socket serverSocket;

    /**
     * Class constructor.
     * @author Gabriele Gessaghi
     * @param client : calling client
     * @param host : server ip
     */
    public NetworkHandlerTCP(Client client, String host){
        super(client, host);
    }

    /**
     * @author Gabriele Gessaghi
     */
    public void ping() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ping", true);
        sendInput(jsonObject.toString());
    }

    /**
     * Send the string on the output buffer.
     * @author Gabriele Gessaghi
     * @param input : String to write on the output buffer.
     */
    public void sendInput(String input) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(serverSocket.getOutputStream());
            out.write(input + "\n");
            out.flush();
            System.out.println("Output sent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Establish the server connection and check the received string, ask for client input or send to client the string.
     * @author Gabriele Gessaghi
     */
    @Override
    public void run() {
        try {
            serverSocket = new Socket(host, SERVER_PORT);
            InputStreamReader in = new InputStreamReader(serverSocket.getInputStream());
            BufferedReader buffer = new BufferedReader(in);
            while (true){
                String receivedString = buffer.readLine();
                if (receivedString != null) {
                    String field;
                    boolean exit = false;
                    JsonReader jsonReader = new JsonReader(new StringReader(receivedString));
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        field = jsonReader.nextName();
                        switch (field) {
                            case "requestNickname" -> client.requestInput(Prompt.NICKNAME);
                            case "requestPlayerNumber" -> client.requestInput(Prompt.PLAYERSNUMBER);
                            case "requestTileSelection" -> client.requestInput(Prompt.TOKENS);
                            case "requestColumn" -> client.requestInput(Prompt.COLUMN);
                            case "closeConnection" -> exit = true;
                            case "pong" -> jsonReader.skipValue();
                            default -> {
                                client.showOutput(receivedString);
                                jsonReader.skipValue();
                            }
                        }
                    }
                    jsonReader.endObject();
                    if (exit) break;
                }
            }
            in.close();
            buffer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
