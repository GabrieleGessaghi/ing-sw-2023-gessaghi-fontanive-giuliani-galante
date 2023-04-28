package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;
import model.cards.ModelUtil;

/**
 * Two columns each formed by 6
 * different types of tiles.
 * @author Niccolò Giuliani
 */
public class TwoColumns implements CommonObjective {
    @Override
    public boolean isSatisfied(Token[][] shelf) {
        ModelUtil util = new ModelUtil();
        return util.columnsChecker(shelf, true) >= 2;
    }

    public CommonType getName(){
        return CommonType.TWOCOLUMNS;
    }
}
