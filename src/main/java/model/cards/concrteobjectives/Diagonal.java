package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Five tiles of the same type forming a
 * diagonal.
 */
public class Diagonal implements CommonObjective {

    @Override
    public int getPoints(Token[][] shelf) {
        int flag = 0;
        int i = 0;
        for (int k = 0; k < 2 && flag == 0; k++) {
            i = k;
            if(shelf[k][0] != Token.NOTHING) {
                for (int j = 0; j < COLUMNS && flag == 0; j++) {
                    if (shelf[k][0] != shelf[i][j])
                        flag = 1;
                    i++;
                }
            }
        }

        for (int k = 0; k < 2 && flag == 0; k++) {
            i = k;
            if(shelf[k][5] != Token.NOTHING) {
                for (int j = COLUMNS; j > 0 && flag == 0; i--) {
                    if (shelf[k][5] != shelf[i][j])
                        flag = 1;
                    i++;
                }
            }
        }

        if(flag==1)
            return 0;
        else
            return 1;
    }

}
