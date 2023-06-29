package server.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.utilities.ConfigLoader;
import server.controller.utilities.JsonTools;
import server.model.cards.CommonCard;
import server.model.cards.CommonType;
import server.model.exceptions.IllegalColumnException;
import server.model.exceptions.IllegalMoveException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static server.controller.utilities.ConfigLoader.NUMBER_OF_COMMON_CARDS;

/**
 * Controls a single game and its players.
 */
public class Game implements Savable, Observable {
    private int currentPlayerIndex;
    private int numberOfPlayers;
    private Board board;
    private Player[] players;
    private CommonCard[] commonCards;
    private final List<Observer> observers;
    private boolean isLastRound;
    private boolean isGameFinished;

    /**
     * Class constructor, currentPlayer is set to player with firstPlayer true.
     * @author Gabriele Gessaghi
     * @param numberOfPlayers is the number of players of the game session.
     * @param playerNicknames a list of all the players' nicknames
     */
    public Game(int numberOfPlayers, ArrayList<String> playerNicknames) {
        players = new Player[numberOfPlayers];
        board = new Board(numberOfPlayers);
        commonCards = new CommonCard[NUMBER_OF_COMMON_CARDS];
        observers = new ArrayList<>();
        isLastRound = false;
        isGameFinished = false;
        this.numberOfPlayers = numberOfPlayers;

        //generates the unique 4 digit code for the current game
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            Date now = new Date();
            String input = now.toString();
            byte[] hash = md.digest(input.getBytes());
            int code = Math.abs(Arrays.hashCode(hash)) % 10000;
            String gameID = String.format("%04d", code);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        commonCards = generateCommonCards(numberOfPlayers);

        // using a set to ensures having 3 different numbers
        Set<Integer> randomNumbers = new HashSet<>();
        while(randomNumbers.size() < numberOfPlayers){

            // using System.currentTimeMillis() as Random seeds for consistency in random generation of the numbers
            randomNumbers.add(new Random(System.currentTimeMillis()).nextInt(12) + 1);
        }

        int[] personalCardsIndexes = randomNumbers.stream().mapToInt(Integer::intValue).toArray();
        boolean isFirstPlayer = true;
        for (int i = 0; i < numberOfPlayers; i++) {
            if (i!=0)
                isFirstPlayer = false;
            players[i] = new Player(playerNicknames.get(i), isFirstPlayer, personalCardsIndexes[i], List.of(commonCards));
        }
        currentPlayerIndex = 0;
    }

    /**
     * Generates two random commonCard for the current game.
     * @author Gabriele Gessaghi
     * @param numberOfPlayers The number of player for the current game.
     */
    private CommonCard[] generateCommonCards(int numberOfPlayers){
        ArrayList<CommonType> types = new ArrayList<CommonType>(Arrays.asList(CommonType.values()));
        Collections.shuffle(types);
        commonCards = new CommonCard[NUMBER_OF_COMMON_CARDS];
        for (int i = 0; i < NUMBER_OF_COMMON_CARDS; i++)
            commonCards[i] = new CommonCard(types.get(i), numberOfPlayers, i);
        return commonCards;
    }

    /**
     * Save the game state in case of disconnections or other problems.
     * @author Gabriele Gessaghi
     * @throws IOException When there's an error in the file creation.
     */
    private void saveGame() throws IOException {
        String gameState = getState().toString();
        try (PrintWriter out = new PrintWriter("/saved_game.txt")) {
            out.println(gameState);
        }
    }

    /**
     * Reload a saved state of a previous game.
     * @author Gabriele Gessaghi
     */
    public void loadGame() {
        Path filePath = Path.of("/saved_game.txt");
        String gameState;
        try {
            gameState = Files.readString(filePath);
            loadState(JsonParser.parseString(gameState).getAsJsonObject());
        } catch (IOException e) {
            System.out.println("Save not found");
        }
    }

