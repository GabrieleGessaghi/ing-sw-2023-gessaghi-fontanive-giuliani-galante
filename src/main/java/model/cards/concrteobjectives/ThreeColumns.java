package model.cards.concrteobjectives;

import model.Token;
import model.cards.CommonObjective;

/**
 * @author Niccolò Giuliani
 * Three columns each formed by 6 tiles Five tiles of the same type forming an X.
 * of maximum three different types. One
 * column can show the same or a different
 * combination of another column.
 */
public class ThreeColumns implements CommonObjective {
    @Override
    public int getPoints(Token[][] shelf) {
        return 0;
    }
}
