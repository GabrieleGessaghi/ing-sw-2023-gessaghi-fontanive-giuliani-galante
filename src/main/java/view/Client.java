package view;

import com.google.gson.JsonObject;
import controller.Prompt;
import view.socket.NetworkHandlerSocket;

import java.awt.desktop.SystemEventListener;
import java.util.ArrayList;
import java.util.Scanner;

import static controller.utilities.ConfigLoader.BOARD_SIZE;


/**
 * Client class.
 * @author Niccolò Galante
 */
public class Client {
    private final String nickname;
    private NetworkHandlerSocket nhs;


    public Client(String nick){
        this.nickname = nick;
        this.nhs = null;
    }

    public void main(){

    }

    /**
     * Receives input from network handler and requests input from client.
     * @param prompt type of information requested.
     * @author Niccolò Galante
     */
    public void requestInput(Prompt prompt){
        switch (prompt) {
            case NICKNAME -> requestNickname();
            case PLAYERSNUMBER -> requestNumberOfPlayers();
            case TOKENS -> System.out.println("Select tokens\n");
            case COLUMN -> System.out.println("Select column\n");
            //case CONNECTIONTYPE -> selectConnectionType;
            //TODO: move connection type selection to main
        }
    }

    public void selectConnectionType(){
        Scanner scn = new Scanner(System.in);
        int selection;

        System.out.println("Select connection type:\n" + "0: socket\n" + "1: RMI\n");
        selection = scn.nextInt();
        while(selection !=0 && selection != 1) {
            System.out.println("Connection type not valid!\n");
            System.out.println("Select connection type:\n" + "0: socket\n" + "1: RMI\n");
            selection = scn.nextInt();
        }

        if(selection == 0)
            nhs = new NetworkHandlerSocket();
    }

    private void requestNickname(){
        Scanner scn = new Scanner(System.in);
        JsonObject jsonObject = new JsonObject();
        String input;

        System.out.println("Insert nickname:\n");
        String name = scn.nextLine();
        jsonObject.addProperty("Nickname", name);
        input = jsonObject.toString();
        nhs.sendInput(input);
    }

    private void requestNumberOfPlayers(){
        Scanner scn = new Scanner(System.in);
        JsonObject jsonObject = new JsonObject();
        String input;
        int numberOfPlayers;

        System.out.println("Insert number of players:\n");
        numberOfPlayers = scn.nextInt();

        while(numberOfPlayers < 2 || numberOfPlayers > 4){
            System.out.println("Number not valid!\n");
            System.out.println("Insert number of players:\n");
            numberOfPlayers = scn.nextInt();
        }

        jsonObject.addProperty("numberOfPlayers", numberOfPlayers);
        input = jsonObject.toString();
        nhs.sendInput(input);
    }

    private void selectTokens(){
        Scanner scn = new Scanner(System.in);
        int numberOfTokens;
        char[] tokenCoordinates = new char[2];
        int[] selectionInt = new int[2];
        int[][] selectedTokens = new int[BOARD_SIZE][BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++)
            for(int j = 0; j < BOARD_SIZE; j++)
                selectedTokens[i][j] = -1;

        System.out.println("How many tokens would you like to select?\n");
        numberOfTokens = scn.nextInt();

        while(numberOfTokens < 1 || numberOfTokens > 3){
            System.out.println("Number not valid!\n");
            System.out.println("How many tokens would you like to select?\n");
            numberOfTokens = scn.nextInt();
        }

        for(int i = 0; i < numberOfTokens; i++){
            for(int j = 0; j < 2; j++) {
                System.out.println("Insert x coordinate\n");
                tokenCoordinates[0] = scn.next().charAt(0);

                while(tokenCoordinates[0] < 'a' || tokenCoordinates[0] > 'i'){
                    System.out.println("Invalid x coordinate!\n");
                    System.out.println("Insert x coordinate\n");
                    tokenCoordinates[0] = scn.next().charAt(0);
                }

                System.out.println("Insert y coordinate\n");
                tokenCoordinates[1] = scn.next().charAt(1);
                while(tokenCoordinates[1] < '1' || tokenCoordinates[1] > '9'){
                    System.out.println("Invalid y coordinate!\n");
                    System.out.println("Insert y coordinate\n");
                    tokenCoordinates[1] = scn.next().charAt(0);
                }
            }

            selectionInt[0] = tokenCoordinates[0] - 'a';
            selectionInt[1] = tokenCoordinates[1] - '1';


            //TODO: specify in matrix the order in which tokens have been selected (0, 1, 2)
        }

    }

}
