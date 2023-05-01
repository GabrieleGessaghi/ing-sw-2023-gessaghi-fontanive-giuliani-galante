package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;

/**
 * Four tiles of the same type in the four corners of the bookshelf.
 * @author Niccolò Giuliani
 */
public class Corners implements CommonObjective {

    public boolean isSatisfied(Token[][] shelf) {
        return shelf[0][0] != Token.NOTHING &&
                shelf[0][0] == shelf[ROWS - 1][0] &&
                shelf[0][0] == shelf[0][COLUMNS - 1] &&
                shelf[0][0] == shelf[ROWS - 1][COLUMNS - 1];
    }

    @Override
    public CommonType getName(){
        return CommonType.CORNERS;
    }

    @Override
    public String getDescription() {
        return "Four tiles of the same type in the four corners of the bookshelf.";
    }
}
