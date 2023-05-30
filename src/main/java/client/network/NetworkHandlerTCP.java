package client.network;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import server.controller.Prompt;

import java.io.*;
import java.net.Socket;

import static server.controller.utilities.ConfigLoader.SERVER_PORT;

public class NetworkHandlerTCP extends NetworkHandler {
    private Socket serverSocket;

    /**
     * Send the string on the output buffer.
     * @author Gabriele Gessaghi
     * @param input : String to write on the output buffer.
     */
    public void sendInput(String input) {
        try {
            if (serverSocket != null) {
                OutputStreamWriter out = new OutputStreamWriter(serverSocket.getOutputStream());
                out.write(input + "\n");
                out.flush();
            }
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Establish the server connection and check the received string, ask for client input or send to client the string.
     * @author Gabriele Gessaghi
     */
    @Override
    public void run() {
        super.run();
        try {
            serverSocket = new Socket(host, SERVER_PORT);
            InputStreamReader in = new InputStreamReader(serverSocket.getInputStream());
            BufferedReader buffer = new BufferedReader(in);
            while (isConnected){
                String receivedString = buffer.readLine();
                if (receivedString != null) {
                    String field;
                    JsonReader jsonReader = new JsonReader(new StringReader(receivedString));
                    jsonReader.beginObject();
                    boolean alreadyPrinted = false;
                    while (jsonReader.hasNext()) {
                        field = jsonReader.nextName();
                        switch (field) {
                            case "requestNickname" -> client.requestInput(Prompt.NICKNAME);
                            case "requestPlayerNumber" -> client.requestInput(Prompt.PLAYERSNUMBER);
                            case "requestTokens" -> client.requestInput(Prompt.TOKENS);
                            case "requestColumn" -> client.requestInput(Prompt.COLUMN);
                            case "ping" -> {}
                            default -> {
                                if (!alreadyPrinted) {
                                    client.showOutput(receivedString);
                                    alreadyPrinted = true;
                                }
                            }
                        }
                        jsonReader.skipValue();
                    }
                    jsonReader.endObject();
                }
            }
            in.close();
            buffer.close();
        } catch (IOException e) {
            disconnect();
        }
    }
}
