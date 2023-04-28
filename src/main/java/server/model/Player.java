package server.model;

import server.model.cards.Card;
import server.model.cards.CommonCard;
import server.model.cards.ModelUtil;
import server.model.cards.PersonalCard;
import server.model.exceptions.FullColumnException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * Handles players' shelves and cards.
 * @author Niccolò Galante
 */
public class Player implements Serializable {
    private final static int NUMBER_OF_CARDS = 3;
    private final String nickname;
    private int points;
    private final boolean isFirstPlayer;
    private final List<Card> cards;
    private final Shelf playerShelf;
    public boolean isConnected;
    public boolean[] isComplete;
    private int adjacentPoints;


    /**
     * Class constructor.
     * @author Niccolò Galante
     * @param nickname Player's nickname.
     * @param isFirstPlayer Indicates whether player is first or not.
     * @param personal Player's personal card.
     * @param common Common cards.
     */
    public Player(String nickname, boolean isFirstPlayer, PersonalCard personal, List<CommonCard> common) {
        cards = new ArrayList<>();
        playerShelf = new Shelf();
        this.nickname = nickname;
        this.isFirstPlayer = isFirstPlayer;
        cards.add(0, personal);
        cards.add(1, common.get(0));
        cards.add(2, common.get(1));
        points = 0;
        adjacentPoints = 0;
        isComplete = new boolean[NUMBER_OF_CARDS];
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
    private void updatePoints() {
        int tempPoints = points - adjacentPoints;

        //Checks if card objectives have been reached
        for (int i = 0; i < NUMBER_OF_CARDS; i++)
            if (!isComplete[i] && cards.get(i).getPoints(playerShelf.getTiles()) != 0) {
                isComplete[i] = true;
                tempPoints += cards.get(i).getPoints(playerShelf.getTiles());
            }

        //Checks if tiles of same type are adjacent
        adjacentPoints = 0;
        ModelUtil util = new ModelUtil();
        adjacentPoints = util.counterIslandType(Token.CAT, playerShelf.getTiles(),true) + util.counterIslandType(Token.BOOK, playerShelf.getTiles(),true) +
                util.counterIslandType(Token.TOY, playerShelf.getTiles(),true) + util.counterIslandType(Token.TROPHY, playerShelf.getTiles(),true) +
                util.counterIslandType(Token.FRAME, playerShelf.getTiles(),true) + util.counterIslandType(Token.PLANT, playerShelf.getTiles(),true);
        points = tempPoints + adjacentPoints;
        //TODO: Check if player arrives at endgame first (adds 1 point)
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
