package client.tui;

import client.Client;
import client.network.NetworkHandler;
import client.network.NetworkHandlerRMI;
import client.network.NetworkHandlerTCP;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import server.controller.Prompt;
import server.controller.utilities.ConfigLoader;
import server.controller.utilities.JsonTools;
import server.model.View;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static client.tui.Colors.*;
import static server.controller.utilities.ConfigLoader.*;

/**
 * TUI class.
 * @author Niccolò Galante
 */
public class TUI implements Client {
    private NetworkHandler networkHandler;
    private String nickname;
    private String lastInput;

    public TUI(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Asks user for initial information then runs a loop scanning for user input.
     * @author Giorgio Massimo Fontanive
     */
    public static void main(String[] args) {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        printOpening();
        Scanner scn = new Scanner(System.in);
        System.out.print("Insert nickname: ");
        String nickname = scn.nextLine();
        TUI client = new TUI(nickname);
        client.connect();

        String userInput = "";
        while (!userInput.equals("exit")) {
            userInput = scn.nextLine();
            switch (userInput) {
                case "chat" -> client.sendNewMessage();
                case "view" -> client.requestNewView();
                case "connect" -> client.connect();
                default -> client.handleInput(userInput);
            }
        }
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void connect() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Insert host's IP address: ");
        String hostIp = scn.nextLine();
        int selection;
        do {
            System.out.print("Select connection type (0 TCP/1 RMI): ");
            selection = scn.nextInt();
        } while (selection !=0 && selection != 1);
        NetworkHandler networkHandler = selection == 0 ? new NetworkHandlerTCP() : new NetworkHandlerRMI();
        networkHandler.setClient(this);
        networkHandler.setHost(hostIp);
        new Thread(networkHandler).start();
        setNetworkHandler(networkHandler);
    }

    /**
     * Receives input from network handler and requests input from client.
     * @author Niccolò Galante
     * @param prompt Type of information requested.
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
     * Updates the method waiting for the user's input.
     * @param userInput A string typed by the user.
     * @author Giorgio Massimo Fontanive
     */
    public void handleInput(String userInput) {
        synchronized (this) {
            lastInput = userInput;
            this.notifyAll();
        }
    }

    /**
     * Lets the user send messages to the chat
     * @author Giorgio Massimo Fontanive
     */
    public void sendNewMessage() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Type \"public\" or the receiver's nickname: ");
        String receiver = scn.nextLine();
        System.out.println("Type the message: ");
        String message = scn.nextLine();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("senderNickname", nickname);
        jsonObject.addProperty("message", message);
        if (!receiver.equals("public")) {
            jsonObject.addProperty("receiverNickname", receiver);
        }

