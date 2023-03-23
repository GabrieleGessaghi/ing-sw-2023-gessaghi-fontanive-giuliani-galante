package model.cards;

import model.Token;

/**
 @author Niccolò Giuliani
 common objective cards of the game
 */
public class CommonCard extends Card {
    private String[] players;
    public int getPoints(Token[][] shelf) {
        return -1;
    }
    public String[] getPlayers() {
        return null;
    }

    public void setCommonObjective(CommonObjective objective){

    }

    public void addPlayer(String nickname){

    }
}
