package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Four lines each formed by 5 tiles of
 * maximum three different types. One
 * line can show the same or a different
 * combination of another line.
 */
public class FourRows implements CommonObjective {

    @Override
    public int getPoints(Token[][] shelf) {

        return 0;
    }
}
