package client;

import server.controller.Prompt;

import java.rmi.Remote;

public interface NetworkHandlerRMIInterface extends Remote {
     void receiveInput(Prompt Input);
     void showOutput(String output);
}