    /**
     * Loads the nicknames from the saved game
     * @return A list of nicknames.
     */
    public static ArrayList<String> loadNicknames() {
        Path filePath = Path.of("/saved_game.txt");
        String gameState;
        try {
            gameState = Files.readString(filePath);
            JsonObject jsonObject = JsonParser.parseString(gameState).getAsJsonObject();
            ArrayList<String> nicknames = new ArrayList<>();
            int i = 0;
            while (jsonObject.has("player" + i)) {
                nicknames.add(jsonObject.get("player" + i).getAsJsonObject().get("nickname").getAsString());
                i++;
            }
            return nicknames;
        } catch (IOException e) {
            System.out.println("Save not found");
        }
        return null;
    }

    /**
     * Checks whether there already was a save game, meaning the server crashed in the meantime
     * @author Gabriele Gessaghi
     * @return True if the file containing the game state is found
     */
    public static boolean isThereGameSaved() {
        Path filePath = Path.of("/saved_game.txt");
        try {
            String gameState = Files.readString(filePath);
            return true;
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Deletes the save file if it finds one.
     */
    public static void deleteSave() {
        Path filePath = Path.of("src/main/resources/saved_game.txt");
        try {
            Files.delete(filePath);
        } catch (IOException ignored) {}
    }

    /**
     * Cycles the player index and checks whether the game is over.
     * @author Giorgio Massimo Fontainve
     */
    private void nextPlayerIndex() {
        currentPlayerIndex++;
        if (currentPlayerIndex == players.length)
            if (!isLastRound)
                currentPlayerIndex = 0;
            else
                isGameFinished = true;
    }

    /**
     * Finds the reference to the player with the given nickname.
     * @param nickname The requested player's nickname.
     * @return A reference to the requested player.
     * @author Giorgio Massimo Fontainve
     */
    private Player findPlayer(String nickname) {
        for (Player player : players)
            if (player.getNickname().equals(nickname))
                return player;
        return null;
    }

    /**
     * Collect the selected tiles from the game board and update the current player shelf.
     * @author Gabriele Gessaghi
     * @param selectedTiles A matrix with -1 for the tiles not chosen and
     *                      the order of choice for the other ones.
     * @param column The column on which the player wants to put the new tiles.
     */
    public void playerTurn (int [][] selectedTiles, int column) throws IllegalMoveException, IllegalColumnException{
        while (!players[currentPlayerIndex].isConnected)
            nextPlayerIndex();

        //Gets the tiles from the board.
        Token[] selectedTokens;
        selectedTokens = board.selectTiles(selectedTiles);

        //Inserts the tokens in the shelf.
        players[currentPlayerIndex].insertTokens(selectedTokens, column);

        //Removes tiles from the board.
        boolean[][] isSelected = Board.convertIntegerMatrix(selectedTiles, -1);
        board.removeTiles(isSelected);

        //Checks if the player has filled their shelf
        if (!isLastRound && players[currentPlayerIndex].isShelfFull()) {
            isLastRound = true;
            String message = JsonTools.createMessage("This is the last round!", false);
            updateObservers(new Event(message));
        }
        nextPlayerIndex();

        try {
            saveGame();
        } catch (IOException e) {
            System.out.println("Unable to save game");
        }
    }

    /**
     * Checks whether the game is over, and sends a message containing the winner's nickname.
     * @author Giorgio Massimo Fontanive
     * @return True if the game is over.
     */
    public boolean gameOver() {
        if (isGameFinished) {
            int maxPoints = 0;
            Player winner = players[0];
            for (Player player : players)
                if (player.getPoints() >= maxPoints) {
                    maxPoints = player.getPoints();
                    winner = player;
                }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("winnerNickname", winner.getNickname());
            jsonObject.addProperty("winnerPoints", winner.getPoints());
            updateObservers(new Event(jsonObject.toString()));
            deleteSave();
            return true;
        }
        return false;
    }

    /**
     * Sends the requested thing to the observers as a JSON string.
     * @author Giorgio Massimo Fontanive
     * @param view The requested thing to be seen.
     */
    public void sendState(View view) {
        updateObservers(new Event(getView(view, null).toString()));
    }

    /**
     * Creates a json object describing a particular topic of the game.
     * @param view The topic requested.
     * @param playerNickname The nickname of the specific player the topic is about, otherwise null.
     * @return A json object containing the requested information.
     */
    public JsonObject getView(View view, String playerNickname) {
        JsonObject jsonObject = new JsonObject();
        Player requestedPlayer = playerNickname == null ? null : findPlayer(playerNickname);
        switch (view) {
            case BOARD -> jsonObject = board.getState();
            case CURRENT_PLAYER -> jsonObject.addProperty("currentPlayerNickname", players[currentPlayerIndex].getNickname());
            case PLAYER_NICKNAMES -> {
                JsonArray jsonArray = new JsonArray();
                for (Player player : players)
                    jsonArray.add(player.getNickname());
                jsonObject.add("nicknames", jsonArray);
            }
            case COMMON_CARDS -> {
                for (int i = 0; i < commonCards.length; i++)
                    jsonObject.add("commonCard" + i, commonCards[i].getState());
            }
            case SPECIFIC_PLAYER -> {
                if (requestedPlayer != null)
                    jsonObject = requestedPlayer.getState();
            }
            case PERSONAL_CARD -> {
                if (requestedPlayer != null) {
                    JsonObject playerState = requestedPlayer.getState();
                    jsonObject.add("personalCard", playerState.get("personalCard").getAsJsonObject());
                }
            }
            case SHELF -> {
                if (requestedPlayer != null) {
                    JsonObject playerState = requestedPlayer.getState();
                    jsonObject.addProperty("nickname", playerNickname);
                    jsonObject.add("shelf", playerState.get("shelf").getAsJsonObject());
                }
            }
            case POINTS -> {
                if (requestedPlayer != null) {
                    JsonObject playerState = requestedPlayer.getState();
                    jsonObject.addProperty("points", playerState.get("totalPoints").getAsInt());
                }
            }
        }
        return jsonObject;
    }

    /**
     * Changes the player's connection status.
     * @param playerNickname The player's nickname.
     * @param isConnected True if the player is connected, false otherwise.
     * @author Giorgio Massimo Fontainve
     */
    public synchronized void setPlayerConnection(String playerNickname, boolean isConnected) {
        Player requestedPlayer = findPlayer(playerNickname);
        if (requestedPlayer != null)
            requestedPlayer.isConnected = isConnected;
    }

    /**
     * Gets the nickname of whose turn it is.
     * @return The nickname of the current Player.
     */
    public String getCurrentPlayer() {
        while (!players[currentPlayerIndex].isConnected)
            nextPlayerIndex();
        return players[currentPlayerIndex].getNickname();
    }

    /**
     * Getter for the board user by the controller to check whether to make players repeat their selections.
     * @return The board for this game.
     */
    public Board getBoard() {
        return board;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
        for (CommonCard commonCard : commonCards)
            commonCard.registerObserver(observer);
    }

    @Override
    public void updateObservers(Event event) {
        for (Observer observer : observers)
            observer.update(event);
    }

    @Override
    public JsonObject getState() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("numberOfPlayers", numberOfPlayers);
        jsonObject.addProperty("isLastRound", isLastRound);
        jsonObject.addProperty("currentPlayerIndex", currentPlayerIndex);
        jsonObject.addProperty("currentPlayerNickname", players[currentPlayerIndex].getNickname());
        jsonObject.add("board", board.getState());
        for (int i = 0; i < players.length; i++)
            jsonObject.add("player" + i, players[i].getState());
        for (int i =0; i < commonCards.length; i++)
            jsonObject.add("commonCard" + i, commonCards[i].getState());
        return jsonObject;
    }

    @Override
    public void loadState(JsonObject jsonObject) {
        Map<String, JsonElement> elements = jsonObject.asMap();
        numberOfPlayers = elements.get("numberOfPlayers").getAsInt();
        board = new Board(numberOfPlayers);
        players = new Player[numberOfPlayers];
        isLastRound = elements.get("isLastRound").getAsBoolean();
        currentPlayerIndex = elements.get("currentPlayerIndex").getAsInt();
        board.loadState(elements.get("board").getAsJsonObject());
        for (int i = 0; i < NUMBER_OF_COMMON_CARDS; i++)
            commonCards[i] = new CommonCard(elements.get("commonCard" + i).toString(), numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(elements.get("player" + i).toString(), List.of(commonCards));
    }


    public CommonCard[] getCommonCards(){
        return commonCards;
    }
}
