package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.TokenTools;

/**
 * Two rows each formed by 5 different types of tiles.
 * One line can show the same or a different combination of the other line.
 * @author Niccolò Giuliani
 */
public class TwoRows implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        return TokenTools.rowsChecker(shelf, true) >= 2;
    }

    @Override
    public CommonType getName(){
        return CommonType.TWOROWS;
    }

    @Override
    public String getDescription() {
        return "Two rows each formed by 5 different types of tiles.\n" +
                "One line can show the same or a different combination of the other line.";
    }
}
