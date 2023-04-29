package client;

import server.controller.Prompt;

import java.io.*;
import java.net.Socket;

public class NetworkHandlerSocket extends Thread{

    private Socket clientSocket;
    private Prompt lastPrompt;
    private Client client;

    /**
     * Class constructor
     * @author Gabriele Gessaghi
     */
    public NetworkHandlerSocket () {}

    /**
     *
     * @Author Gabriele Gessaghi
     * @return
     */
    public boolean ping () {
        return true;
    }

    /**
     *
     * @Author Gabriele Gessaghi
     */
    @Override
    public void run () {}

    /**
     *
     * @Author Gabriele Gessaghi
     * @param input
     */
    public void sendInput (String input) {}
}
