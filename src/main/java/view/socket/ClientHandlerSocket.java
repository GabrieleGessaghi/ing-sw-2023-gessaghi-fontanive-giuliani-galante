package view.socket;

import controller.observer.Event;
import controller.observer.Observer;
import view.ClientHandler;
import controller.Prompt;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * socket class to handle the Client
 * @author Niccolò Giuliani
 */
public class ClientHandlerSocket extends ClientHandler {
    private Prompt lastRequest;
    private final boolean isThereRequest;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Socket s;
    private final ArrayList<Observer> observers;

    public ClientHandlerSocket(Socket s, InputStream inputStream, OutputStream outputStream){
        this.s = s;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.lastRequest = Prompt.NICKNAME;
        this.isThereRequest = true;
        this.observers = new ArrayList<>();

    }

    @Override
    public void updateObservers(Event event) {
        for(Observer o : observers)
            o.update(event);
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
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void requestInput(Prompt prompt) {
        this.lastRequest = prompt;
    }

    //TODO: Exit condition and closing the resources
    @Override
    public void run() {
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(in);
        while(true) {
            try{
                if(isThereRequest){
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
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        try{
            out.write(jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
