package client;

import com.google.gson.stream.JsonReader;
import server.controller.Prompt;
import server.controller.utilities.JsonTools;

import java.io.*;
import java.net.Socket;

public class NetworkHandlerSocket implements Runnable{
    private Socket serverSocket;
    private String host;
    private Client client;

    //NEL COSTRUTTORE PASSA SOLO CLIENT

    public NetworkHandlerSocket (Client client, String host){
        this.client = client;
        this.host = host;
    }

    //LASCIARE STARE PER ORA
    public boolean ping () {
        return true;
    }

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
            JsonReader jsonReader = new JsonReader(new StringReader(receivedString));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "requestNickname" -> client.requestInput(Prompt.NICKNAME);
                    case "requestPlayerNumber" -> client.requestInput(Prompt.PLAYERSNUMBER);
                    case "requestTileSelection" -> client.requestInput(Prompt.TOKENS);
                    case "requestColumn" -> client.requestInput(Prompt.COLUMN);
                    default -> client.showOutput(receivedString);
                }
            }
            jsonReader.endObject();
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    }

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
