package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Four tiles of the same type in the four corners of the bookshelf.
 * @author Niccolò Giuliani
 */
public class Corners implements CommonObjective {
    public boolean isSatisfied(Token[][] shelf){

        if(shelf[0][0] != Token.NOTHING &&
                shelf[0][0] == shelf[ROWS][0] &&
                shelf[0][0] == shelf[0][COLUMNS] &&
                shelf[0][0] == shelf[ROWS][COLUMNS])
            return true;
        else
            return false;

    }

    public CommonType getName(){
        return CommonType.CORNERS;
    }
}
