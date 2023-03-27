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
        int[] counter;
        int differentType;
        counter = new int[7];
        int AtLeastFour = 0;
        for(int i=0; i < COLUMNS ; i++){
            for(int w=0; w < 7 ; w++)
                counter[w]=0;
            differentType = 0;
            for(int j=0; j < ROWS ; j++){
                if(shelf[j][i] == Token.CAT )
                    counter[0]++;
                else if(shelf[j][i] == Token.BOOK)
                    counter[1]++;
                else if(shelf[j][i] == Token.TOY)
                    counter[2]++;
                else if(shelf[j][i] == Token.TROPHY)
                    counter[3]++;
                else if(shelf[j][i] == Token.FRAME)
                    counter[4]++;
                else if(shelf[j][i] == Token.PLANT)
                    counter[5]++;
                else if(shelf[j][i] == Token.NOTHING)
                    counter[6]++;
            }
            for(int n=0; n < 6; n++){
                if(counter[n] > 0)
                    differentType++;
            }
            if(differentType <= 3 && counter[6] == 0)
                AtLeastFour++;
        }

        if(AtLeastFour >= 4)
            return true;
        else
            return false;
    }

    public CommonType getName(){
        return CommonType.FOURROWS;
    }

}
