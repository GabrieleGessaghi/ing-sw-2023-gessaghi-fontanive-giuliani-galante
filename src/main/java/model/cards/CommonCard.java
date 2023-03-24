package model.cards;

import model.Token;

/**
 @author Niccolò Giuliani
 common objective cards of the game
 */
public class CommonCard extends Card {
    private String[] players;
    private CommonObjective objective;
    /**
     * @author Niccolò Giuliani
     * @param objective
     * constructor
     */
    public CommonCard(CommonObjective objective) {

    }


    public int getPoints(Token[][] shelf) {
        return -1;
    }
    /**
     * @author Niccolò Giuliani
     * method to know which players have took the card
     */
    public String[] getPlayers() {
        return null;
    }


    /**
     * @author Niccolò Giuliani
     * @param nickname
     * method to add a player
     */
    public void addPlayer(String nickname){

    }
}
