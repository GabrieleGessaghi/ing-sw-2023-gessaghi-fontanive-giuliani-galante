package model;

import model.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles players' shelves and cards.
 * @author Niccolò Galante
 */
public class Player {
    private final String nickname;
    private int points;
    private boolean isFirstPlayer;
    public boolean isConnected;
    public List<Card> cards; //array used for common and personal cards
    private final Shelf playerShelf; //reference to player's shelf

    public Player(String nickname) {
        //TODO: Add isfirst player and cards as parameters
        cards = new ArrayList<>();
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
        tempPoints = 0;
        for(Card card: cards){
            tempPoints = tempPoints + card.getPoints(playerShelf.getTiles()); //sum of points for each card
        }
        //TODO: check if player has already obtained points from specific card
    }

    /**
     * Inserts tokens.
     * @param tokens
     * @param column
     * @author Niccolò Galante.
     */
    public void insertTokens(Token[] tokens, int column){
        //Si selezionano fino a 3 token (array); i token vanno inseriti uno ad uno controllano che non siano NOTHING, controllando ogni volta
        //che la colonna scelta non sia piena (vedi FullColumnException di Shelf => si deve usare try e catch);
        //con catch si può usare IllegalMoveException
        //se è piena bisogna eliminare i token già inseriti
    }

}