        networkHandler.sendInput(jsonObject.toString());
    }

    /**
     *
     */
    public void requestNewView() {
        Scanner scn = new Scanner(System.in);
        System.out.println("What do you want to see?");
        String request = scn.nextLine();
        JsonObject jsonObject = new JsonObject();
        switch (request) {
            case "board" -> jsonObject.addProperty("requestBoard", true);
            case "common cards" -> jsonObject.addProperty("requestCommonCards", true);
            case "chat" -> jsonObject.addProperty("requestChat", true);
            case "shelf" -> jsonObject.addProperty("requestShelf", true);
            case "personal card" -> jsonObject.addProperty("requestPersonalCard", true);
        }
        if (request.equals("chat")) {
            System.out.println("Which chat do you want to see?");
            jsonObject.addProperty("requestedPlayerNickname", scn.nextLine());
        }
        if (request.equals("shelf")) {
            System.out.println("Whose shelf do you want to see?");
            jsonObject.addProperty("requestedPlayerNickname", scn.nextLine());
        }
        networkHandler.sendInput(jsonObject.toString());
    }

    /**
     * Displays requested output to client.
     * @author Gabriele Gessaghi
     * @param toShow Requested information.
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
                    case "newNickname" -> nickname = jsonReader.nextString();
                    case "nicknames" -> toPrint.append(printNicknames(jsonReader));
                    case "points" -> toPrint.append("Points: ").append(jsonReader.nextInt()).append("\n");
                    case "isFirstPlayer" -> toPrint.append(jsonReader.nextBoolean() ? "First player" : "Not first player").append("\n");
                    case "commonCard0", "commonCard1" -> toPrint.append(printCommonCard(jsonReader)).append("\n");
                    case "message", "privateMessage", "publicMessage", "error" -> toPrint.append(jsonReader.nextString()).append("\n");
                    case "messages" -> {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext())
                            toPrint.append(jsonReader.nextString()).append("\n");
                        jsonReader.endArray();
                    }
                    case "tiles" -> toPrint.append(printTiles(jsonReader));
                    case "shelf" -> toPrint.append(printShelf(jsonReader));
                    case "personalCard" -> toPrint.append(printPersonalCard(jsonReader));
                    case "currentPlayerNickname" -> {
                        String currentPlayer = jsonReader.nextString();
                        toPrint.append(currentPlayer.equals(nickname) ? "It's your turn!" : "It's " + currentPlayer + "'s turn!").append("\n");
                    }
                    case "connectionError" -> {
                        toPrint.append("You were disconnected. Please type \"connect\" to reconnect.\n");
                        jsonReader.skipValue();
                    }
                    case "ping" -> {
                        return;
                    }
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            System.out.print(toPrint);
        } catch (IOException e) {
            System.out.println("Error while parsing received JSON string.");
        }
    }

    /**
     * Stops the thread's execution until a new user input is received.
     * @author Giorgio Massimo Fontanive
     */
    private void waitForInput() {
        lastInput = null;
        synchronized (this) {
            while (lastInput == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Sends the player's nickname.
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
        do {
            System.out.print("Insert number of players: ");
            waitForInput();
        } while (lastInput.charAt(0) < '2' || lastInput.charAt(0) > '4');

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("playersNumber", lastInput.charAt(0) - '0');
        networkHandler.sendInput(jsonObject.toString());
    }

    /**
     * Asks player to select tokens from board.
     * @author Niccolò Galante
     */
    private void requestTokenSelection() {
        int[][] selectedTokens = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            Arrays.fill(selectedTokens[i], -1);

        //Number of tokens
        char numberOfTokens;
        do {
            System.out.print("\nHow many tokens would you like to select? (1-3): ");
            waitForInput();
            numberOfTokens = lastInput.charAt(0);
        } while(numberOfTokens < '1' || numberOfTokens > '3');

        //Token selection with coordinates
        String[] tokenCoordinates = new String[3];
        Arrays.fill(tokenCoordinates, "");
        for(int i = 0; i < numberOfTokens - '0'; i++)
            do {
                System.out.print("Insert coordinates for token " + (i+1) + ": ");
                waitForInput();
                tokenCoordinates[i] = lastInput;

            } while (tokenCoordinates[i].length() != 2 ||
                    (tokenCoordinates[i].charAt(0) < 'a' || tokenCoordinates[i].charAt(0) > 'i' ||
                            tokenCoordinates[i].charAt(1) < '1' || tokenCoordinates[i].charAt(1) > '9'));

        //Order selection
        if (numberOfTokens > '1') {
            String orderSelection;
            for (int i = 0; i < numberOfTokens - '0'; i++)
                System.out.print((i + 1) +  ": " + tokenCoordinates[i] + "\n");

            do {
                System.out.print("In what order would you like to insert the selected tokens? ");
                waitForInput();
                orderSelection = lastInput;
            } while(orderSelection.length() != numberOfTokens - '0');

            //Place in int matrix
            for(int i = 0; i < numberOfTokens - '0'; i++){
                String tokenCoordinate = tokenCoordinates[orderSelection.charAt(i) - '1'];
                selectedTokens[tokenCoordinate.charAt(1) - '0'][tokenCoordinate.charAt(0) - 'a'] = i;
            }
        } else
            selectedTokens[tokenCoordinates[0].charAt(1) - '0'][tokenCoordinates[0].charAt(0) - 'a'] = 0;

        JsonArray jMatrix;
        JsonObject jMatrixToSend = new JsonObject();
        jMatrix = JsonTools.createJsonMatrix(selectedTokens);
        jMatrixToSend.add("selectedTiles", jMatrix);
        networkHandler.sendInput(jMatrixToSend.toString());
    }

    /**
     * Asks player to select column from their shelf to insert tokens in.
     * @author Niccolò Galante
     */
    private void requestColumnSelection(){
        int selectedColumn;
        do {
            System.out.print("\nInsert column in which you want to insert the selected tokens: ");
            waitForInput();
            selectedColumn = lastInput.charAt(0) - '1';
        } while (selectedColumn < 0 || selectedColumn > SHELF_COLUMNS - 1);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("selectedColumn", selectedColumn);
        networkHandler.sendInput(jsonObject.toString());
    }

    /**
     * Prints the board's tiles.
     * @author Niccolò Galante
     * @param jsonReader Reads json input.
     * @return Tiles to be shown in a string format.
     * @throws IOException When there's an issue reading the json matrix.
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
     * @return Shelf to be shown in a string format.
     * @throws IOException When there's an issue reading the json matrix.
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
     * @throws IOException When there's an issue reading the json matrix.
     */
    private StringBuilder printPersonalCard(JsonReader jsonReader) throws IOException {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("Personal card: \n").append(" 1  2  3  4  5\n");
        jsonReader.beginObject();
        while (jsonReader.hasNext())
            if (jsonReader.nextName().equals("correctTiles")) {
                int[][] intMatrix = JsonTools.readMatrix(jsonReader);
                for (int i = 0; i < SHELF_ROWS; i++) {
                    for (int j = 0; j < SHELF_COLUMNS; j++) {
                        toPrint.append(intToTokenInitial(intMatrix[i][j]));
                    }
                    toPrint.append("\n");
                }
            } else
                jsonReader.skipValue();
        jsonReader.endObject();
        return toPrint;
    }

    private StringBuilder printCommonCard(JsonReader jsonReader) throws IOException {
        StringBuilder toPrint = new StringBuilder();
        jsonReader.beginObject();
        while (jsonReader.hasNext())
            switch (jsonReader.nextName()) {
                case "objectiveDescription" -> toPrint.append("Common Objective description:\n").append(jsonReader.nextString()).append("\n");
                case "numberOfTokensLeft" -> toPrint.append("Remaining tokens: ").append(jsonReader.nextInt()).append("\n");
                case "nextPointsAvailable" -> toPrint.append("Next common card points: ").append(jsonReader.nextInt()).append("\n");
                default -> jsonReader.skipValue();
            }
        jsonReader.endObject();
        return toPrint;
    }

    /**
     * Prints every player's nickname in the order they connected to the server, which is the order they will play in.
     * @param jsonReader Reads json input.
     * @return The list of names to be shown.
     * @author Giorgio Massimo Fontanive
     */
    private StringBuilder printNicknames(JsonReader jsonReader) throws IOException {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("The players, in order, are: \n");
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            toPrint.append(jsonReader.nextString()).append("\n");
        }
        jsonReader.endArray();
        return toPrint;
    }

    /**
     * Prints opening screen.
     * @author Niccolò Galante
     */
    private static void printOpening(){
        System.out.print("\033[0;1m" + "\n" +
                "                               ___             ___  .-.                \n" +
                "                              (   )           (   )/    \\  .-.         \n" +
                " ___ .-. .-.  ___  ___   .--.  | | .-.   .--.  | | | .`. ;( __) .--.   \n" +
                "(   )   '   \\(   )(   )/  _  \\ | |/   \\ /    \\ | | | |(___|''\")/    \\  \n" +
                " |  .-.  .-. ;| |  | |. .' `. ;|  .-. .|  .-. ;| | | |_    | ||  .-. ; \n" +
                " | |  | |  | || |  | || '   | || |  | ||  | | || |(   __)  | ||  | | | \n" +
                " | |  | |  | || '  | |_\\_`.(___) |  | ||  |/  || | | |     | ||  |/  | \n" +
                " | |  | |  | |'  `-' (   ). '. | |  | ||  ' _.'| | | |     | ||  ' _.' \n" +
                " | |  | |  | | `.__. || |  `\\ || |  | ||  .'.-.| | | |     | ||  .'.-. \n" +
                " | |  | |  | | ___ | |; '._,' '| |  | |'  `-' /| | | |     | |'  `-' / \n" +
                "(___)(___)(___|   )' | '.___.'(___)(___)`.__.'(___|___)   (___)`.__.'  \n" +
                "               ; `-' '                                                 \n" +
                "                .__.'                                                  \n" +
                COLOUR_RESET
        );
        System.out.print("\n\n");
        System.out.print("\033[0;1m" + "                              WELCOME!\n\n\n" + COLOUR_RESET);

    }

    /**
     * Converts integer value to the color of each token.
     * @author Niccolò Galante
     * @param value Integer value that is to be converted.
     * @return Color value followed by three spaces and a color rest.
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
}