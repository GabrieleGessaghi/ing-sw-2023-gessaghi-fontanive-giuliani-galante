package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.ModelUtil;

/**
 * Three columns each formed by 6 tiles of maximum three different types.
 * One column can show the same or a different combination of another column
 * @author Niccolò Giuliani
 */
public class ThreeColumns implements CommonObjective {
    @Override
    public boolean isSatisfied(Token[][] shelf) {
        ModelUtil util = new ModelUtil();
        return util.columnsChecker(shelf,false) >= 3;
    }

    public CommonType getName(){
        return CommonType.THREECOLUMNS;
    }
}
