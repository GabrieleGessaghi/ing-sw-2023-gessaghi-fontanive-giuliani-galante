package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;
import model.cards.ModelUtil;

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
