package model.cards;
import java.util.*;
import java.util.function.Supplier;
import model.Token;

/**
 @author Niccolò Giuliani
 common objective cards of the game
 */
public class CommonCard extends Card {
    private List<String> players;
    private int numberOfPlayers;
    private CommonObjective objective;
    private CommonType name;

    /**
     * Class constructor
     * @author Niccolò Giuliani
     * @param objective the algorithm for this card.
     * @param numberOfPlayers the number of players playing this game.
     */
    public CommonCard(CommonObjective objective, int numberOfPlayers) {
        this.objective = objective;
        this.name=objective.getName();
        this.numberOfPlayers=numberOfPlayers;
    }

    public int getPoints(Token[][] shelf) {
        boolean satisfied;
        satisfied = objective.isSatisfied(shelf);
        if(satisfied)
            switch (numberOfPlayers) {
                case 2:
                    if(players.size() == 0)
                        return 8;
                    else if(players.size() == 1)
                        return 4;
                case 3:
                    if(players.size() == 0)
                        return 8;
                    else if(players.size() == 1)
                        return 6;
                    else if(players.size() == 2)
                        return 4;
                case 4:
                    if(players.size() == 0)
                        return 8;
                    else if(players.size() == 1)
                        return 6;
                    else if(players.size() == 2)
                        return 4;
                    else if(players.size() == 3)
                        return 2;
            }
        return -1;
    }

    /**
     * Getter for the players list.
     * @author Niccolò Giuliani
     * @return the list of player names who have already taken the card.
     */
    public List getPlayers() {
        return players;
    }

    /**
     * Adds a player's name to the list.
     * @author Niccolò Giuliani
     * @param nickname the name chosen by the player.
     */
    public void addPlayer(String nickname){
        players.add(nickname);
    }
}
