package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Five tiles of the same type forming an X.
 */
public class XShape implements CommonObjective {
    @Override
    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
