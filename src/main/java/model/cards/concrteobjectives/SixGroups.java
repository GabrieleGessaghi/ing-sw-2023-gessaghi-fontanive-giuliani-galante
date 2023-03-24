package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Six groups each containing at least
 * 2 tiles of the same type (not necessarily
 * in the depicted shape).
 * The tiles of one group can be different
 * from those of another group.
 */
public class SixGroups implements CommonObjective {

    @Override
    public int getPoints(Token[][] shelf) {
        int counter = 0;

        for (int i = 0; i < ROWS && counter < 6; i++){
            for (int j = 0; j < COLUMNS ; j++){
                if(shelf[i][j] != Token.NOTHING) {
                    if (shelf[i][j] == shelf[i][j + 1])
                        counter++;
                    if (shelf[i][j] == shelf[i + 1][j])
                        counter++;
                }
            }
        }
        if(counter > 6)
            return 1;
        else
            return 0;
    }
}
