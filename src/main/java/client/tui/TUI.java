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

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Scanner;

import static client.tui.Colors.*;
import static server.controller.utilities.ConfigLoader.*;

/**
 * TUI class.
 * @author Niccolò Galante
 */
public class TUI implements Client {
    private NetworkHandler networkHandler;
    private final String nickname;
    private String lastInput;

    public TUI(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Asks user for initial information then runs a loop scanning for user input.
     * @author Giorgio Massimo Fontanive
     */
    public static void main(String[] args) {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");

        printOpening();
        Scanner scn = new Scanner(System.in);

        System.out.print("Insert nickname: ");
        String nickname = scn.nextLine();

        System.out.print("Insert host's IP address: ");
        String hostIp = scn.nextLine();

        int selection;
        do {
            System.out.print("Select connection type (0 TCP/1 RMI): ");
            selection = scn.nextInt();
        } while (selection !=0 && selection != 1);

        TUI client = new TUI(nickname);
        NetworkHandler networkHandler = selection == 0 ? new NetworkHandlerTCP() : new NetworkHandlerRMI();
        networkHandler.setClient(client);
        networkHandler.setHost(hostIp);
        new Thread(networkHandler).start();
        client.setNetworkHandler(networkHandler);

        String userInput = "";
        while (!userInput.equals("exit")) {
            userInput = scn.nextLine();
            switch (userInput) {
                case "chat" -> client.sendNewMessage();
                default -> client.handleInput(userInput);
            }
        }
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
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

    //TODO: Improve this method
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
                    case "nicknames" -> toPrint.append(printNicknames(jsonReader));
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
            System.out.print("How many tokens would you like to select? (1-3): ");
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
            } while (tokenCoordinates[i] != null && tokenCoordinates[i].length() != 2 &&
                    (tokenCoordinates[i].charAt(0) < 'a' || tokenCoordinates[i].charAt(0) > 'i' ||
                            tokenCoordinates[i].charAt(1) < '1' || tokenCoordinates[i].charAt(1) > '9'));

        //Order selection
        if (numberOfTokens > '1') {
            String orderSelection;
            for (int i = 0; i < numberOfTokens - '0'; i++)
                System.out.print("\n" + (i + 1) +  ": " + tokenCoordinates[i]);

            do {
                System.out.print("\nIn what order would you like to insert the selected tokens? ");
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
            System.out.print("Insert column in which you want to insert the selected tokens: ");
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
        toPrint.append("\n").append("Personal card: \n").append(" 1  2  3  4  5\n");
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
        toPrint.append("\n");
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