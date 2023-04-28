package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.TokenTools;



/**
 * Four groups each containing at least
 * 4 tiles of the same type (not necessarily
 * in the depicted shape).
 * The tiles of one group can be different
 * from those of another group.
 * @author Niccolò Giuliani
 */
public class FourGroups implements CommonObjective {
    //private int counterInterIsland; //counter of the instances of the island


    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int generalCounter; //general counter of the groups
        TokenTools util = new TokenTools();
        generalCounter = util.counterIslandType(Token.CAT, shelf,false)  + util.counterIslandType(Token.TOY, shelf,false) + util.counterIslandType(Token.BOOK, shelf,false) +
                util.counterIslandType(Token.TROPHY, shelf,false) + util.counterIslandType(Token.FRAME, shelf,false) + util.counterIslandType(Token.PLANT, shelf,false);
        return generalCounter >= 4;
    }

    public CommonType getName(){
        return CommonType.FOURGROUPS;
    }

}
