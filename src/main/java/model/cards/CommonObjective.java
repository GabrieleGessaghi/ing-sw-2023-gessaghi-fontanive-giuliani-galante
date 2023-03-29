package model.cards;

import model.Token;

import static model.Configuration.COLUMNS_SHELF;
import static model.Configuration.ROWS_SHELF;

/**
 * Interface for implementing the strategy pattern.
 * @author Niccolò Giuliani
 */
public interface CommonObjective {
     //TODO: Move these to the shelf class
      int ROWS=ROWS_SHELF;
      int COLUMNS=COLUMNS_SHELF;

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
