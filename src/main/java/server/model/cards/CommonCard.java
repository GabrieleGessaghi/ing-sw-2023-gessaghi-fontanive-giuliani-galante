package server.model.cards;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.observer.Event;
import server.model.Token;
import server.model.cards.concreteobjectives.*;

import java.util.ArrayList;
import java.util.Map;

import static server.controller.utilities.ConfigLoader.COMMONCARD_POINTS;
import static server.controller.utilities.ConfigLoader.PLAYERS_MIN;

/**
 * Common objective card of the game.
 * @author Niccolò Giuliani
 */
public class CommonCard extends Card {
    private int numberOfTokensLeft;
    private int currentIndex;
    private final int numberOfPlayers;
    private CommonObjective objective;
    private CommonType name;

    /**
     * Class constructor.
     * @author Niccolò Giuliani
     * @param type The algorithm for this card.
     * @param numberOfPlayers The number of players playing this game.
     */
    public CommonCard(CommonType type, int numberOfPlayers, int index) {
        this.objective = createCommonObjective(type);
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfTokensLeft = numberOfPlayers;
        this.name = objective.getName();
        observers = new ArrayList<>();
        currentIndex = index;
    }

    public CommonCard(String jsonState, int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        observers = new ArrayList<>();
        loadState(JsonParser.parseString(jsonState).getAsJsonObject());
    }

    /**
     * Gives players' the points they deserve and removes the points token.
     * @author Giorgio Massimo Fontanive
     * @param shelf A matrix of Tokens taken from a player's shelf.
     * @return The number of points the player gets.
     */
    public int getPoints(Token[][] shelf) {
        boolean satisfied;
        int points = 0;
        satisfied = objective.isSatisfied(shelf);
        if (satisfied) {
            points = COMMONCARD_POINTS[numberOfPlayers - PLAYERS_MIN][numberOfPlayers - numberOfTokensLeft];
            numberOfTokensLeft--;

            //Updates observers
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", "A common objective was completed!");
            jsonObject.add("commonCard" + currentIndex, getState());
            updateObservers(new Event(jsonObject.toString()));
        }
        return points;
    }

    public int getIndex() {
        return name.ordinal();
    }

    /**
     * generates the correct constructor for the given commonType
     * @author Gabriele Gessaghi
     * @param commonType is the commonType type
     * @return the correct constructor
     */
    private static CommonObjective createCommonObjective(CommonType commonType) {
        return switch (commonType) {
            case STAIRS -> new Stairs();
            case XSHAPE -> new XShape();
            case CORNERS -> new Corners();
            case TWOROWS -> new TwoRows();
            case DIAGONAL -> new Diagonal();
            case EIGHTANY -> new Eightany();
            case FOURROWS -> new FourRows();
            case SIXGROUPS -> new SixGroups();
            case FOURGROUPS -> new FourGroups();
            case TWOCOLUMNS -> new TwoColumns();
            case TWOSQUARES -> new TwoSquares();
            case THREECOLUMNS -> new ThreeColumns();
        };
    }

    @Override
    public JsonObject getState() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cardIndex", name.ordinal());
        jsonObject.addProperty("currentIndex", currentIndex);
        jsonObject.addProperty("objectiveDescription", objective.getDescription());
        jsonObject.addProperty("numberOfTokensLeft", numberOfTokensLeft);
        jsonObject.addProperty("nextPointsAvailable", COMMONCARD_POINTS[numberOfPlayers - PLAYERS_MIN][numberOfPlayers - numberOfTokensLeft]);
        return jsonObject;
    }

    @Override
    public void loadState(JsonObject jsonObject) {
        Map<String, JsonElement> elements = jsonObject.asMap();
        numberOfTokensLeft = elements.get("numberOfTokensLeft").getAsInt();
        currentIndex = elements.get("currentIndex").getAsInt();
        name = CommonType.values()[(elements.get("cardIndex").getAsInt())];
        objective = createCommonObjective(name);
    }
}
