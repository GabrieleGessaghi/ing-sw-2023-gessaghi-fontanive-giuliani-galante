package model.cards;

import model.Token;

import static model.Configurations.SHELF_COLUMNS;
import static model.Configurations.SHELF_ROWS;
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
        int countCorrect = 0;
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                if(shelf[i][j] != Token.NOTHING && shelf[i][j] == correctTiles[i][j])
                    countCorrect++;
        return (countCorrect == 0 ? 0 : PERSONALCARD_POINTS[countCorrect - 1]);
    }
}
