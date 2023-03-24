package model.cards;

import model.Token;

/**
 * @author Niccolò Giuliani
 * interface for common card
 */
public interface CommonObjective {
     static final int ROWS=6;
     static final int COLUMNS=5;

     /**
      * @author Niccolò Giuliani
      * @param shelf
      * @return 1 if the condition is respect, 0 otherwise
      */
     public int getPoints(Token[][] shelf) ;
     /**
      * @author Niccolò Giuliani
      * @return the name of the card
      */
     public CommonType name();
}
