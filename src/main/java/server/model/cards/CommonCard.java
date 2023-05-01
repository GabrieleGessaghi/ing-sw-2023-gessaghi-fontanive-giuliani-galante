package server.model.cards;

import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.model.Savable;
import server.model.Token;
import server.controller.utilities.ConfigLoader;
import server.model.cards.concreteobjectives.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common objective card of the game.
 * @author Niccolò Giuliani
 */
public class CommonCard extends Card implements Savable, Observable {
    private int numberOfTokensLeft;
    private int numberOfPlayers;
    private CommonObjective objective;
    private CommonType name;
    private final List<Observer> observers;

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

    public CommonCard(String jsonState) {
        loadState(jsonState);
        observers = new ArrayList<>();
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
            points = ConfigLoader.COMMONCARD_POINTS[numberOfPlayers - ConfigLoader.PLAYERS_MIN][numberOfPlayers - numberOfTokensLeft];
            numberOfTokensLeft--;
            updateObservers(new Event(getState()));
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
        elements.put("objectiveType", name.ordinal());
        elements.put("objectiveDescription", objective.getDescription());
        elements.put("numberOfTokensLeft", numberOfTokensLeft);
        elements.put("numberOfPlayers", numberOfPlayers);
        return JsonTools.createJson(elements).toString();
    }

    @Override
    public void loadState(String jsonMessage) {
        Map<String, Object> elements;
        elements = JsonTools.parseJson(jsonMessage);
        numberOfTokensLeft = (Integer) elements.get("numberOfTokensLeft");
        name = CommonType.values()[((Integer) elements.get("objectiveType"))];
        objective = createCommonObjective(name);
        numberOfPlayers = (Integer) elements.get("numberOfPlayers");
    }
}
