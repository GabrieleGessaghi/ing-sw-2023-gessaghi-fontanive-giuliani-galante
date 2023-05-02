package client.network;

import client.Client;
import com.google.gson.stream.JsonReader;
import server.controller.Prompt;

import java.io.*;
import java.net.Socket;

public class NetworkHandlerTCP implements Runnable{
    private Socket serverSocket;
    private String host;
    private Client client;

    //NEL COSTRUTTORE PASSA SOLO CLIENT

    /**
     * Class constructor.
     * @author Gabriele Gessaghi
     * @param client : calling client
     * @param host : server ip
     */
    public NetworkHandlerTCP(Client client, String host){
        this.client = client;
        this.host = host;
    }

    /**
     *
     * @author Gabriele Gessaghi
     */
    //LASCIARE STARE PER ORA
    public boolean ping () {
        return true;
    }

    /**
     * Establish the server connection and check the received string, ask for client input or send to client the string.
     * @author Gabriele Gessaghi
     */
    @Override
    public void run () {
        //TRY
            //CONNETTITI AL SERVER "LOCALHOST" CON PORTA 1234 CREANDO IL SOCKET
            //CREA GLI OGGETTI DATAINPUTSTREAM E DATAOUTPUTSTREAM
            //WHILE TRUE
                //RICEVI IN INGRESSO UNA STRINGA DAL CLIENTHANDLER
                //PARSARE IL JSON
                //NEL CASO CI SIANO CAMPI DI RICHIESTA (VEDI MESSAGGIO WHATSAPP)
                    //CHIAMARE IL METODO requestInput DI CLIENT CON IL GIUSTO PROMPT (Non ancora implementato)
                //ALTRIMENTI MANDARE A showOutput DI CLIENT PASSANDO LA STRINGA
    try {
        serverSocket = new Socket(host,1234);
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

    /**
     * Send the string on the output buffer.
     * @author Gabriele Gessaghi
     * @param input : String to write on the output buffer.
     */
    public void sendInput (String input) {
        //MANDA LA STRINGA CON L'OGGETTO DATAOUTPUTSTREAM
        try {
            OutputStreamWriter out = new OutputStreamWriter(serverSocket.getOutputStream());
            out.write(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
