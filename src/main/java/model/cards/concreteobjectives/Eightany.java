package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * @author Niccolò Giuliani
 * Eight tiles of the same type. There’s no
 * restriction about the position of these
 * tiles.
 */
public class Eightany implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int countNothing =0, countCat=0, countBook=0, countToy=0, countTrophy=0, countFrame=0, countPlant=0;
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLUMNS; j++){

                if (shelf[i][j] == Token.CAT)
                    countCat++;
                else if (shelf[i][j] == Token.BOOK)
                    countBook++;
                else if (shelf[i][j] == Token.TOY)
                    countToy++;
                else if (shelf[i][j] == Token.TROPHY)
                    countTrophy++;
                else if (shelf[i][j] == Token.FRAME)
                    countFrame++;
                else if (shelf[i][j] == Token.PLANT)
                    countPlant++;
            }
        }
        if( countCat >= 8 || countBook >= 8 || countToy >= 8 || countTrophy >=8 || countFrame >=8 || countPlant >=8 )
            return true;
        else
            return false;
    }
    public CommonType getName(){
        return CommonType.EIGHTANY;
    }
}
