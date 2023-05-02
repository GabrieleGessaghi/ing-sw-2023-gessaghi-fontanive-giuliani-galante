package server.model.cards;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.model.Savable;
import server.model.Token;
import server.model.cards.concreteobjectives.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static server.controller.utilities.ConfigLoader.COMMONCARD_POINTS;
import static server.controller.utilities.ConfigLoader.PLAYERS_MIN;

/**
 * Common objective card of the game.
 * @author Niccolò Giuliani
 */
public class CommonCard extends Card implements Savable {
    private int numberOfTokensLeft;
    private final int numberOfPlayers;
    private CommonObjective objective;
    private CommonType name;

    /**
     * Class constructor.
     * @author Niccolò Giuliani
     * @param type The algorithm for this card.
     * @param numberOfPlayers The number of players playing this game.
     */
    public CommonCard(CommonType type, int numberOfPlayers) {
        this.objective = createCommonObjective(type);
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfTokensLeft = numberOfPlayers;
        this.name = objective.getName();
        observers = new ArrayList<>();
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
            updateObservers(new Event(getState().toString()));
            numberOfTokensLeft--;


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
        jsonObject.addProperty("objectiveType", name.ordinal());
        jsonObject.addProperty("objectiveDescription", objective.getDescription());
        jsonObject.addProperty("numberOfTokensLeft", numberOfTokensLeft);
        jsonObject.addProperty("nextPointsAvailable", COMMONCARD_POINTS[numberOfPlayers - PLAYERS_MIN][numberOfPlayers - numberOfTokensLeft]);
        return jsonObject;
    }

    @Override
    public void loadState(JsonObject jsonObject) {
        Map<String, JsonElement> elements = jsonObject.asMap();
        numberOfTokensLeft = elements.get("numberOfTokensLeft").getAsInt();
        name = CommonType.values()[(elements.get("objectiveType").getAsInt())];
        objective = createCommonObjective(name);
    }
}
