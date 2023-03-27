package model.cards;

import model.Token;

/**
 * Personal objective cards
 * @author Niccolò Giuliani
 */
public class PersonalCard extends Card {
    private Token[][] correctTiles;

    /**
     * constructor
     * @author Niccolò Giuliani
     */
    public PersonalCard(Token[] correctTiles) {
        //TODO: Implement different personal cards.
    }

    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
