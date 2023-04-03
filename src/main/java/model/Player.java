package model;

import model.cards.Card;
import model.cards.CommonCard;
import model.cards.PersonalCard;
import model.exceptions.FullColumnException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles players' shelves and cards.
 * @author Niccolò Galante
 */
public class Player {
    private final static int NUMBER_OF_CARDS = 3;
    private final String nickname;
    private int points;
    private final boolean isFirstPlayer;
    private List<Card> cards;
    private final Shelf playerShelf;
    public boolean isConnected;
    public boolean[] isComplete;

    public Player(String nickname, boolean isFirstPlayer, PersonalCard personal, List<CommonCard> common) {
        cards = new ArrayList<>();
        playerShelf = new Shelf();
        this.nickname = nickname;
        this.isFirstPlayer = isFirstPlayer;
        cards.add(personal);
        cards.add(common.get(0));
        cards.add(common.get(1));
    }

    /**
     * Getter for player's nickname.
     * @author Niccolò Galante
     * @return Player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for player's points.
     * @author Niccolò Galante
     * @return Player's points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for boolean to indicate if player is first.
     * @author Niccolò Galante
     * @return True if player is first.
     */
    public boolean getIsFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * Updates player's points.
     * @author Niccolò Galante.
     */
    public void updatePoints(){
        int tempPoints = points;
        isComplete = new boolean[NUMBER_OF_CARDS];

        for(int i = 0; cards.get(i) != null; i++){
            if(cards.get(i).getPoints(playerShelf.getTiles()) != 0)
                isComplete[i] = true;

            if(isComplete[i])
                tempPoints += cards.get(i).getPoints(playerShelf.getTiles());
            }
        //TODO: Ensure points from common cards are updated correctly.
        //TODO: Add points based on number of adjacent tiles of the same type
        //TODO: Check if player arrives at endgame first (adds 1 point)
        points = tempPoints;
    }

    /**
     * Inserts tokens.
     * @param tokens Tokens to insert in shelf.
     * @param column Column in which tokens are to be inserted.
     * @author Niccolò Galante.
     */
    public void insertTokens(Token[] tokens, int column) throws FullColumnException {
        int tokensInserted = 0;
        for (Token t: tokens)
            if(!t.equals(Token.NOTHING))
                try {
                    playerShelf.insertToken(t, column);
                    tokensInserted++;
                } catch(FullColumnException e) {
                    for(int i = 0; i < tokensInserted; i++)
                        playerShelf.removeToken(column);
                }
        updatePoints();
    }
}
