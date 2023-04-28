package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.ModelUtil;

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
