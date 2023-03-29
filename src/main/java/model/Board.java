package model;

import com.google.gson.Gson;
import model.Bag;
import model.Token;
import model.exceptions.IllegalMoveException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The board on which the game is played.
 * @author Giorgio Massimo Fontanive
 */
public class Board {
    private final static int BOARD_SIZE = 9;
    private final static int MAX_TOKENS_PER_TURN = 3;
    private boolean[][] usableTiles;
    private final Token[][] tiles;
    private final Bag bag;

    /**
     * Sets the usable tiles based on the number of players and fills the board with tokens.
     * @author Giorgio Massimo Fontanive
     * @param numberOfPlayers the number of players in this game, determines the usable tiles.
     */
    public Board(int numberOfPlayers) {
        usableTiles = new boolean[BOARD_SIZE][BOARD_SIZE];
        tiles = new Token[BOARD_SIZE][BOARD_SIZE];
        bag = new Bag();

        //Initialized the board's usable tiles based on number of players
        String jsonFile = "";
        String jsonFilePath = "";
        Gson gson = new Gson();
        switch (numberOfPlayers) {
            case 2 -> jsonFilePath = "/src/main/resources/boards/twoplayersboard.json";
            case 3 -> jsonFilePath = "/src/main/resources/boards/threeplayersboard.json";
            case 4 -> jsonFilePath = "/src/main/resources/boards/fourplayersboard.json";
        }
        try {
            jsonFile = Files.readString(Paths.get(jsonFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        usableTiles = gson.fromJson(jsonFile, usableTiles.getClass());

        reset();
    }

    /**
     * Checks whether the board needs to be filled with tokens.
     * @author Giorgio Massimo Fontanive
     * @return true if there are no tokens touching each other.
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
        for (int i = 0; i < usableTiles.length; i++)
            for (int j = 0; j < usableTiles.length; j++)
                if (usableTiles[i][j] && tiles[i][j] == Token.NOTHING)
                    tiles[i][j] = bag.drawToken();
    }

    /**
     * Checks whether the tile can be selected by the player.
     * @author Giorgio Massimo Fontainve
     * @param row the tile's row in the board.
     * @param column the tile's column in the board.
     * @return true if the tile has a free side and is not empty.
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
     * @param selectedTiles a boolean matrix with true in the positions of the chosen tiles.
     * @return true if the tiles are in an available position and if they are in a line.
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

        //Checks wether the chosen tiles are in a vertical or horizontal line.
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
     * @return the tiles' matrix.
     */
    public Token[][] getTiles() {
        return tiles;
    }

    /**
     * Converts the selected tiles into an ordered array of tokens.
     * @throws IllegalMoveException when a move breaks the rules (check isMoveLegal).
     * @param selectedTiles a matrix with -1 for the tiles not chosen and
     *                      the order of choice for the other ones.
     * @return an array with the select tiles in order.
     */
    public Token[] selectTiles(int[][] selectedTiles) throws IllegalMoveException {
        Token[] selectedTokens = new Token[MAX_TOKENS_PER_TURN];
        boolean[][] selectedTilesBoolean = new boolean[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (selectedTiles[i][j] != -1) {
                    selectedTokens[selectedTiles[i][j]] = tiles[i][j];
                    selectedTilesBoolean[i][j] = true;
                }
        if (isMoveLegal(selectedTilesBoolean))
            return selectedTokens;
        else
            throw new IllegalMoveException("This combination of tiles is forbidden!");
    }

    /**
     * Empties the board of the selected tiles.
     * @param selectedTiles a boolean matrix with true in the positions of the tiles to be emptied.
     */
    public void removeTiles(boolean[][] selectedTiles) {
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (selectedTiles[i][j])
                    tiles[i][j] = Token.NOTHING;
        if (isResetNeeded())
            reset();
    }
}
