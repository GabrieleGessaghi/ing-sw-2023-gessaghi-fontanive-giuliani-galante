package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;

/**
 * Eight tiles of the same type. There’s no
 * restriction about the position of these
 * tiles.
 * @author Niccolò Giuliani
 */
public class Eightany implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int countNothing = 0, countCat = 0, countBook = 0, countToy = 0, countTrophy = 0, countFrame = 0, countPlant = 0;
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++)
                switch (shelf[i][j]) {
                    case CAT -> countCat++;
                    case BOOK -> countBook++;
                    case TOY -> countToy++;
                    case TROPHY -> countTrophy++;
                    case FRAME -> countFrame++;
                    case PLANT -> countPlant++;
                }
        return countCat >= 8 || countBook >= 8 || countToy >= 8 || countTrophy >= 8 || countFrame >= 8 || countPlant >= 8;
    }

    public CommonType getName(){
        return CommonType.EIGHTANY;
    }
}
