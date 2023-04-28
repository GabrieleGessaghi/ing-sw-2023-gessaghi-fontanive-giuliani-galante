package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;
import model.cards.ModelUtil;

/**
 * Four lines each formed by 5 tiles of
 * maximum three different types. One
 * line can show the same or a different
 * combination of another line.
 * @author Niccolò Giuliani
 */
public class FourRows implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        ModelUtil util = new ModelUtil();
        return util.rowsChecker(shelf, false) >= 4;
    }

    public CommonType getName(){
        return CommonType.FOURROWS;
    }

}
