package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Five tiles of the same type forming a
 * diagonal.
 * @author Niccolò Giuliani
 */
public class Stairs implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
       return checkType(shelf,Token.CAT) || checkType(shelf,Token.BOOK) || checkType(shelf,Token.TOY) || checkType(shelf,Token.TROPHY)
        || checkType(shelf,Token.FRAME) || checkType(shelf,Token.PLANT);
    }
    private boolean checkType(Token[][] shelf,Token type){
        int []counter;
        counter = new int[ROWS];
        int k,w;
        boolean flagRight = false;
        boolean flagLeft = false;
        boolean flagRvsdRight = false;
        boolean flagRvsdfLeft = false;
        k=0;
        w=1;
        for(k = 0,w = 1; k < 2 && flagLeft; k++, w--) {
            flagLeft=false;
            for (int j = 0; j < COLUMNS; j++) {
                for (int i = k; i < ROWS - w - j; i++) {
                    if (shelf[i][j] != type)
                        flagLeft = true;

                }
            }

        }

        if(!flagLeft)
            return true;
        else{
            for(k = 0,w = 0; k < 2 && flagLeft; k++, w++) {
                flagRight=false;
                for (int j = 0; j < COLUMNS; j++) {
                    for (int i = k; i < j + 1 + w; i++) {
                        if (shelf[i][j] != type)
                            flagRight = true;

                    }
                }

            }
            if(!flagRight)
                return true;
            else{
                for(k = 0,w = 0; k < 2 && flagLeft; k++, w++) {
                    flagRvsdfLeft=false;
                    for (int j = 0; j < COLUMNS; j++) {
                        for (int i = 4 + k; i > j + 1 + w; i--) {
                            if (shelf[i][j] != type)
                                flagRvsdfLeft = true;

                        }
                    }

                }
                if(!flagRvsdfLeft)
                    return true;
                else{
                    for(k = 0,w = 0; k < 2 && flagLeft; k++, w++) {
                        flagRvsdRight=false;
                        for (int j = 0; j < COLUMNS; j++) {
                            for (int i = 4 + k; i > 4 - j + w + 1; i--) {
                                if (shelf[i][j] != type)
                                    flagRvsdRight = true;

                            }
                        }

                    }
                    if(!flagRvsdRight)
                        return true;
                    else
                        return false;
                }
            }

        }




    }

    public CommonType getName(){
        return CommonType.STAIRS;
    }
}
