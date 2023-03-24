package model.cards;

import model.Token;

/**
 @author Niccolò Giuliani
 personal objective cards
 */
public class PersonalCard extends Card {

    private Token[][] correctTiles;
    /**
     @author Niccolò Giuliani
     constructor
     */
    public PersonalCard(Token[] correctTiles) {

    }

    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
