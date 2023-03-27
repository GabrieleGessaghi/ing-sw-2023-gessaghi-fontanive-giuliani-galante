package model.cards;
import java.util.*;
import java.util.function.Supplier;
import model.Token;

/**
 * common objective cards of the game
 * @author Niccolò Giuliani
 */
public class CommonCard extends Card {
    private List<String> players = new ArrayList();
    private CommonObjective objective;
    private int numberOfPlayers;
    private CommonType name;
    /**
     * constructor
     * @author Niccolò Giuliani
     * @param obj objective of the card
     */
    public CommonCard(CommonObjective obj, int number) {
        this.objective = obj;
        this.numberOfPlayers=number;
        this.name=obj.name();
    }


    public int getPoints(Token[][] shelf) {
        int i;
        i=this.objective.getPoints(shelf);
        if(i == 1){
            if(numberOfPlayers == 2){
                if(players.size() == 0)
                    return 8;
                else if(players.size() == 1)
                    return 4;
            }
            else if(numberOfPlayers == 3){
                if(players.size() == 0)
                    return 8;
                else if(players.size() == 1)
                    return 6;
                else if(players.size() == 2)
                    return 4;
            }
            else if(numberOfPlayers == 4){
                if(players.size() == 0)
                    return 8;
                else if(players.size() == 1)
                    return 6;
                else if(players.size() == 2)
                    return 4;
                else if(players.size() == 3)
                    return 2;
            }
        }
        return -1;
    }
    /**
     * method to know which players have took the card
     * @author Niccolò Giuliani
     * @return players who completed the objective
     */
    public List getPlayers() {
        return players;
    }


    /**
     * method to add a player
     * @author Niccolò Giuliani
     * @param nickname nickname of the player
     */
    public void addPlayer(String nickname){
        players.add(nickname);
    }
}
