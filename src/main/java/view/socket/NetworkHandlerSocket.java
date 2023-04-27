package view.socket;

import controller.Promt;

import java.io.*;
import java.net.Socket;

public class NetworkHandlerSocket extends Thread{

    private Socket clientSocket;
    private Promt lastPromt;

    private DataInputStream dis;
    private DataOutputStream dos;


    /**
     * Class constructor
     * @author Gabriele Gessaghi
     */
    public NetworkHandlerSocket () {
        clientSocket = new Socket();
        lastPromt = null;
    }

    public boolean ping () {
        return true;
    }

    @Override
    public void run () {}

    public void sendInput (String input) {}
}
