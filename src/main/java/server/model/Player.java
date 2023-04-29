package server.model;

import com.google.gson.JsonObject;
import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
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
    private String nickname;
    private int points;
    private Shelf playerShelf;
    private Card personalCard;
    private final Map<Card, Boolean> commonCards;
    private final List<Observer> observers;
    private boolean isFirstPlayer;
    public boolean isConnected;

    /**
     * Class constructor.
     * @author Niccolò Galante
     * @param nickname Player's nickname.
     * @param isFirstPlayer Indicates whether player is first or not.
     * @param personalCardIndex Player's personal card's index.
     * @param common Common cards.
     */
    public Player(String nickname, boolean isFirstPlayer, int personalCardIndex, List<CommonCard> common) {
        commonCards = new HashMap<>();
        for (CommonCard commonCard : common)
            commonCards.put(commonCard, true);
        personalCard = new PersonalCard(personalCardIndex);
        playerShelf = new Shelf();
        points = 0;
        this.nickname = nickname;
        this.isFirstPlayer = isFirstPlayer;
        this.isConnected = true;
        observers = new ArrayList<>();
    }

    public Player(String jsonState, List<CommonCard> common) {
        loadState(jsonState);
        commonCards = new HashMap<>();
        for (CommonCard commonCard : common)
            commonCards.put(commonCard, true);
        nickname = "";
        playerShelf = new Shelf();
        personalCard = null;
        observers = new ArrayList<>();
        isFirstPlayer = false;
        isConnected = true;
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

        //Checks if tiles of same type are adjacent
        int adjacentPoints = 0;
        for (Token tokenType : Token.values())
            adjacentPoints += TokenTools.counterIslandType(tokenType, playerShelf.getTiles(), true);
        points += adjacentPoints;

        return points + adjacentPoints + personalCard.getPoints(playerShelf.getTiles());
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
    private void updateCommonCardPoints() {

        //Checks if common card objectives have been reached
        int cardPoints = 0;
        for (Card card : commonCards.keySet()) {
            if (commonCards.get(card))
                cardPoints = card.getPoints(playerShelf.getTiles());
            if (cardPoints > 0)
                commonCards.replace(card, false);
            points += cardPoints;
        }
//        for (int i = 0; i < NUMBER_OF_CARDS; i++)
//            if (!isComplete[i] && cards.get(i).getPoints(playerShelf.getTiles()) != 0) {
//                isComplete[i] = true;
//                tempPoints += cards.get(i).getPoints(playerShelf.getTiles());
//            }

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
        updateCommonCardPoints();
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
        Map<String, Object> elements = new HashMap<>();
        elements.put("nickname", nickname);
        elements.put("commonCardPoints", points);
        elements.put("totalPoints", getPoints());
        elements.put("personalCardIndex", personalCard.getIndex());
        elements.put("shelf", JsonTools.createJson(JsonTools.parseJson(playerShelf.getState())));
        elements.put("isFirstPlayer", isFirstPlayer ? 1 : 0);
        //TODO: Save which cards have already been completed
        return JsonTools.createJson(elements).toString();
    }

    @Override
    public void loadState(String jsonMessage) {
        Map<String, Object> elements;
        elements = JsonTools.parseJson(jsonMessage);
        nickname = (String) elements.get("nickname");
        points = (Integer) elements.get("commonCardPoints");
        personalCard = new PersonalCard((Integer) elements.get("personalCardIndex"));
        isFirstPlayer = (Integer) elements.get("isFirstPlayer") == 1;
        playerShelf = new Shelf();
        playerShelf.loadState(elements.get("shelf").toString());
        //TODO: Get which cards have already been completed
    }
}
