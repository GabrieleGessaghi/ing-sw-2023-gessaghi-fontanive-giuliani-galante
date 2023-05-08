package server.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.cards.CommonCard;
import server.model.cards.CommonType;
import server.model.exceptions.IllegalColumnException;
import server.model.exceptions.IllegalMoveException;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static server.controller.utilities.ConfigLoader.NUMBER_OF_COMMON_CARDS;

public class Game implements Savable, Observable {
    private final String gameID;
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
            gameID = String.format("%04d", code);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        commonCards = genCommonCard(numberOfPlayers);

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
    private CommonCard[] genCommonCard (int numberOfPlayers){
        ArrayList<CommonType> types = new ArrayList<CommonType>(Arrays.asList(CommonType.values()));
        Collections.shuffle(types);
        commonCards = new CommonCard[NUMBER_OF_COMMON_CARDS];
        for (int i = 0; i < NUMBER_OF_COMMON_CARDS; i++)
            commonCards[i] = new CommonCard(types.get(i), numberOfPlayers);
        return commonCards;
    }

    /**
     * Save the game state in case of disconnections or other problems.
     * @author Gabriele Gessaghi
     * @throws IOException When there's an error in the file creation.
     */
    private void saveGame() throws IOException {
        String fileName = String.format("/src/data/game-%s.txt",gameID);
        FileOutputStream fOut = new FileOutputStream(new File(fileName));

        fOut.close();
    }

    /**
     * Reload a saved state of a previous game.
     * @author Gabriele Gessaghi
     */
    public void loadGame(String fileName) throws FileNotFoundException {
        try {
            FileInputStream fIn = new FileInputStream(fileName);

        } catch (FileNotFoundException e){
            String errorMessage = "Game files not found!";
            System.out.println(errorMessage);
            updateObservers(new Event(JsonTools.createMessage(errorMessage)));
        }
    }

    /**
     * Collect the selected tiles from the game board and update the current player shelf.
     * @author Gabriele Gessaghi
     * @param selectedTiles A matrix with -1 for the tiles not chosen and
     *                      the order of choice for the other ones.
     * @param column The column on which the player wants to put the new tiles.
     */
    public void playerTurn (int [][] selectedTiles, int column) throws IllegalMoveException, IllegalColumnException{
        if (players[currentPlayerIndex].isConnected) {

            //Creates the message sent at the beginning of the turn
            JsonObject jsonMessage = new JsonObject();
            jsonMessage.addProperty("currentPlayerNickname", players[currentPlayerIndex].getNickname());

            //Gets the tiles from the board.
            Token[] selectedTokens;
            selectedTokens = board.selectTiles(selectedTiles);

            //Inserts the tokens in the shelf.
            players[currentPlayerIndex].insertTokens(selectedTokens, column);

            //Removes tiles from the board.
            boolean[][] isSelected = Board.convertIntegerMatrix(selectedTiles, -1);
            board.removeTiles(isSelected);

            if (players[currentPlayerIndex].isShelfFull()) {
                isLastRound = true;
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", "This is the last round.\n" +
                        players[currentPlayerIndex].getNickname() + " has filled their shelf!");
            }
        }

        currentPlayerIndex++;
        if (currentPlayerIndex == players.length)
            if (!isLastRound)
                currentPlayerIndex = 0;
            else
                isGameFinished = true;
        //Save Game
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
        switch (view) {
            case BOARD -> updateObservers(new Event(board.getState().toString()));
            case CURRENT_PLAYER -> updateObservers(new Event(players[currentPlayerIndex].getState().toString())); //TODO: Show the personal card as well
            case CHAT -> {}
            case PLAYERS_POINTS -> {
                JsonObject jsonObject = new JsonObject();
                for (int i = 0; i < players.length; i++) {
                    jsonObject.addProperty("player" + i, players[i].getNickname());
                    jsonObject.addProperty("points" + i, players[i].getPoints());
                }
                updateObservers(new Event(jsonObject.toString()));
            }
            case COMMON_CARDS -> {
                for (CommonCard card : commonCards)
                    updateObservers(new Event(card.getState().toString()));
            }
        }
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
        for (Player player : players)
            player.registerObserver(observer);
        for (CommonCard commonCard : commonCards)
            commonCard.registerObserver(observer);
        board.registerObserver(observer);
    }

    @Override
    public void updateObservers(Event event) {
        for (Observer observer : observers)
            if (observer != null)
                observer.update(event);
    }

    @Override
    public JsonObject getState() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("numberOfPlayers", numberOfPlayers);
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
        currentPlayerIndex = elements.get("currentPlayerIndex").getAsInt();
        board.loadState(elements.get("board").getAsJsonObject());
        for (int i = 0; i < NUMBER_OF_COMMON_CARDS; i++)
            commonCards[i] = new CommonCard(elements.get("commonCard" + i).toString(), numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(elements.get("player" + i).toString(), List.of(commonCards));
    }
}
