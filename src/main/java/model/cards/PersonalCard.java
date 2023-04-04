package model.cards;

import model.Token;
import static model.Configurations.SHELF_COLUMNS;
import static model.Configurations.SHELF_ROWS;

/**
 * Personal objective cards.
 * @author Niccolò Galante
 */
public class PersonalCard extends Card {
    //TODO: Move to configuration file (Giorgio)
    private static final int[] points = new int[]{1, 2, 4, 6, 9, 12};
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

        for(int i=0; i<SHELF_ROWS; i++)
            for(int j=0; j<SHELF_COLUMNS; j++)
                if(shelf[i][j] != Token.NOTHING &&shelf[i][j] == correctTiles[i][j])
                    countCorrect++;

        if(countCorrect == 1)
            return 1;
        else if(countCorrect == 2)
            return 2;
        else if(countCorrect == 3)
            return 4;
        else if(countCorrect == 4)
            return 6;
        else if(countCorrect == 5)
            return 9;
        else if(countCorrect == 6)
            return 12;

        return 0;
    }
}
