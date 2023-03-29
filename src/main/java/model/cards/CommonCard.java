package model.cards;

import model.Token;

import static model.Configuration.COMMONCARD_POINTS;

/**
 * Common objective cards of the game.
 * @author Niccolò Giuliani
 */
public class CommonCard extends Card {
    private int numberOfTokensLeft;
    private final int numberOfPlayers;
    private final CommonObjective objective;
    private final CommonType name;
    private final int[][] common_points;

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
        this.numberOfTokensLeft = numberOfPlayers;
        common_points = new int[4][4];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++)
                common_points[i][j] = COMMONCARD_POINTS[i][j];
        }
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
                            points = common_points[0][0];
                            break;
                        case 1:
                            points = common_points[0][1];
                            break;
                    }
                    break;
                case 3:
                    switch (numberOfTokensLeft) {
                        case 3:
                            points = common_points[1][0];
                            break;
                        case 2:
                            points = common_points[1][1];
                            break;
                        case 1:
                            points = common_points[1][2];
                            break;
                    }
                    break;
                case 4:
                    switch (numberOfTokensLeft) {
                        case 4:
                            points = common_points[2][0];
                            break;
                        case 3:
                            points = common_points[2][1];
                            break;
                        case 2:
                            points = common_points[2][2];
                            break;
                        case 1:
                            points = common_points[2][3];
                            break;
                    }
                    break;
            }
            numberOfTokensLeft--;
        }
        return points;
    }
}
