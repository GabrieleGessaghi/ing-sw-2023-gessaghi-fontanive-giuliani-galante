package model.cards;

import model.Token;

/**
 * Personal objective cards.
 * @author Niccolò Giuliani
 */
public class PersonalCard extends Card {
    private static final int[] points = new int[]{1, 2, 4, 6, 9, 12};
    private Token[][] correctTiles;

    /**
     * Class constructor.
     * @author Niccolò Giuliani
     */
    public PersonalCard(Token[] correctTiles) {
        //TODO: Implement different personal cards.
    }

    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
