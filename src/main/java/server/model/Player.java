package server.model;

import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.model.cards.Card;
import server.model.cards.CommonCard;
import server.model.cards.TokenTools;
import server.model.cards.PersonalCard;
import server.model.exceptions.FullColumnException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles players' shelves and cards.
 * @author Niccolò Galante
 */
public class Player implements Savable, Observable {
    private final String nickname;
    private int points;
    private final boolean isFirstPlayer;
    private final Map<Card, Boolean> cards;
    private final Shelf playerShelf;
    private final List<Observer> observers;
    public boolean isConnected;

    /**
     * Class constructor.
     * @author Niccolò Galante
     * @param nickname Player's nickname.
     * @param isFirstPlayer Indicates whether player is first or not.
     * @param personal Player's personal card.
     * @param common Common cards.
     */
    public Player(String nickname, boolean isFirstPlayer, PersonalCard personal, List<CommonCard> common) {
        cards = new HashMap<>();
        cards.put(personal, true);
        cards.put(common.get(0), true);
        cards.put(common.get(1), true);
        playerShelf = new Shelf();
        this.nickname = nickname;
        this.isFirstPlayer = isFirstPlayer;
        isConnected = true;
        observers = new ArrayList<>();
        points = 0;
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
        int adjacentPoints = 0;
        int cardPoints = 0;

        //Checks if card objectives have been reached
        for (Card card : cards.keySet()) {
            if (cards.get(card))
                cardPoints = card.getPoints(playerShelf.getTiles());
            if (cardPoints > 0)
                cards.replace(card, false);
            points += cardPoints;
        }
//        for (int i = 0; i < NUMBER_OF_CARDS; i++)
//            if (!isComplete[i] && cards.get(i).getPoints(playerShelf.getTiles()) != 0) {
//                isComplete[i] = true;
//                tempPoints += cards.get(i).getPoints(playerShelf.getTiles());
//            }

        //Checks if tiles of same type are adjacent
        TokenTools util = new TokenTools();
        for (Token tokenType : Token.values())
            adjacentPoints += util.counterIslandType(tokenType, playerShelf.getTiles(), true);
        points += adjacentPoints;

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

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void updateObservers(Event event) {
        for (Observer observer : observers)
            if (observer != null)
                observer.update(event);
    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public void loadState(String jsonMessage) {

    }
}
