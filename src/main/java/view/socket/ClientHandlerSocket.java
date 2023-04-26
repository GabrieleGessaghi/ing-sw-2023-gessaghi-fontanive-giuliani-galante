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
    private int port;
    private Promt lastRequest;
    private boolean itIsARequest;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket s;

    private ArrayList<Observer> observers;
    public ClientHandlerSocket(ArrayList<Observer> observers, int port,Socket s, InputStream inputStream, OutputStream outputStream){
        this.observers = observers;
        this.port = port;
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

    }



    @Override
    public void requestInput(Promt promt) {
        this.lastRequest = promt;
    }

    @Override
    public void run() {
        boolean requestSent = false;
        while(true) {
            try{
                OutputStreamWriter out = new OutputStreamWriter(outputStream);
                InputStreamReader in = new InputStreamReader(inputStream);
                BufferedReader buffer = new BufferedReader(in);
                if(itIsARequest){
                    if(lastRequest == Promt.NICKNAME) {
                        out.write("{requestNickname:true}");
                        requestSent = true;
                    }
                }
                String line = buffer.readLine();

                if(requestSent  && lastRequest == Promt.NICKNAME) {
                    Event nickname = new Event(buffer.readLine());
                    updateObservers(nickname);
                    requestSent = false;
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
