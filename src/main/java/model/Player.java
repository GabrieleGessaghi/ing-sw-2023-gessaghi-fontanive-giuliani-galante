package model;

import model.cards.Card;
import model.cards.CommonCard;
import model.cards.PersonalCard;
import model.exceptions.FullColumnException;

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
    private boolean isFirstPlayer;
    public boolean isConnected;
    public boolean firstToFinish;
    public List<Card> cards; // array used for common and personal cards
    public boolean[] isCompleted; // array used to check if a card's objective has been completed
    private final Shelf playerShelf; // reference to player's shelf

    public Player(String nickname, boolean isFirst, PersonalCard personal, List<CommonCard> common) {
        cards = new ArrayList<>();
        playerShelf = new Shelf();
        isCompleted = new boolean[NUMBER_OF_CARDS]; // all false by default
        this.nickname = nickname;
        isFirstPlayer = isFirst;
        cards.set(0, personal);
        cards.set(1, common.get(0));
        cards.set(2, common.get(1));
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
        List<Card> tempCards;
        tempCards = cards;
        tempPoints = points;

        for(int i = 0; i< tempCards.size(); i++)
            if(isCompleted[i])
                tempCards.remove(i);

        for(Card card: tempCards)
            tempPoints = tempPoints + card.getPoints(playerShelf.getTiles()); // sum of points for each card

        if(firstToFinish) // adds bonus point if player is first to reach end game
            tempPoints++;

        points = tempPoints;

        //TODO: add points based on number of adjacent tiles of the same type

    }

    /**
     * Inserts tokens.
     * @param tokens tokens to insert in shelf.
     * @param column column in which tokens are to be inserted.
     * @author Niccolò Galante.
     */
    public void insertTokens(Token[] tokens, int column) throws FullColumnException {
        int tokensInserted = 0;

        for (Token t: tokens)
            if(!t.equals(Token.NOTHING))
                try {
                    playerShelf.insertToken(t, column);
                    tokensInserted++;
                }catch(FullColumnException e){
                    for(int i=0; i<tokensInserted; i++)
                        playerShelf.removeToken(column);
                }
    }

}
