package model;

/**
 * The board on which the game is played.
 * @author Giorgio Massimo Fontanive
 */
public class Board {
    private final static int BOARD_SIZE = 9;
    private boolean[][] usableTiles;
    private Token[][] tiles;
    private Bag bag;

    public Board(int numberOfPlayers) {
        usableTiles = new boolean[BOARD_SIZE][BOARD_SIZE];
        tiles = new Token[BOARD_SIZE][BOARD_SIZE];
        bag = new Bag();

        //Create usable tiles

        reset();
    }

    private void reset() {
        for (int i = 0; i < usableTiles.length; i++)
            for (int j = 0; j < usableTiles.length; j++)
                if (usableTiles[i][j] && tiles[i][j] == Token.NOTHING)
                    tiles[i][j] = bag.drawToken();
    }

    private boolean isResetNeeded() {
        boolean resetNeeded = true;
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (usableTiles[i][j] &&
                tiles[i - 1][j] != Token.NOTHING ||
                tiles[i + 1][j] != Token.NOTHING ||
                tiles[i][j - 1] != Token.NOTHING ||
                tiles[i][j + 1] != Token.NOTHING)
                    resetNeeded = false;
        return  resetNeeded;
    }

    private boolean isMoveLegal(boolean[][] selectedTiles) {
        boolean legal = true;
        int selectedAmount = 0;

        for (int i = 0; i < tiles.length && legal; i++)
            for (int j = 0; j < tiles.length && legal; j++)
                if (selectedTiles[i][j])
                    if (!isTokenSelectable(i, j))
                        legal = false;
                    else
                        selectedAmount++;
        if (selectedAmount <= 0 || selectedAmount > 3)
            legal = false;



        return legal;
    }

    private boolean isTokenSelectable(int row, int column) {
        return usableTiles[row][column] &&
                tiles[row - 1][column] == Token.NOTHING ||
                tiles[row + 1][column] == Token.NOTHING ||
                tiles[row][column - 1] == Token.NOTHING ||
                tiles[row][column + 1] == Token.NOTHING;
    }

    public Token[][] getTiles() {
        return tiles;
    }

    public Token[] selectTiles(int[][] selectedTiles) {
        return null;
    }

    public void removeTiles(boolean[][] selectedTiles) {
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (selectedTiles[i][j])
                    tiles[i][j] = Token.NOTHING;
    }
}
