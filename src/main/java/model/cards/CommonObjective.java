package model.cards;

import model.Token;

/**
 * interface for common card
 * @author Niccolò Giuliani
 */
public interface CommonObjective {
     static final int ROWS=6;
     static final int COLUMNS=5;

     /**
      * method to know if the objective is completed successfully
      * @author Niccolò Giuliani
      * @param shelf
      * @return 1 if the condition is respect, 0 otherwise
      */
     public int getPoints(Token[][] shelf) ;
     /**
      * method to know the name of the card
      * @author Niccolò Giuliani
      * @return the name of the card
      */
     public CommonType name();
}
