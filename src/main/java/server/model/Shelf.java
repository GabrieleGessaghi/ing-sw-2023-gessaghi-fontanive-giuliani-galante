package server.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import server.controller.utilities.JsonTools;
import server.model.exceptions.IllegalColumnException;
import server.controller.utilities.ConfigLoader;

import java.io.IOException;
import java.util.*;

public class Shelf implements Savable {
    private final Token[][] tiles;

    /**
     * Class constructor, all the shelf positions are set to NOTHING token type.
     * @author Gabriele Gessaghi
     */
    public Shelf() {
        tiles = new Token[ConfigLoader.SHELF_ROWS][ConfigLoader.SHELF_COLUMNS];
        for (Token[] row: tiles)
            Arrays.fill(row, Token.NOTHING);
    }

    public Shelf(String jsonState) {
        tiles = new Token[ConfigLoader.SHELF_ROWS][ConfigLoader.SHELF_COLUMNS];
        loadState(JsonParser.parseString(jsonState).getAsJsonObject());
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
    public void insertToken(Token token, int column) throws IllegalColumnException {
        for (int i = ConfigLoader.SHELF_ROWS-1; i>=0 ; i--)
            if (tiles[i][column].equals((Token.NOTHING))) {
                tiles[i][column] = token;
                return;
            }
        throw new IllegalColumnException("The selected column is full!");
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
    public JsonObject getState() {
        JsonObject jsonObject = new JsonObject();

        //Converts the matrices into integer ones.
        int[][] tilesInteger = new int[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        for (int i = 0; i < ConfigLoader.BOARD_SIZE; i++)
            for (int j = 0; j < ConfigLoader.BOARD_SIZE; j++)
                tilesInteger[i][j] = tiles[i][j].ordinal();

        jsonObject.add("shelfTiles", JsonTools.createJsonMatrix(tilesInteger));
        return jsonObject;
    }

    @Override
    public void loadState(JsonObject jsonObject) {
        Map<String, JsonElement> elements = jsonObject.asMap();
        try {
            int[][] tilesInteger = JsonTools.readMatrix(elements.get("shelfTiles").getAsJsonArray());
            Token[] tokenValues = Token.values();
            for (int i = 0; i < ConfigLoader.BOARD_SIZE; i++)
                for (int j = 0; j < ConfigLoader.BOARD_SIZE; j++)
                    tiles[i][j] = tokenValues[tilesInteger[i][j]];

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
