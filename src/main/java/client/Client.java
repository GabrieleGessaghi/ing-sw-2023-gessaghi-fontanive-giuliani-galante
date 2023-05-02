package client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import server.controller.Prompt;
import server.controller.utilities.ConfigLoader;
import client.NetworkHandlerSocket;
import server.controller.utilities.JsonTools;
import server.model.Token;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Scanner;

import static server.controller.utilities.ConfigLoader.BOARD_SIZE;
import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static server.controller.utilities.JsonTools.createJsonMatrix;


/**
 * Client class.
 * @author Niccolò Galante
 */
public class Client {
    private String nickname;
    private NetworkHandlerSocket nhs;

    /**
     * Class constructor
     * @author Niccolò Galante
     */
    public Client(String nick){
        this.nickname = nick;
        this.nhs = null;
    }

    public void main(){
        String hostIp;
        Scanner scn = new Scanner(System.in);

        System.out.println("Insert host's IP address:\n");
        hostIp = scn.nextLine();
        requestConnectionType(hostIp);

        System.out.println("Insert nickname:\n");
        nickname = scn.nextLine();
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
            case TOKENS -> requestTokenSelection();
            case COLUMN -> requestColumnSelection();
        }
    }

    /**
     * Asks player to select connection type (either socket or RMI).
     * @author Niccolò Galante
     */
    private void requestConnectionType(String host){
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
            nhs = new NetworkHandlerSocket(this, host);
        else {}
            //nhs = new RMIHandlerSocket();
        new Thread(nhs).start();
    }

    /**
     * Asks player to insert nickname.
     * @author Niccolò Galante
     */
    private void requestNickname(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Nickname", nickname);
        nhs.sendInput(jsonObject.toString());
    }

    /**
     * Asks player to insert number of players in the game.
     * @author Niccolò Galante
     */
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

    /**
     * Asks player to select tokens from board.
     * @author Niccolò Galante
     */
    private void requestTokenSelection(){
        Scanner scn = new Scanner(System.in);
        JsonArray jMatrix;
        JsonObject jMatrixToSend = new JsonObject();
        int numberOfTokens;
        char[] tokenCoordinates = new char[2];
        int[] selectionInt = new int[2];
        int[][] selectedTokens = new int[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        for(int i = 0; i < ConfigLoader.BOARD_SIZE; i++)
            for(int j = 0; j < ConfigLoader.BOARD_SIZE; j++)
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

            selectedTokens[selectionInt[0]][selectionInt[1]] = i;
        }
        jMatrix = createJsonMatrix(selectedTokens);
        jMatrixToSend.add("selectedTokens", jMatrix);
        nhs.sendInput(jMatrixToSend.toString());
    }

    /**
     * Asks player to select column from their shelf to insert tokens in.
     * @author Niccolò Galante
     */
    private void requestColumnSelection(){
        Scanner scn = new Scanner(System.in);
        JsonObject jsonObject = new JsonObject();
        String input;
        int selectedColumn;

        System.out.println("Insert column in which you want to insert the selected tokens:\n");
        selectedColumn = scn.nextInt();

        while(selectedColumn < 0 || selectedColumn > SHELF_COLUMNS - 1){
            System.out.println("Column not valid!\n");
            System.out.println("Insert column in which you want to insert the selected tokens:\n");
            selectedColumn = scn.nextInt();
        }

        jsonObject.addProperty("selectedColumn", selectedColumn);
        input = jsonObject.toString();
        nhs.sendInput(input);
    }

    public void showOutput (String toShow){
        JsonReader jsonReader = new JsonReader(new StringReader(toShow));
        String field;
        String toPrint = "";
        try {
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "nickname" -> toPrint += "Player: " + jsonReader.nextString() + "\n";
                    case "totalPoints" -> toPrint += "Points: " + jsonReader.nextInt() + "\n";
                    case "isFirstPlayer" -> {
                        if (jsonReader.nextBoolean()) {
                            toPrint += "First player\n";
                        }else {
                            toPrint += "Not first player\n";
                        }
                    }
                    case "playerIndex" -> toPrint += "Player index: " + jsonReader.nextInt() + "\n";
                    case "currentPlayerNickname" -> toPrint += "Current player: " + jsonReader.nextString() + "\n";
                    case "objectiveDescription" -> toPrint += "Common Objective description: " + jsonReader.nextString() + "\n";
                    case "numberOfTokensLeft" -> toPrint += "Remaining tokens: " + jsonReader.nextInt() + "\n";
                    case "nextPointsAvailable" -> toPrint += "Next common card points: " + jsonReader.nextInt() + "\n";
                    case "board" -> {
                        toPrint += "Board: \n";
                        jsonReader.beginObject();
                        int[][] intMatrix = JsonTools.readMatrix(jsonReader);
                        char[][] charMatrix = new char[BOARD_SIZE][BOARD_SIZE];

                        for(int i = 0; i < BOARD_SIZE; i++)
                            for(int j = 0; j < BOARD_SIZE; j++)
                                charMatrix[i][j] = intToTokenInitial(intMatrix[i][j]);

                        System.out.println("  A B C D E F G H I\n");
                        for(int i = 0; i < BOARD_SIZE; i++)
                            System.out.println(i + "" + Arrays.toString(charMatrix[i]) + "\n");

                        jsonReader.endObject();
                    }
                    case "shelf" -> {
                        toPrint += "Shelf: \n";
                        jsonReader.beginObject();
                        switch (jsonReader.nextName()){
                            case "shelfTiles" -> {
                                int [][] intMatrix = JsonTools.readMatrix(jsonReader);
                                for (int i=0; i<ConfigLoader.SHELF_ROWS; i--){
                                    for (int j = 0; j<ConfigLoader.SHELF_COLUMNS; j--) {
                                        toPrint += "| " + Token.values()[intMatrix[i][j]] + " |";
                                    }
                                    toPrint += "\n";
                                }
                                toPrint += "\n";
                            }
                            default -> jsonReader.skipValue();
                        }
                        jsonReader.endObject();
                    }
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            System.out.println(toPrint);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private char intToTokenInitial(int value){
        char tokenInitial;
        tokenInitial = switch (value){
            case 0 -> 'X'; // nothing
            case 1 -> 'C'; // cat
            case 2 -> 'B'; // book
            case 3 -> 'Y'; // toy
            case 4 -> 'T'; // trophy
            case 5 -> 'F'; // frame
            case 6 -> 'P'; // plant
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
        return tokenInitial;
    }

}
