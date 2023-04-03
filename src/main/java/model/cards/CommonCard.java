package model.cards;

import model.Token;
import static model.Configurations.COMMONCARD_POINTS;
import static model.Configurations.MIN_PLAYERS;

/**
 * Common objective cards of the game.
 * @author Niccolò Giuliani
 */
public class CommonCard extends Card {
    private int numberOfTokensLeft;
    private final int numberOfPlayers;
    private final CommonObjective objective;
    private final CommonType name;

    /**
     * Class constructor
     * @author Niccolò Giuliani
     * @param objective       the algorithm for this card.
     * @param numberOfPlayers the number of players playing this game.
     * @author Niccolò Giuliani
     */
    public CommonCard(CommonObjective objective, int numberOfPlayers) {
        this.objective = objective;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfTokensLeft = numberOfPlayers;
        this.name = objective.getName();
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
            points = COMMONCARD_POINTS[numberOfPlayers - MIN_PLAYERS][numberOfPlayers - numberOfTokensLeft];
            numberOfTokensLeft--;
        }
        return points;
    }
}
