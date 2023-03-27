package model.cards;

import model.Token;

/**
 *  general card of the game
 *  @author Niccolò Giuliani
 */
public abstract class Card {

    protected int points[];

    /**
     * method to know the points
     * @author Niccolò Giuliani
     * @param shelf personal shelf of the player
     * @return points
     */
    public abstract int getPoints(Token[][]shelf);



}
