package model.cards;

import model.Token;

/**
 @author Niccolò Giuliani
 */
public abstract class Card {
    protected int points[];
    public abstract int getPoints(Token[][]shelf);



}
