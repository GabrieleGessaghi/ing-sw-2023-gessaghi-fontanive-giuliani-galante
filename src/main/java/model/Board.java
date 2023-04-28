package model;

import com.google.gson.stream.JsonReader;
import controller.observer.Event;
import controller.observer.Observer;
import controller.utilities.JsonTools;
import controller.observer.Observable;
import model.exceptions.IllegalMoveException;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static controller.utilities.ConfigLoader.MAX_TOKENS_PER_TURN;
import static controller.utilities.ConfigLoader.BOARD_SIZE;

/**
 * The board on which the game is played.
 * @author Giorgio Massimo Fontanive
 */
public class Board implements Observable, Saveable {
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
        usableTiles = new boolean[BOARD_SIZE][BOARD_SIZE];
        tiles = new Token[BOARD_SIZE][BOARD_SIZE];
        bag = new Bag();
        observers = new ArrayList<>();

        //Initializes the board's usable tiles based on number of players.
        String jsonFile = "";
        String jsonFilePath = "";
        JsonReader jsonReader;
        switch (numberOfPlayers) {
            case 2 -> jsonFilePath = "src/main/resources/Boards/twoPlayersBoard.json";
            case 3 -> jsonFilePath = "src/main/resources/Boards/threePlayersBoard.json";
            case 4 -> jsonFilePath = "src/main/resources/Boards/fourPlayersBoard.json";
        }
        try {
            jsonFile = Files.readString(Paths.get(jsonFilePath));
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
                if (usableTiles[i][j] && (
                        tiles[i - 1][j] != Token.NOTHING ||
                                tiles[i + 1][j] != Token.NOTHING ||
                                tiles[i][j - 1] != Token.NOTHING ||
                                tiles[i][j + 1] != Token.NOTHING))
                    resetNeeded = false;
        return  resetNeeded;
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
     * @author Giorgio Massimo Fontainve
     * @param row The tile's row in the board.
     * @param column The tile's column in the board.
     * @return True if the tile has a free side and is not empty.
     */
    private boolean isTokenSelectable(int row, int column) {
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
    private boolean isMoveLegal(boolean[][] selectedTiles) {

        //Checks whether each tile is selectable by itself.
        boolean legal = true;
        int selectedAmount = 0;
        for (int i = 0; i < tiles.length && legal; i++)
            for (int j = 0; j < tiles.length && legal; j++)
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
        for (int i = 0; i < tiles.length && !isFirstFound; i++)
            for (int j = 0; j < tiles.length && !isFirstFound; j++) {
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
        Token[] selectedTokens = new Token[MAX_TOKENS_PER_TURN];
        boolean[][] selectedTilesBoolean;
        Arrays.fill(selectedTokens, Token.NOTHING);
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (selectedTiles[i][j] != -1)
                    selectedTokens[selectedTiles[i][j]] = tiles[i][j];
        selectedTilesBoolean = Board.convertIntegerMatrix(selectedTiles, -1);
        if (isMoveLegal(selectedTilesBoolean))
            return selectedTokens;
        else
            throw new IllegalMoveException("This combination of tiles is forbidden!");
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
        int[][] tilesInteger = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] usableTilesInteger = new int[BOARD_SIZE][BOARD_SIZE];

        //Converts the matrices into integer ones.
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++) {
                tilesInteger[i][j] = tiles[i][j].ordinal();
                usableTilesInteger[i][j] = usableTiles[i][j] ? 1 : 0;
            }

        elements.put("tiles", JsonTools.createJsonMatrix(tilesInteger));
        elements.put("usableTiles", JsonTools.createJsonMatrix(usableTilesInteger));
        return JsonTools.createJson(elements);
    }

    @Override
    public void loadState(String jsonMessage) {
        Map<String, Object> elements = JsonTools.parseJson(jsonMessage);
        try {
            int[][] tilesInteger = JsonTools.readMatrix((JsonReader) elements.get("tiles"));
            int[][] usableTilesInteger = JsonTools.readMatrix((JsonReader) elements.get("usableTiles"));

            //Converts the integer matrices into usable ones.
            Token[] tokenValues = Token.values();
            for (int i = 0; i < BOARD_SIZE; i++)
                for (int j = 0; j < BOARD_SIZE; j++) {
                    tiles[i][j] = tokenValues[tilesInteger[i][j]];
                    usableTiles[i][j] = usableTilesInteger[i][j] == 1;
                }

        } catch (IOException e) {
            String errorMessage = "Failed to load Board's save state!";
            updateObservers(new Event(JsonTools.createMessage(errorMessage)));
            throw new RuntimeException(e);
        }
    }
}
