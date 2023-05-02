package server.view.rmi;

import server.controller.Prompt;

import java.rmi.Remote;

public interface ServerUsable extends Remote {
     void receiveInput(Prompt Input);
     void showOutput(String output);
}
