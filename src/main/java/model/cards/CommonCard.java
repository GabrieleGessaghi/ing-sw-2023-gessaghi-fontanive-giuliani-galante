package model.cards;
import java.util.*;
import java.util.function.Supplier;
import model.Token;

/**
 * common objective cards of the game
 * @author Niccolò Giuliani
 */
public class CommonCard extends Card {
    private int numberOfTokensLeft;
    private int numberOfPlayers;
    private CommonObjective objective;
    private CommonType name;

    /**
     * Class constructor
     * @author Niccolò Giuliani
     * @param objective       the algorithm for this card.
     * @param numberOfPlayers the number of players playing this game.
     * @author Niccolò Giuliani
     */
    public CommonCard(CommonObjective objective, int numberOfPlayers) {
        this.objective = objective;
        this.name = objective.getName();
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Gives players' the points they deserve and removes the points token.
     * @author Giorgio Massimo Fontanive
     * @param shelf a matrix of Tokens taken from a player's shelf.
     * @return the number of points the player gets.
     */
    public int getPoints(Token[][] shelf) {
        boolean satisfied;
        int points = 0;
        satisfied = objective.isSatisfied(shelf);
        if (satisfied) {
            switch (numberOfPlayers) {
                case 2:
                    switch (numberOfTokensLeft) {
                        case 2:
                            points = 8;
                        case 1:
                            points = 4;
                    }
                case 3:
                    switch (numberOfTokensLeft) {
                        case 3:
                            points = 8;
                        case 2:
                            points = 6;
                        case 1:
                            points = 4;
                    }
                case 4:
                    switch (numberOfTokensLeft) {
                        case 4:
                            points = 8;
                        case 3:
                            points = 6;
                        case 2:
                            points = 4;
                        case 1:
                            points = 2;
                    }
            }
            numberOfTokensLeft--;
        }
        return points;
    }
}
