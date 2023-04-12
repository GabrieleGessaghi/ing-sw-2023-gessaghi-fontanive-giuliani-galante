package model;

import model.exceptions.FullColumnException;

import java.io.Serializable;
import java.util.Arrays;
import static controller.Configurations.SHELF_COLUMNS;
import static controller.Configurations.SHELF_ROWS;

public class Shelf implements Serializable {
    private final Token [][] tiles;

    /**
     * Class constructor, all the shelf positions are set to NOTHING token type.
     * @author Gabriele Gessaghi
     */
    public Shelf() {
        tiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
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
        for (int i = SHELF_ROWS-1; i>=0 ; i--)
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
        for (int i = 0; i < SHELF_ROWS; i++)
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
}
