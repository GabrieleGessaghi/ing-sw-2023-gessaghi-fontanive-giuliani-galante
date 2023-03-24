package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Two lines each formed by 5 different
 * types of tiles. One line can show the
 * same or a different combination of the
 * other line.
 */
public class TwoRows implements CommonObjective {
    @Override
    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
