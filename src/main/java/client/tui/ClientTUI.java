package client.tui;

import client.network.NetworkHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import server.controller.Prompt;
import server.controller.utilities.ConfigLoader;
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
 * ClientTUI class.
 * @author Niccolò Galante
 */
public class ClientTUI {
    private final String nickname;
    private NetworkHandler networkHandler;

    public ClientTUI(String nickname) {
        this.nickname = nickname;
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    /**
     * Receives input from network handler and requests input from client.
     * @author Niccolò Galante
     * @param prompt type of information requested.
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
     * Asks player to insert nickname.
     * @author Niccolò Galante
     */
    private void requestNickname(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickname", nickname);
        networkHandler.sendInput(jsonObject.toString());
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
        networkHandler.sendInput(input);
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
        networkHandler.sendInput(jMatrixToSend.toString());
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
        networkHandler.sendInput(input);
    }

    /**
     * Displays requested output to client.
     * @author Gabriele Gessaghi
     * @param toShow requested information.
     */
    public void showOutput (String toShow){
        JsonReader jsonReader = new JsonReader(new StringReader(toShow));
        String field;
        StringBuilder toPrint = new StringBuilder();
        try {
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "nickname" -> toPrint.append("Player: ").append(jsonReader.nextString()).append("\n");
                    case "totalPoints" -> toPrint.append("Points: ").append(jsonReader.nextInt()).append("\n");
                    case "isFirstPlayer" -> {
                        if (jsonReader.nextBoolean()) {
                            toPrint.append("First player\n");
                        }else {
                            toPrint.append("Not first player\n");
                        }
                    }
                    case "playerIndex" -> toPrint.append("Player index: ").append(jsonReader.nextInt()).append("\n");
                    case "currentPlayerNickname" -> toPrint.append("Current player: ").append(jsonReader.nextString()).append("\n");
                    case "objectiveDescription" -> toPrint.append("Common Objective description: ").append(jsonReader.nextString()).append("\n");
                    case "numberOfTokensLeft" -> toPrint.append("Remaining tokens: ").append(jsonReader.nextInt()).append("\n");
                    case "nextPointsAvailable" -> toPrint.append("Next common card points: ").append(jsonReader.nextInt()).append("\n");
                    case "board" -> {
                        toPrint.append("Board: \n");
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
                        toPrint.append("Shelf: \n");
                        jsonReader.beginObject();
                        if (jsonReader.nextName().equals("shelfTiles")) {
                            int[][] intMatrix = JsonTools.readMatrix(jsonReader);
                            for (int i = 0; i < ConfigLoader.SHELF_ROWS; i++) {
                                for (int j = 0; j < SHELF_COLUMNS; j++) {
                                    toPrint.append("| ").append(Token.values()[intMatrix[i][j]]).append(" |");
                                }
                                toPrint.append("\n");
                            }
                            toPrint.append("\n");
                        } else {
                            jsonReader.skipValue();
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

    /**
     * Converts integer value to the initial of each token.
     * @author Niccolò Galante
     * @param value integer value that is to be converted.
     * @return converted integer value.
     */
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
