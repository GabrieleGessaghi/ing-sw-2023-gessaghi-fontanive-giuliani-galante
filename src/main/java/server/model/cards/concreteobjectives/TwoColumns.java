package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.TokenTools;

/**
 * Two columns each formed by 6 different types of tiles.
 * @author Niccolò Giuliani
 */
public class TwoColumns implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        return TokenTools.columnsChecker(shelf, true) >= 2;
    }

    @Override
    public CommonType getName(){
        return CommonType.TWOCOLUMNS;
    }

    @Override
    public String getDescription() {
        return "Two columns each formed by 6 different types of tiles.";
    }
}
