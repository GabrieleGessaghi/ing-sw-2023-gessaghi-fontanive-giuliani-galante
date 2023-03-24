package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;


/**
 @author Niccolò Giuliani
 Four tiles of the same type in the four
 corners of the bookshelf.
 */
public class Corners implements CommonObjective {

    public int getPoints(Token[][] shelf){

        if( shelf[0][0] != Token.NOTHING && shelf[0][0] == shelf[ROWS][0] && shelf[0][0] == shelf[0][COLUMNS] && shelf[0][0] == shelf[ROWS][COLUMNS])
            return 1;
        else
            return 0;

    }
}
