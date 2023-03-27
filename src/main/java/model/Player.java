package model;

import model.cards.Card;

/**
 * Handles players' shelves and cards.
 * @author Niccolò Galante
 */
public class Player {
    private final String nickname;
    private int points;
    private boolean isFirstPlayer;
    public boolean isConnected;
    public Card[] playerCards;
    private Shelf playerShelf;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter for players' nickname.
     * @author Niccolò Galante
     * @return player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for players' points.
     * @author Niccolò Galante
     * @return player's points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for boolean to indicate if player is first.
     * @author Niccolò Galante
     * @return true if player is first.
     */
    public boolean getIsFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * Getter for boolean to indicate if player is first.
     * @author Niccolò Galante
     * @return true if player is first.
     */
    public void updatePoints(){

    }

}
