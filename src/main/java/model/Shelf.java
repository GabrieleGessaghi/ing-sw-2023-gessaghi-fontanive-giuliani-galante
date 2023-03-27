package model;

import model.exceptions.FullColumnException;

import java.util.Arrays;

public class Shelf {

    private final static int COLUMNS = 5;
    private final static int ROWS = 6;
    private Token [][] tiles;

    /**
     * Class constructor, all the shelf positions are set to NOTHING token type
     * @author Gabriele Gessaghi
     */
    public Shelf () {
        tiles = new Token[ROWS][COLUMNS];
        for ( Token[] row: tiles)
            Arrays.fill(row, Token.NOTHING);
    }

    /**
     * Return the current state of the shelf
     * @author Gabriele Gessaghi
     */
    public Token [][] getTiles () { return tiles;}

    /**
     * Insert the new token inside the first free cell of the selected column
     * @author Gabriele Gessaghi
     * @param token token to be inserted inside the shelf
     * @param column column where the token has to bbe inserted
     */
    public void insertToken (Token token, int column) throws FullColumnException {
        for (int i=0; i<ROWS; i++)
            if (tiles[i][column].equals((Token.NOTHING))) {
                tiles[i][column] = token;
                return;
            }
        throw new FullColumnException("The selected column is full!");
    }

    /**
     * Remove the last inserted token of a given column
     * @author Gabriele Gessaghi
     * @param column column where the token has to bbe inserted
     */
    public void removeToken (int column) {
        for (int i=ROWS; i>=0; i--)
            if (!tiles[i][column].equals((Token.NOTHING))) {
                tiles[i][column] = Token.NOTHING;
                return;
            }
    }

    /**
     * Check if there is a NOTHING token inside the shelf (an empty space)
     * @author Gabriele Gessaghi
     * @return true only if NOTHING token type is not present in the shelf
     */
    public boolean isFull () {
        for ( Token[] row: tiles)
            if (Arrays.stream(row).anyMatch(tmp -> tmp.equals(Token.NOTHING)))
                return false;

        return true;
    }
}
