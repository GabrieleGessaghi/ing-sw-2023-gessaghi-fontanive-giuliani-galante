package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;
import model.cards.ModelUtil;

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
        ModelUtil util = new ModelUtil();
        return util.rowsChecker(shelf, true) >= 2;
    }

    public CommonType getName(){
        return CommonType.TWOROWS;
    }
}
