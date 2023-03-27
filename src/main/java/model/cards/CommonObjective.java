package model.cards;

import model.Token;

/**
 * Interface for implementing the strategy pattern.
 * @author Niccolò Giuliani
 */
public interface CommonObjective {
     //TODO: Move these to the shelf class
     static final int ROWS=6;
     static final int COLUMNS=5;

     /**
      * Checks whether a player's shelf satisfies the card's objective.
      * @author Niccolò Giuliani
      * @param shelf a matrix of Tokens
      * @return true if the given shelf satisfies the algorithm.
      */
     public boolean isSatisfied(Token[][] shelf) ;

     /**
      * Getter for the card's name.
      * @author Niccolò Giuliani
      * @return the card's name.
      */
     public CommonType getName();
}
