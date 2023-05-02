package client.network;

import client.Client;
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
    public boolean ping () {
        return true;
    }

    /**
     * Send the string on the output buffer.
     * @author Gabriele Gessaghi
     * @param input : String to write on the output buffer.
     */
    public void sendInput (String input) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(serverSocket.getOutputStream());
            out.write(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Establish the server connection and check the received string, ask for client input or send to client the string.
     * @author Gabriele Gessaghi
     */
    @Override
    public void run () {
        try {
            serverSocket = new Socket(host, SERVER_PORT);
            InputStreamReader in = new InputStreamReader(serverSocket.getInputStream());
            BufferedReader buffer = new BufferedReader(in);
            while (true){
                String receivedString = buffer.readLine();
                String field;
                boolean exit = false;
                JsonReader jsonReader = new JsonReader(new StringReader(receivedString));
                jsonReader.beginObject();
                while(jsonReader.hasNext()) {
                    field = jsonReader.nextName();
                    switch (field) {
                        case "requestNickname" -> client.requestInput(Prompt.NICKNAME);
                        case "requestPlayerNumber" -> client.requestInput(Prompt.PLAYERSNUMBER);
                        case "requestTileSelection" -> client.requestInput(Prompt.TOKENS);
                        case "requestColumn" -> client.requestInput(Prompt.COLUMN);
                        case "closeConnection" -> exit = true;
                        default -> client.showOutput(receivedString);
                    }
                }
                jsonReader.endObject();
                if (exit) break;
            }
            try {
                in.close();
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
