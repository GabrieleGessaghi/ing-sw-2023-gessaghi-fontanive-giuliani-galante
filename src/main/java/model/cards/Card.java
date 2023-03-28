package model.cards;

import model.Token;

/**
 * Each card calculates the number of points a given matrix of tokens will obtain.
 * @author Niccolò Giuliani
 */
public abstract class Card {
    /**
     * Returns the number of points the given shelf will get.
     * @author Niccolò Giuliani
     * @param shelf a matrix of Tokens taken from a player's shelf.
     * @return the number of points this shelf will get.
     */
    public abstract int getPoints(Token[][]shelf);
}
