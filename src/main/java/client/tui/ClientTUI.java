package client.tui;

import client.Client;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import server.controller.Prompt;
import server.controller.utilities.JsonTools;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Scanner;

import static client.tui.Colors.*;
import static server.controller.utilities.ConfigLoader.*;

/**
 * ClientTUI class.
 * @author Niccolò Galante
 */
public class ClientTUI extends Client {

    private final String nickname;

    public ClientTUI(String nickname) {
        this.nickname = nickname;
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

        System.out.print("Insert number of players: ");
        numberOfPlayers = scn.nextInt();
        while(numberOfPlayers < 2 || numberOfPlayers > 4){
            System.out.print("Number not valid!\n");
            System.out.print("Insert number of players: ");
            numberOfPlayers = scn.nextInt();
        }

        jsonObject.addProperty("playersNumber", numberOfPlayers);
        input = jsonObject.toString();
        networkHandler.sendInput(input);
    }

    /**
     * Asks player to select tokens from board.
     * @author Niccolò Galante
     */
    //TODO: Improve this
    private void requestTokenSelection(){
        Scanner scn = new Scanner(System.in);
        JsonArray jMatrix;
        JsonObject jMatrixToSend = new JsonObject();
        char numberOfTokens;
        String[] tokenCoordinates = new String[3];
        int[][] selectedTokens = new int[BOARD_SIZE][BOARD_SIZE];
        String orderSelection;

        Arrays.fill(tokenCoordinates, "");

        for(int i = 0; i < BOARD_SIZE; i++)
            Arrays.fill(selectedTokens[i], -1);

        //NUMBER OF TOKENS
        System.out.print("How many tokens would you like to select?: ");
        numberOfTokens = scn.nextLine().charAt(0);
        while(numberOfTokens < '1' || numberOfTokens > '3'){
            System.out.print("Number not valid!\n");
            System.out.print("How many tokens would you like to select?: ");
            numberOfTokens = scn.nextLine().charAt(0);
        }

        //TOKEN SELECTION WITH COORDINATES
        for(int i = 0; i < numberOfTokens - '0'; i++) {
            System.out.print("Insert coordinates for token " + (i+1) + ": ");
            tokenCoordinates[i] = scn.nextLine();
            while (tokenCoordinates[i] != null && (tokenCoordinates[i].charAt(0) < 'a' || tokenCoordinates[i].charAt(0) > 'i' || tokenCoordinates[i].charAt(1) < '1' || tokenCoordinates[i].charAt(1) > '9')) {
                System.out.print("\nCoordinates not valid!\n");
                System.out.print("Insert coordinates for token " + i);
                tokenCoordinates[i] = scn.nextLine();
            }
        }

        //ORDER SELECTION
        if(numberOfTokens > '1') {
            System.out.print("\nIn what order would you like to insert the selected tokens?\n");
            for (int i = 0; i < numberOfTokens - '0'; i++)
                System.out.print((i + 1) +  ": " + tokenCoordinates[i] + "\n");
            orderSelection = scn.nextLine();
            while(orderSelection.length() != numberOfTokens - '0'){
                System.out.print("\nIn what order would you like to insert the selected tokens?");
                for (int i = 0; i < numberOfTokens - '0'; i++)
                    System.out.print((i + 1) +  ": " + tokenCoordinates[i] + "\n");
                orderSelection = scn.nextLine();
            }

            for(int i = 0; i < numberOfTokens - '0'; i++){
                String tokenCoordinate = tokenCoordinates[orderSelection.charAt(i) - '1'];
                selectedTokens[tokenCoordinate.charAt(1) - '0'][tokenCoordinate.charAt(0) - 'a'] = i;
            }


        }
        jMatrix = JsonTools.createJsonMatrix(selectedTokens);
        jMatrixToSend.add("selectedTiles", jMatrix);
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

        System.out.print("Insert column in which you want to insert the selected tokens: ");
        selectedColumn = scn.nextInt();
        selectedColumn -= 1;

        while(selectedColumn < 0 || selectedColumn > SHELF_COLUMNS - 1){
            System.out.print("Column not valid!\n");
            System.out.print("Insert column in which you want to insert the selected tokens: ");
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
        toPrint.append("\n");
        try {
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "nickname" -> toPrint.append("Player: ").append(jsonReader.nextString()).append("\n");
                    case "totalPoints" -> toPrint.append("Points: ").append(jsonReader.nextInt()).append("\n");
                    case "isFirstPlayer" -> toPrint.append(jsonReader.nextBoolean() ? "First player\n" : "Not first player\n");
                    case "playerIndex" -> toPrint.append("Player index: ").append(jsonReader.nextInt()).append("\n");
                    case "currentPlayerNickname" -> toPrint.append("Current player: ").append(jsonReader.nextString()).append("\n");
                    case "objectiveDescription" -> toPrint.append("Common Objective description:\n").append(jsonReader.nextString()).append("\n");
                    case "numberOfTokensLeft" -> toPrint.append("Remaining tokens: ").append(jsonReader.nextInt()).append("\n");
                    case "nextPointsAvailable" -> toPrint.append("Next common card points: ").append(jsonReader.nextInt()).append("\n");
                    case "message" -> toPrint.append(jsonReader.nextString()).append("\n");
                    case "tiles" -> toPrint.append(printTiles(jsonReader));
                    case "shelf" -> toPrint.append(printShelf(jsonReader));
                    case "personalCard" -> toPrint.append(printPersonalCard(jsonReader));
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            System.out.print(toPrint);
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
    private String intToTokenInitial(int value){
        String tokenInitial;
        tokenInitial = switch (value){
            case 0 -> BLACK_BACKGROUND + "   "; // nothing
            case 1 -> GREEN_BACKGROUND + "   "; // cat
            case 2 -> WHITE_BACKGROUND + "   "; // book
            case 3 -> YELLOW_BACKGROUND + "   "; // game
            case 4 -> CYAN_BACKGROUND + "   "; // trophy
            case 5 -> BLUE_BACKGROUND + "   "; // frame
            case 6 -> RED_BACKGROUND + "   "; // plant
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
        tokenInitial += COLOUR_RESET;
        return tokenInitial;
    }

    /**
     * Prints tiles.
     * @author Niccolò Galante
     * @param jsonReader Reads json input.
     * @return Tiles to be shown.
     * @throws IOException when there's an issue.
     */
    private StringBuilder printTiles(JsonReader jsonReader) throws IOException {
        StringBuilder toPrint = new StringBuilder();

        toPrint.append("Board: \n");
        int[][] intMatrix = JsonTools.readMatrix(jsonReader);

        toPrint.append("   A  B  C  D  E  F  G  H  I\n");
        for(int i = 0; i < BOARD_SIZE; i++) {
            toPrint.append(i).append(" ");
            for (int j = 0; j < BOARD_SIZE; j++)
                toPrint.append(intToTokenInitial(intMatrix[i][j]));
            toPrint.append("\n");
        }
        return toPrint;
    }

    /**
     * Prints shelf.
     * @author Niccolò Galante
     * @param jsonReader Reads json input.
     * @return Shelf to be shown.
     * @throws IOException when there's an issue.
     */
    private StringBuilder printShelf(JsonReader jsonReader) throws IOException {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("Shelf: \n").append(" 1  2  3  4  5\n");
        jsonReader.beginObject();
        if (jsonReader.nextName().equals("shelfTiles")) {
            int[][] intMatrix = JsonTools.readMatrix(jsonReader);
            for (int i = 0; i < SHELF_ROWS; i++) {
                for (int j = 0; j < SHELF_COLUMNS; j++) {
                    toPrint.append(intToTokenInitial(intMatrix[i][j]));
                }
                toPrint.append("\n");
            }
        } else {
            jsonReader.skipValue();
        }
        jsonReader.endObject();

        return toPrint;
    }

    /**
     * Prints personal card.
     * @author Niccolò Galante
     * @param jsonReader Reads json input.
     * @return Personal card to be shown.
     * @throws IOException when there's an issue.
     */
    private StringBuilder printPersonalCard(JsonReader jsonReader) throws IOException{
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("\n").append("Personal card: \n").append(" 1  2  3  4  5\n");
        jsonReader.beginObject();
        if (jsonReader.nextName().equals("correctTiles")) {
            int[][] intMatrix = JsonTools.readMatrix(jsonReader);
            for (int i = 0; i < SHELF_ROWS; i++) {
                for (int j = 0; j < SHELF_COLUMNS; j++) {
                    toPrint.append(intToTokenInitial(intMatrix[i][j]));
                }
                toPrint.append("\n");
            }
        } else {
            jsonReader.skipValue();
        }
        jsonReader.endObject();
        toPrint.append("\n");
        return toPrint;
    }
}
