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
    public Card[] playerCards; //array used for common and personal cards
    private final static int NUMBER_OF_CARDS = 3; //size of array
    private Shelf playerShelf; //reference to player's shelf

    public Player(String nickname) {
        playerCards = new Card[NUMBER_OF_CARDS];
        this.nickname = nickname;
        playerShelf = new Shelf();
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
     * Updates players' points.
     * @author Niccolò Galante.
     */
    public void updatePoints(){
        int tempPoints;
        tempPoints = points;
        for(int i=0; i<NUMBER_OF_CARDS; i++){
            //tempPoints = tempPoints + playerCards[i].getPoints(playerShelf);
        }
    }

    /**
     * Inserts tokens.
     * @param tokens
     * @param column
     * @author Niccolò Galante.
     */
    public void insertTokens(Token[] tokens, int column){

    }

}
