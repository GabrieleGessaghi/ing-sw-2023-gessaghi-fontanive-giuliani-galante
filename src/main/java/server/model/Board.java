package server.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.IOUtils;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.exceptions.IllegalMoveException;
import server.controller.utilities.ConfigLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * The board on which the game is played.
 * @author Giorgio Massimo Fontanive
 */
public class Board implements Savable {
    private boolean[][] usableTiles;
    private final Token[][] tiles;
    private final Bag bag;
    private final List<Observer> observers;

    /**
     * Sets the usable tiles based on the number of players and fills the board with tokens.
     * @author Giorgio Massimo Fontanive
     * @param numberOfPlayers The number of players in this game, determines the usable tiles.
     */
    public Board(int numberOfPlayers) {
        usableTiles = new boolean[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        tiles = new Token[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        bag = new Bag();
        observers = new ArrayList<>();

        //Initializes the board's usable tiles based on number of players.
        String jsonFile = "";
        String jsonFilePath = "";
        JsonReader jsonReader;
        switch (numberOfPlayers) {
            case 2 -> jsonFilePath = "/boards/twoPlayersBoard.json";
            case 3 -> jsonFilePath = "/boards/threePlayersBoard.json";
            case 4 -> jsonFilePath = "/boards/fourPlayersBoard.json";
        }
        try {
            InputStream inputStream = ConfigLoader.class.getResourceAsStream(jsonFilePath);
            jsonFile = IOUtils.toString(inputStream);
            //jsonFile = Files.readString(Paths.get(jsonFilePath));
            jsonReader = new JsonReader(new StringReader(jsonFile));
            jsonReader.beginObject();
            jsonReader.nextName();
            usableTiles = Board.convertIntegerMatrix(JsonTools.readMatrix(jsonReader), 0);
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        reset();
    }

    /**
     * Checks whether the board needs to be filled with tokens.
     * @author Giorgio Massimo Fontanive
     * @return True if there are no tokens touching each other.
     */
    private boolean isResetNeeded() {
        boolean resetNeeded = true;
        for (int i = 0; i < tiles.length && resetNeeded; i++)
            for (int j = 0; j < tiles.length && resetNeeded; j++)
                if (usableTiles[i][j] && tiles[i][j] != Token.NOTHING && !hasAllNothingAround(i, j))
                    resetNeeded = false;
        return  resetNeeded;
    }

    /**
     * Checks whether the tile in the given position has all "nothing" surrounding it.
     * @return True if the tile in the given position has all "nothing" around.
     */
    private boolean hasAllNothingAround(int i, int j) {
        boolean allNothingAround;
        int iBorder = 0;
        int jBorder = 0;

        //Finds in which directions it cannot check for "nothing"
        if (i == 0) iBorder = 1;
        else if (i == tiles.length - 1) iBorder = -1;
        if (j == 0) jBorder = 1;
        else if (j == tiles.length - 1) jBorder = -1;

        allNothingAround = (jBorder == 1 || tiles[i][j - 1] == Token.NOTHING) &&
                (jBorder == -1 || tiles[i][j + 1] == Token.NOTHING) &&
                (iBorder == -1 || tiles[i + 1][j] == Token.NOTHING) &&
                (iBorder == 1 || tiles[i - 1][j] == Token.NOTHING);
        return allNothingAround;
    }

    /**
     * Fills the board with tokens in all available spaces.
     * @author Giorgio Massimo Fontanive
     */
    private void reset() {
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == null)
                    tiles[i][j] = Token.NOTHING;
                if (usableTiles[i][j] && tiles[i][j] == Token.NOTHING)
                    tiles[i][j] = bag.drawToken();
            }
    }

    /**
     * Checks whether the tile can be selected by the player.
     * @author Giorgio Massimo Fontanive
     * @param row The tile's row in the board.
     * @param column The tile's column in the board.
     * @return True if the tile has a free side and is not empty.
     */
    private boolean isTokenSelectable(int row, int column) {
        if (row == 0 || row == tiles.length - 1 || column == 0 || column == tiles.length - 1)
            return true;
        return usableTiles[row][column] && tiles[row][column] != Token.NOTHING && (
                tiles[row - 1][column] == Token.NOTHING ||
                        tiles[row + 1][column] == Token.NOTHING ||
                        tiles[row][column - 1] == Token.NOTHING ||
                        tiles[row][column + 1] == Token.NOTHING);
    }


    /**
     * Checks whether the chosen tiles can be selected within the rules of the game.
     * @author Giorgio Massimo Fontanive
     * @param selectedTiles A boolean matrix with true in the positions of the chosen tiles.
     * @return True if the tiles are in an available position and if they are in a line.
     */
    public boolean isMoveLegal(boolean[][] selectedTiles) {
        if (selectedTiles == null)
            return false;

        //Checks whether each tile is selectable by itself.
        boolean legal = true;
        int selectedAmount = 0;
        for (int i = 0; i < ConfigLoader.BOARD_SIZE && legal; i++)
            for (int j = 0; j < ConfigLoader.BOARD_SIZE && legal; j++)
                if (selectedTiles[i][j])
                    if (isTokenSelectable(i, j))
                        selectedAmount++;
                    else
                        legal = false;
        if (selectedAmount <= 0 || selectedAmount > 3)
            legal = false;

        //Finds the position of the top left most selected tile.
        int firstTileRow = -1;
        int firstTileColumn = -1;
        boolean isFirstFound = false;
        for (int i = 0; i < ConfigLoader.BOARD_SIZE && !isFirstFound; i++)
            for (int j = 0; j < ConfigLoader.BOARD_SIZE && !isFirstFound; j++) {
                if (selectedTiles[i][j]) {
                    firstTileRow = i;
                    firstTileColumn = j;
                    isFirstFound = true;
                }
            }

        //Checks whether the chosen tiles are in a vertical or horizontal line.
        boolean horizontalLine = true;
        boolean verticalLine = true;
        for (int i = 0; i < selectedAmount && horizontalLine; i++)
            if (!selectedTiles[firstTileRow][firstTileColumn + i])
                horizontalLine = false;
        for (int i = 0; i < selectedAmount && verticalLine; i++)
            if (!selectedTiles[firstTileRow + i][firstTileColumn])
                verticalLine = false;

        return legal && (horizontalLine || verticalLine);
    }

    /**
     * Getter for the tiles' matrix.
     * @author Giorgio Massimo Fontanive
     * @return The tiles' matrix.
     */
    public Token[][] getTiles() {
        return tiles;
    }

    /**
     * Converts the selected tiles into an ordered array of tokens.
     * @author Giorgio Massimo Fontanive
     * @throws IllegalMoveException When a move breaks the rules (check isMoveLegal).
     * @param selectedTiles A matrix with -1 for the tiles not chosen and
     *                      the order of choice for the other ones.
     * @return An array with the select tiles in order.
     */
    public Token[] selectTiles(int[][] selectedTiles) throws IllegalMoveException {
        Token[] selectedTokens = new Token[ConfigLoader.MAX_TOKENS_PER_TURN];
        boolean[][] selectedTilesBoolean;
        Arrays.fill(selectedTokens, Token.NOTHING);
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (selectedTiles[i][j] != -1)
                    selectedTokens[selectedTiles[i][j]] = tiles[i][j];
        selectedTilesBoolean = Board.convertIntegerMatrix(selectedTiles, -1);
        if (isMoveLegal(selectedTilesBoolean))
            return selectedTokens;
        else {
            String errorMessage = "This combination of tiles is forbidden!";
            throw new IllegalMoveException(errorMessage);
        }
    }

    /**
     * Empties the board of the selected tiles.
     * @author Giorgio Massimo Fontanive
     * @param selectedTiles A boolean matrix with true in the positions of the tiles to be emptied.
     */
    public void removeTiles(boolean[][] selectedTiles) {
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (selectedTiles[i][j])
                    tiles[i][j] = Token.NOTHING;
        if (isResetNeeded())
            reset();
    }

    /**
     * Converts a matrix of integers into booleans.
     * @author Giorgio Massimo Fontanive
     * @param selectedTiles A matrix with -1 for the tiles not chosen.
     * @param exclusionNumber The number which sets the tile to false.
     * @return A matrix with true in the position of the chosen tiles.
     */
    public static boolean[][] convertIntegerMatrix(int[][] selectedTiles, int exclusionNumber) {
        boolean[][] convertedSelection = new boolean[selectedTiles.length][selectedTiles.length];
        for (int i = 0; i < selectedTiles.length; i++)
            for (int j = 0; j < selectedTiles[i].length; j++)
                if (selectedTiles[i][j] != exclusionNumber)
                    convertedSelection[i][j] = true;
        return convertedSelection;
    }

    @Override
    public JsonObject getState() {
        JsonObject jsonObject = new JsonObject();

        //Converts the matrices into integer ones.
        int[][] tilesInteger = new int[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        int[][] usableTilesInteger = new int[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        for (int i = 0; i < ConfigLoader.BOARD_SIZE; i++)
            for (int j = 0; j < ConfigLoader.BOARD_SIZE; j++) {
                tilesInteger[i][j] = tiles[i][j].ordinal();
                usableTilesInteger[i][j] = usableTiles[i][j] ? 1 : 0;
            }

        jsonObject.add("tiles", JsonTools.createJsonMatrix(tilesInteger));
        jsonObject.add("usableTiles", JsonTools.createJsonMatrix(usableTilesInteger));
        return jsonObject;
    }

    @Override
    public void loadState(JsonObject jsonObject) {
        Map<String, JsonElement> elements = jsonObject.asMap();
        try {
            int[][] tilesInteger = JsonTools.readMatrix(elements.get("tiles").getAsJsonArray());
            int[][] usableTilesInteger = JsonTools.readMatrix(elements.get("usableTiles").getAsJsonArray());
            Token[] tokenValues = Token.values();
            for (int i = 0; i < ConfigLoader.BOARD_SIZE; i++)
                for (int j = 0; j < ConfigLoader.BOARD_SIZE; j++) {
                    tiles[i][j] = tokenValues[tilesInteger[i][j]];
                    usableTiles[i][j] = usableTilesInteger[i][j] == 1;
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
