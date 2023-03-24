package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Two lines each formed by 5 different
 * types of tiles. One line can show the
 * same or a different combination of the
 * other line.
 */
public class TwoRows implements CommonObjective {
    @Override
    public int getPoints(Token[][] shelf) {
        int[] counter;
        int differentType;
        counter = new int[7];
        int AtLeastTwo = 0;
        for(int i=0; i < ROWS ; i++){
            for(int w=0; w < 7 ; w++)
                counter[w]=0;
            differentType = 0;
            for(int j=0; j < COLUMNS ; j++){
                if(shelf[i][j] == Token.CAT )
                    counter[0]++;
                else if(shelf[i][j] == Token.BOOK)
                    counter[1]++;
                else if(shelf[i][j] == Token.TOY)
                    counter[2]++;
                else if(shelf[i][j] == Token.TROPHY)
                    counter[3]++;
                else if(shelf[i][j] == Token.FRAME)
                    counter[4]++;
                else if(shelf[i][j] == Token.PLANT)
                    counter[5]++;
                else if(shelf[i][j] == Token.NOTHING)
                    counter[6]++;
            }
            for(int n=0; n < 6; n++){
                if(counter[n] > 0)
                    differentType++;
            }
            if(differentType == 5 && counter[6] == 0)
                AtLeastTwo++;
        }

        if(AtLeastTwo >= 2)
            return 1;
        else
            return 0;
    }
}
