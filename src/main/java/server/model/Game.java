package server.model;

import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.cards.Card;
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
    private String gameID;
    private int currentPlayerIndex;
    private int numberOfPlayers;
    private Board board;
    private Player[] players;
    private CommonCard[] commonCards;
    private final List<Observer> observers;

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
        while(randomNumbers.size()<3){
            // using System.currentTimeMillis() as Random seeds for consistency in random generation of the numbers
            randomNumbers.add(new Random(System.currentTimeMillis()).nextInt(12));
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
     * Collect the selected tiles from the game board and update the current player shelf.
     * @author Gabriele Gessaghi
     * @param selectedTiles A matrix with -1 for the tiles not chosen and
     *                      the order of choice for the other ones.
     * @param column The column on which the player wants to put the new tiles.
     */
    public void playerTurn (int [][] selectedTiles, int column) {
        //TODO: Throw exceptions when input is wrong
        if (players[currentPlayerIndex].isConnected) {

            //Gets the tiles from the board.
            Token[] selectedTokens;
            try {
                selectedTokens = board.selectTiles(selectedTiles);
            } catch (IllegalMoveException e1){
                System.out.println("The selected tokens are not valid!");
                return;
            }

            //Inserts the tokens in the shelf.
            try {
                players[currentPlayerIndex].insertTokens(selectedTokens, column);
            } catch (IllegalColumnException e2) {
                System.out.println("The selected column can't store all the selected tokens!");
                return;
            }

            //Removes tiles from the board.
            boolean[][] isSelected = new boolean[selectedTiles.length][selectedTiles[0].length];
            isSelected = Board.convertIntegerMatrix(selectedTiles, -1);
            board.removeTiles(isSelected);
        }
        currentPlayerIndex++;
    }

    /**
     * Return the winner if the game is over.
     * @author Gabriele Gessaghi
     * @return The nickname of the winner or null if not over yet.
     */
    public String endGame() { return null;}

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

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void updateObservers(Event event) {
        for (Observer observer : observers)
            if (observer != null)
                observer.update(event);
    }

    @Override
    public String getState() {
        Map<String, Object> elements = new HashMap<>();
        elements.put("numberOfPlayers", numberOfPlayers);
        elements.put("currentPlayerIndex", currentPlayerIndex);
        elements.put("currentPlayerNickname", players[currentPlayerIndex].getNickname());
        elements.put("board", JsonTools.createJson(JsonTools.parseJson(board.getState())));
        for (int i = 0; i < players.length; i++)
            elements.put("player" + i, JsonTools.createJson(JsonTools.parseJson(players[i].getState())));
        for (int i =0; i < commonCards.length; i++)
            elements.put("commonCard" + i, JsonTools.createJson(JsonTools.parseJson(commonCards[i].getState())));
        return JsonTools.createJson(elements).toString();
    }

    @Override
    public void loadState(String jsonMessage) {
        Map<String, Object> elements;
        elements = JsonTools.parseJson(jsonMessage);
        numberOfPlayers = (Integer) elements.get("numberOfPlayers");
        currentPlayerIndex = (Integer) elements.get("currentPlayerIndex");
        board = new Board(numberOfPlayers);
        board.loadState(elements.get("board").toString());
        players = new Player[numberOfPlayers];
        for (int i = 0; i < NUMBER_OF_COMMON_CARDS; i++)
            commonCards[i] = new CommonCard(elements.get("commonCard" + i).toString());
        for (int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(elements.get("player" + i).toString(), List.of(commonCards));
    }
}
