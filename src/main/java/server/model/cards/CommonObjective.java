package server.model.cards;

import server.model.Token;
import server.controller.utilities.ConfigLoader;

/**
 * Interface for implementing the strategy pattern.
 * @author Niccolò Giuliani
 */
public interface CommonObjective {

      //TODO: USE CONFIGURATION CONSTANTS
      int ROWS= ConfigLoader.SHELF_ROWS;
      int COLUMNS= ConfigLoader.SHELF_COLUMNS;

     /**
      * Checks whether a player's shelf satisfies the card's objective.
      * @author Niccolò Giuliani
      * @param shelf A matrix of Tokens
      * @return True if the given shelf satisfies the algorithm.
      */
     public boolean isSatisfied(Token[][] shelf) ;

     /**
      * Getter for the card's name.
      * @author Niccolò Giuliani
      * @return The card's name.
      */
     public CommonType getName();
}
