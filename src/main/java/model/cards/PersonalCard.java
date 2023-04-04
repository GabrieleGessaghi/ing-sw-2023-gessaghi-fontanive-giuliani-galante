package model.cards;

import model.Token;

import static model.Configurations.PERSONALCARD_POINTS;

/**
 * Personal objective cards.
 * @author Niccolò Galante
 */
public class PersonalCard extends Card {
    private Token[][] correctTiles;

    /**
     * Class constructor.
     * @author Niccolò Galante
     */
    public PersonalCard(Token[] correctTiles) {
        //TODO: Implement different personal cards through Json
    }

    public int getPoints(Token[][] shelf) {
        //TODO: Implement the method (use PERSONALCARD_POINTS[])
        return 0;
    }
}
