package view.socket;

import controller.Prompt;

import java.io.*;
import java.net.Socket;

public class NetworkHandlerSocket extends Thread{

    private Socket clientSocket;
    private Prompt lastPrompt;

    private DataInputStream dis;
    private DataOutputStream dos;


    /**
     * Class constructor
     * @author Gabriele Gessaghi
     */
    public NetworkHandlerSocket () {
        clientSocket = new Socket();
        lastPrompt = null;
    }

    public boolean ping () {
        return true;
    }

    @Override
    public void run () {}

    public void sendInput (String input) {}
}
