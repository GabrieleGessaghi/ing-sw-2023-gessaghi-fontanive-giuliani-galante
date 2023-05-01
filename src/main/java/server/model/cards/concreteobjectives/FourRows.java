package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.TokenTools;

/**
 * Four lines each formed by 5 tiles of maximum three different types.
 * One line can show the same or a different combination of another line.
 * @author Niccolò Giuliani
 */
public class FourRows implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        return TokenTools.rowsChecker(shelf, false) >= 4;
    }

    @Override
    public CommonType getName(){
        return CommonType.FOURROWS;
    }

    @Override
    public String getDescription() {
        return "Four lines each formed by 5 tiles of maximum three different types.\nOne line can show the same or a different combination of another line.";
    }
}
