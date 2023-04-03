package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Four lines each formed by 5 tiles of
 * maximum three different types. One
 * line can show the same or a different
 * combination of another line.
 * @author Niccolò Giuliani
 */
public class FourRows implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int[] counter = new int[7];
        int differentType;
        int atLeastFour = 0;
        for (int i = 0; i < ROWS ; i++){
            for (int w = 0; w < 7 ; w++)
                counter[w]=0;
            differentType = 0;
            for (int j = 0; j < COLUMNS ; j++)
                switch (shelf[i][j]) {
                    case CAT -> counter[0]++;
                    case BOOK -> counter[1]++;
                    case TOY -> counter[2]++;
                    case TROPHY -> counter[3]++;
                    case FRAME -> counter[4]++;
                    case PLANT -> counter[5]++;
                    case NOTHING -> counter[6]++;
                }
            for (int n = 0; n < 6; n++)
                if(counter[n] > 0)
                    differentType++;
            if (differentType <= 3 && counter[6] == 0)
                atLeastFour++;
        }
        return atLeastFour >= 4;
    }

    public CommonType getName(){
        return CommonType.FOURROWS;
    }

}
