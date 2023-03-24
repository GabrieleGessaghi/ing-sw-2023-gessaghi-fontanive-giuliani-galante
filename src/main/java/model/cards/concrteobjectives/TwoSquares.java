package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Two groups each containing 4 tiles of
 * the same type in a 2x2 square. The tiles
 * of one square can be different from
 * those of the other square.
 */
public class TwoSquares implements CommonObjective {
    @Override
    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
