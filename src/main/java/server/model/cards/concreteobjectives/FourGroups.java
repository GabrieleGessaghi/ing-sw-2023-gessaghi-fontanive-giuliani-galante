package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.TokenTools;



/**
 * Four groups each containing at least 4 tiles of the same type.
 * The tiles of one group can be different from those of another group.
 * @author Niccolò Giuliani
 */
public class FourGroups implements CommonObjective {
    //private int counterInterIsland; //counter of the instances of the island


    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int generalCounter; //general counter of the groups
        generalCounter = TokenTools.counterIslandType(Token.CAT, shelf,false)  + TokenTools.counterIslandType(Token.TOY, shelf,false) + TokenTools.counterIslandType(Token.BOOK, shelf,false) +
                TokenTools.counterIslandType(Token.TROPHY, shelf,false) + TokenTools.counterIslandType(Token.FRAME, shelf,false) + TokenTools.counterIslandType(Token.PLANT, shelf,false);
        return generalCounter >= 4;
    }

    @Override
    public CommonType getName(){
        return CommonType.FOURGROUPS;
    }

    @Override
    public String getDescription() {
        return  "Four groups each containing at least 4 tiles of the same type.\nThe tiles of one group can be different from those of another group";
    }
}
