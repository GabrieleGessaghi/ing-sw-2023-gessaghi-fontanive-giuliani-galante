package server.model.cards;

import server.model.Token;
import server.controller.utilities.ConfigLoader;

import java.io.Serializable;

/**
 * Common objective card of the game.
 * @author Niccolò Giuliani
 */
public class CommonCard extends Card implements Serializable {
    private int numberOfTokensLeft;
    private final int numberOfPlayers;
    private final CommonObjective objective;
    private final CommonType name;

    /**
     * Class constructor.
     * @author Niccolò Giuliani
     * @param objective The algorithm for this card.
     * @param numberOfPlayers The number of players playing this game.
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
     * @param shelf A matrix of Tokens taken from a player's shelf.
     * @return The number of points the player gets.
     */
    public int getPoints(Token[][] shelf) {
        boolean satisfied;
        int points = 0;
        satisfied = objective.isSatisfied(shelf);
        if (satisfied) {
            points = ConfigLoader.COMMONCARD_POINTS[numberOfPlayers - ConfigLoader.PLAYERS_MIN][numberOfPlayers - numberOfTokensLeft];
            numberOfTokensLeft--;
        }
        return points;
    }

    public int getIndex() {
        return name.ordinal();
    }
}
