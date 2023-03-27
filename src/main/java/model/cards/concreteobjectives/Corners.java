package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;


/**
 *  Four tiles of the same type in the four
 *  corners of the bookshelf.
 * @author Niccolò Giuliani
 */
public class Corners implements CommonObjective {

    public int getPoints(Token[][] shelf){

        if( shelf[0][0] != Token.NOTHING && shelf[0][0] == shelf[ROWS][0] && shelf[0][0] == shelf[0][COLUMNS] && shelf[0][0] == shelf[ROWS][COLUMNS])
            return 1;
        else
            return 0;

    }
    public CommonType name(){
        return CommonType.CORNERS;
    }
}
