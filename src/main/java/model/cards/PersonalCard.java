package model.cards;

import model.Token;

/**
 * Personal objective cards.
 * @author Niccolò Galante
 */
public class PersonalCard extends Card {
    private static final int[] points = new int[]{1, 2, 4, 6, 9, 12};
    private Token[][] correctTiles;

    /**
     * Class constructor.
     * @author Niccolò Galante
     */
    public PersonalCard(Token[] correctTiles) {
        //TODO: Implement different personal cards.
    }

    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
