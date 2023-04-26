package view.socket;

import com.google.gson.stream.JsonReader;
import controller.exceptions.TooManyPlayersException;
import controller.observer.Event;
import controller.observer.Observer;
import view.ClientHandler;
import view.Promt;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandlerSocket extends ClientHandler {
    private Promt lastRequest;
    private boolean itIsARequest;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket s;

    private ArrayList<Observer> observers;
    public ClientHandlerSocket(Socket s, InputStream inputStream, OutputStream outputStream){
        this.s = s;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.lastRequest = Promt.NICKNAME;
        this.itIsARequest = true;
    }
    @Override
    public void updateObservers(Event event) {
        for(Observer o : observers)
            o.update(event);
    }

    @Override
    public void update(Event event) {

    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }



    @Override
    public void requestInput(Promt promt) {
        this.lastRequest = promt;
    }

    @Override
    public void run() {
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(in);
        while(true) {
            try{

                if(itIsARequest){
                    switch (lastRequest) {
                        case NICKNAME -> out.write("{requestNickname:true}");
                        case PLAYERSNUMBER -> out.write("{requestPlayersNumber:true}");
                        case TOKENS -> out.write("{requestTokens:true}");
                        case COLUMN -> out.write("{requestColumn:true}");
                    }
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
    protected void showOutput(String jsonMessage) {

    }
}
