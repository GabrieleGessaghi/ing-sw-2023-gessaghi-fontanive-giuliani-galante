package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.TokenTools;

/**
 * Two rows each formed by 5 different
 * types of tiles. One line can show the
 * same or a different combination of the
 * other line.
 * @author Niccolò Giuliani
 */
public class TwoRows implements CommonObjective {
    @Override
    public boolean isSatisfied(Token[][] shelf) {
        TokenTools util = new TokenTools();
        return util.rowsChecker(shelf, true) >= 2;
    }

    public CommonType getName(){
        return CommonType.TWOROWS;
    }
}
