package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Four groups each containing at least
 * 4 tiles of the same type (not necessarily
 * in the depicted shape).
 * The tiles of one group can be different
 * from those of another group.
 */
public class FourGroups implements CommonObjective {
    @Override
    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
