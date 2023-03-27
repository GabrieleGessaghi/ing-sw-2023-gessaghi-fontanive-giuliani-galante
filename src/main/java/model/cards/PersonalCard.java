package model.cards;

import model.Token;

/**
 * Personal objective cards.
 * @author Niccolò Giuliani
 */
public class PersonalCard extends Card {
    private Token[][] correctTiles;

    /**
     * Class constructor.
     * @author Niccolò Giuliani
     */
    public PersonalCard(Token[] correctTiles) {
        points = new int[]{1, 2, 4, 6, 9, 12};
        //TODO: Implement different personal cards.
    }

    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
