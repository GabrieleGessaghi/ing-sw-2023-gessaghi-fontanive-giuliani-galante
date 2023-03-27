package model.cards;
import java.util.*;
import java.util.function.Supplier;
import model.Token;

/**
 @author Niccolò Giuliani
 common objective cards of the game
 */
public class CommonCard extends Card {
    private int numberOfTokensLeft;
    private int numberOfPlayers;
    private CommonObjective objective;
    private CommonType name;

    /**
     * Class constructor
     *
     * @param objective       the algorithm for this card.
     * @param numberOfPlayers the number of players playing this game.
     * @author Niccolò Giuliani
     */
    public CommonCard(CommonObjective objective, int numberOfPlayers) {
        this.objective = objective;
        this.name = objective.getName();
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getPoints(Token[][] shelf) {
        boolean satisfied;
        int points = 0;
        satisfied = objective.isSatisfied(shelf);
        if (satisfied) {
            switch (numberOfPlayers) {
                case 2:
                    if (players.size() == 0)
                        return 8;
                    else if (players.size() == 1)
                        return 4;
                case 3:
                    if (players.size() == 0)
                        return 8;
                    else if (players.size() == 1)
                        return 6;
                    else if (players.size() == 2)
                        return 4;
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
