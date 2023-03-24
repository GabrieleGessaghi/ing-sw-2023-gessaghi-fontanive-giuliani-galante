package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Two columns each formed by 6
 * different types of tiles.
 */
public class TwoColumns implements CommonObjective {
    @Override
    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
