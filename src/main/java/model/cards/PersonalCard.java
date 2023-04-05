package model.cards;

import model.Token;

import java.io.Serializable;

import static model.Configurations.SHELF_COLUMNS;
import static model.Configurations.SHELF_ROWS;
import static model.Configurations.PERSONALCARD_POINTS;

/**
 * Personal objective cards.
 * @author Niccolò Galante
 */
public class PersonalCard extends Card implements Serializable {
    private Token[][] correctTiles;

    /**
     * Class constructor.
     * @author Niccolò Galante
     */
    public PersonalCard(int index) {
        //TODO: Implement different personal cards through Json
    }

    /**
     *
     * @author Niccolò Galante
     * @param shelf A matrix of Tokens taken from a player's shelf.
     * @return
     */
    public int getPoints(Token[][] shelf) {
        int countCorrect = 0;
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                if(shelf[i][j] != Token.NOTHING && shelf[i][j] == correctTiles[i][j])
                    countCorrect++;
        return (countCorrect == 0 ? 0 : PERSONALCARD_POINTS[countCorrect - 1]);
    }
}
