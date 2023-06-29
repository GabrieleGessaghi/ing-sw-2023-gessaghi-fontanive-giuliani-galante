package server.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.model.cards.Card;
import server.model.cards.CommonCard;
import server.model.cards.TokenTools;
import server.model.cards.PersonalCard;
import server.model.exceptions.IllegalColumnException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles players' shelves and cards.
 * @author Niccolò Galante
 */
public class Player implements Savable {
    private String nickname;
    private Shelf shelf;
    private Card personalCard;
    private final Map<Card, Boolean> commonCards;
    private int commonCardPoints;
    private boolean isFirstPlayer;
    public boolean isConnected;

    /**
     * Class constructor.
     * @author Niccolò Galante
     * @param playerNickname Player's nickname.
     * @param firstPlayer Indicates whether player is first or not.
     * @param personalCardIndex Player's personal card's index.
     * @param commonCardsList Common cards.
     */
    public Player(String playerNickname, boolean firstPlayer, int personalCardIndex, List<CommonCard> commonCardsList) {
        nickname = playerNickname;
        shelf = new Shelf();
        personalCard = new PersonalCard(personalCardIndex);
        commonCardPoints = 0;
        isFirstPlayer = firstPlayer;
        isConnected = true;
        commonCards = new HashMap<>();
        for (CommonCard commonCard : commonCardsList)
            commonCards.put(commonCard, true);
    }

    /**
     * Initializes the player with a JSON string.
     * @author Giorgio Massimo Fontanive
     * @param jsonState The string containing the needed information.
     * @param commonCardsList Common cards.
     */
    public Player(String jsonState, List<CommonCard> commonCardsList) {
        nickname = "";
        shelf = new Shelf();
        personalCard = null;
        isFirstPlayer = false;
        isConnected = true;
        commonCards = new HashMap<>();
        for (CommonCard commonCard : commonCardsList)
            commonCards.put(commonCard, true);
        loadState(JsonParser.parseString(jsonState).getAsJsonObject());
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
     * Getter for player's commonCardPoints.
     * @author Niccolò Galante
     * @return Player's commonCardPoints.
     */
    public int getPoints() {

        //Checks if tiles of same type are adjacent
        int adjacentPoints = 0;
        for (Token tokenType : Token.values())
            if(tokenType != Token.NOTHING)
             adjacentPoints += TokenTools.counterIslandType(tokenType, shelf.getTiles(), true);

        return commonCardPoints + adjacentPoints + personalCard.getPoints(shelf.getTiles());
    }

    /**
     * Updates player's commonCardPoints.
     * @author Niccolò Galante.
     */
    private void updateCommonCardPoints() {
        int cardPoints = 0;
        for (Card card : commonCards.keySet()) {
            if (commonCards.get(card)){
                cardPoints = card.getPoints(shelf.getTiles());
                if (cardPoints != 0)
                    commonCards.replace(card, false);
            }
            commonCardPoints += cardPoints;
        }
    }

    /**
     * Inserts tokens.
     * @param tokens Tokens to insert in shelf.
     * @param column Column in which tokens are to be inserted.
     * @author Niccolò Galante.
     */
    public void insertTokens(Token[] tokens, int column) throws IllegalColumnException {
        int tokensInserted = 0;
        for (Token t: tokens)
            if(!t.equals(Token.NOTHING))
                try {
                    shelf.insertToken(t, column);
                    tokensInserted++;
                } catch(IllegalColumnException e) {
                    for(int i = 0; i < tokensInserted; i++)
                        shelf.removeToken(column);
                    throw new IllegalColumnException("The column is full!");
                }
        updateCommonCardPoints();
    }

    /**
     * Checks whether there's empty tiles in the player's shelf
     * @author Giorgio Massimo Fontanive
     * @return True if there's no more empty tiles.
     */
    public boolean isShelfFull() {
        if (shelf.isFull()) {
            commonCardPoints++;
            return true;
        }
        return false;
    }

    @Override
    public JsonObject getState() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickname", nickname);
        jsonObject.add("shelf", shelf.getState());
        jsonObject.addProperty("totalPoints", getPoints());
        jsonObject.addProperty("isFirstPlayer", isFirstPlayer);
        jsonObject.addProperty("personalCardIndex", personalCard.getIndex());
        jsonObject.addProperty("commonCardPoints", commonCardPoints);
        jsonObject.add("personalCard", personalCard.getState());
        int i = 0;
        for (Card card : commonCards.keySet()) {
            jsonObject.addProperty("completedCommonCard" + i, commonCards.get(card));
            i++;
        }
        return jsonObject;
    }

    @Override
    public void loadState(JsonObject jsonObject) {
        Map<String, JsonElement> elements = jsonObject.asMap();
        nickname = elements.get("nickname").getAsString();
        personalCard = new PersonalCard(elements.get("personalCardIndex").getAsInt());
        commonCardPoints = elements.get("commonCardPoints").getAsInt();
        isFirstPlayer = elements.get("isFirstPlayer").getAsBoolean();
        shelf = new Shelf();
        shelf.loadState(elements.get("shelf").getAsJsonObject());

        //Loads which cards have already been completed
        int i = 0;
        for (Card card : commonCards.keySet()) {
            boolean completed = elements.get("completedCommonCard" + i).getAsBoolean();
            commonCards.replace(card, completed);
            i++;
        }
    }
}
