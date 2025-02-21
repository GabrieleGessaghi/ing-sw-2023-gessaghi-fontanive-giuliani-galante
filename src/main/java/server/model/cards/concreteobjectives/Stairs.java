package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;

/**
 * Five columns of increasing or decreasing height.
 * Starting from the first column on the left or on the right, each next column must be made of exactly one more tile.
 * Tiles can be of any type.
 * @author Niccolò Giuliani
 */
public class Stairs implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int[] counter;
        counter = new int[COLUMNS];
        boolean flagRight = false;
        boolean flagLeft = false;
        for (int i = 0; i < COLUMNS; i++)
            counter[i] = 0;
        for (int i = 0; i < COLUMNS ; i++)
            for (int j = 0; j < ROWS ; j++)
                if (shelf[j][i] != Token.NOTHING)
                    counter[i]++;
        for (int i = 0; i < COLUMNS - 1; i++)
            if(counter[i] != counter[i + 1] + 1 )
                    flagLeft = true;
        for (int i = 0; i < COLUMNS - 1; i++)
            if (counter[i] != counter[i + 1] - 1)
                    flagRight = true;
        return !flagRight || !flagLeft;
    }

    @Override
    public CommonType getName(){
        return CommonType.STAIRS;
    }

    @Override
    public String getDescription() {
        return  "Five columns of increasing or decreasing height.\n" +
                "Starting from the first column on the left or on the right, each next column must be made of exactly one more tile.\n" +
                "Tiles can be of any type.";
    }
}