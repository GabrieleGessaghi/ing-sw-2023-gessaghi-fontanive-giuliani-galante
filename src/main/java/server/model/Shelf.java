package server.model;

import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.exceptions.FullColumnException;
import server.controller.utilities.ConfigLoader;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Shelf implements Savable, Observable {
    private final Token[][] tiles;
    private final List<Observer> observers;

    /**
     * Class constructor, all the shelf positions are set to NOTHING token type.
     * @author Gabriele Gessaghi
     */
    public Shelf() {
        tiles = new Token[ConfigLoader.SHELF_ROWS][ConfigLoader.SHELF_COLUMNS];
        observers = new ArrayList<>();
        for (Token[] row: tiles)
            Arrays.fill(row, Token.NOTHING);
    }

    /**
     * Getter for the current state of the shelf.
     * @author Gabriele Gessaghi
     * @return The tiles matrix of Tokens.
     */
    public Token[][] getTiles() {
        return tiles;
    }

    /**
     * Insert the new token inside the first free cell of the selected column.
     * @author Gabriele Gessaghi
     * @param token Token to be inserted inside the shelf.
     * @param column Column where the token has to bbe inserted.
     */
    public void insertToken(Token token, int column) throws FullColumnException {
        for (int i = ConfigLoader.SHELF_ROWS-1; i>=0 ; i--)
            if (tiles[i][column].equals((Token.NOTHING))) {
                tiles[i][column] = token;
                return;
            }
        throw new FullColumnException("The selected column is full!");
    }

    /**
     * Remove the last inserted token of a given column.
     * @author Gabriele Gessaghi
     * @param column Column where the token has to be inserted.
     */
    public void removeToken(int column) {
        for (int i = 0; i < ConfigLoader.SHELF_ROWS; i++)
            if (!tiles[i][column].equals((Token.NOTHING))) {
                tiles[i][column] = Token.NOTHING;
                return;
            }
    }

    /**
     * Check if there is a NOTHING token inside the shelf (an empty space).
     * @author Gabriele Gessaghi
     * @return true only if NOTHING token type is not present in the shelf.
     */
    public boolean isFull() {
        for (Token[] row: tiles)
            if (Arrays.asList(row).contains(Token.NOTHING))
                return false;
        return true;
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

        //Converts the matrices into integer ones.
        int[][] tilesInteger = new int[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        for (int i = 0; i < ConfigLoader.BOARD_SIZE; i++)
            for (int j = 0; j < ConfigLoader.BOARD_SIZE; j++)
                tilesInteger[i][j] = tiles[i][j].ordinal();

        elements.put("shelfTiles", JsonTools.createJsonMatrix(tilesInteger));
        return JsonTools.createJson(elements);
    }

    @Override
    public void loadState(String jsonMessage) {
        Map<String, Object> elements = JsonTools.parseJson(jsonMessage);
        try {

            //Converts the integer matrices into usable ones.
            int[][] tilesInteger = JsonTools.readMatrix((JsonReader) elements.get("shelfTiles"));
            Token[] tokenValues = Token.values();
            for (int i = 0; i < ConfigLoader.BOARD_SIZE; i++)
                for (int j = 0; j < ConfigLoader.BOARD_SIZE; j++)
                    tiles[i][j] = tokenValues[tilesInteger[i][j]];

        } catch (IOException e) {
            String errorMessage = "Failed to load a Shelf's save state!";
            updateObservers(new Event(JsonTools.createMessage(errorMessage)));
            throw new RuntimeException(e);
        }
    }
}
