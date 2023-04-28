package server.model.cards;

import server.model.Token;

/**
 * Each card calculates the number of points a given matrix of tokens will obtain.
 * @author Niccolò Giuliani
 */
public abstract class Card {
    /**
     * Returns the number of points the given shelf will get.
     * @author Niccolò Giuliani
     * @param shelf A matrix of Tokens taken from a player's shelf.
     * @return The number of points this shelf would get.
     */
    public abstract int getPoints(Token[][]shelf);
}
