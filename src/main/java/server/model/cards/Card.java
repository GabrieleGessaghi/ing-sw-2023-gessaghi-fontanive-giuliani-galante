package server.model.cards;

import server.controller.observer.Event;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.model.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Each card calculates the number of points a given matrix of tokens will obtain.
 * @author Niccolò Giuliani
 */
public abstract class Card implements Observable {
    protected List<Observer> observers;

    public Card() {
        observers = new ArrayList<>();
    }

    /**
     * Returns the number of points the given shelf will get.
     * @author Niccolò Giuliani
     * @param shelf A matrix of Tokens taken from a player's shelf.
     * @return The number of points this shelf would get.
     */
    public abstract int getPoints(Token[][]shelf);

    /**
     * Gets the card's index in their respective set.
     * @return The card's index.
     */
    public abstract int getIndex();

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
}
