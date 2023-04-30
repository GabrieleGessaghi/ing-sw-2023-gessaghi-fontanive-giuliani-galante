package client;

import server.controller.Prompt;

import java.io.*;
import java.net.Socket;

public class NetworkHandlerSocket extends Thread{
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;

    //LASCIARE STARE PER ORA
    public boolean ping () {
        return true;
    }

    @Override
    public void run () {
        //TRY
            //CONNETTITI AL SERVER "LOCALHOST" CON PORTA 1234 CREANDO IL SOCKET
            //CREA GLI OGGETTI DATAINPUTSTREAM E DATAOUTPUTSTREAM
            //WHILE TRUE
                //RICEVI IN INGRESSO UNA STRINGA DAL CLIENTHANDLER
                //PARSARE IL JSON
                //NEL CASO CI SIANO CAMPI DI RICHIESTA (VEDI MESSAGGIO WHATSAPP)
                    //CHIAMARE IL METODO requestInput DI CLIENT CON IL GIUSTO PROMPT (Non ancora implementato)
                //ALTRIMENTI MANDARE A showOutput DI CLIENT PASSANDO LA STRINGA
    }

    public void sendInput (String input) {
        //MANDA LA STRINGA CON L'OGGETTO DATAOUTPUTSTREAM
    }
}
