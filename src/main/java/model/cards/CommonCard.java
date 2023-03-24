package model.cards;

import model.Token;

/**
 @author Niccolò Giuliani
 common objective cards of the game
 */
public class CommonCard extends Card {
    private String[] players;

    /**
     @author Niccolò Giuliani
     constructor
     */
    public CommonCard() {

    }
    /**
     @author Niccolò Giuliani
     method to set the type of the card
     */
    public void setCommonObjective(CommonObjective objective) {

    }

    public int getPoints(Token[][] shelf) {
        return -1;
    }
    /**
     @author Niccolò Giuliani
     method to know which players have took the card
     */
    public String[] getPlayers() {
        return null;
    }


    /**
     @author Niccolò Giuliani
     method to add a player
     */
    public void addPlayer(String nickname){

    }
}
