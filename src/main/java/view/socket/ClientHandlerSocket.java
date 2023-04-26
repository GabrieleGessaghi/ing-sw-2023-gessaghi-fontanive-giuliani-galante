package view.socket;

import controller.observer.Event;
import controller.observer.Observer;
import view.ClientHandler;

import java.io.InputStream;
import java.io.OutputStream;

public class ClientHandlerSocket extends ClientHandler {
    private int port;
    private InputStream inputstream;
    private OutputStream outputstream;


    @Override
    public void updateObservers(Event event) {

    }

    @Override
    public void update(Event event) {

    }

    @Override
    public void registerObserver(Observer observer) {

    }

    @Override
    public void notifyObservers(Event event) {

    }

    @Override
    public void requestInput(Promt promt) {

    }

    @Override
    public void run() {

    }

    @Override
    protected void showOutput(String jsonMessage) {

    }
}
