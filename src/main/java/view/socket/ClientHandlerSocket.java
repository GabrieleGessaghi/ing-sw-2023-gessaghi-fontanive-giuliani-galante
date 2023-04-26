package view.socket;

import controller.observer.Event;
import controller.observer.Observer;
import view.ClientHandler;
import view.Promt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandlerSocket extends ClientHandler {
    private int port;
    private Promt lastPromt;
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
        this.lastPromt = promt;
    }

    @Override
    public void run() {
      /*  while(true){
            try{


            } catch (IOException e){
                e.printStackTrace();
            }
        }*/
    }

    @Override
    protected void showOutput(String jsonMessage) {

    }
}
