package model.cards;

import model.Token;

/**
 * @author Niccolò Giuliani
 * general card of the game
 */
public abstract class Card {

    protected int points[];

    /**
     * @author Niccolò Giuliani
     * @param shelf
     * method to know the points
     */
    public abstract int getPoints(Token[][]shelf);



}
