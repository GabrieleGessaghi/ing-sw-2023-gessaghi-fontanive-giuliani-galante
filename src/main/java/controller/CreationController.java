package controller;

import model.Game;

/**
 * Controller of the creation of the game
 * @author Niccolò Giuliani
 */
public class CreationController implements Observer {
    private int playersNumber;
    private String[] playersNicknames;
    private boolean isGameReady;


    @Override
    public void update(Event event) {

    }

    /**
     * getter of the state of the game (ready or not ready)
     * @return true if game is ready
     * @author Niccolò Giuliani
     */
    public boolean getIsGameReady(){
        return true;
    }

    /**
     * method to create the Game@return
     * @return the Game created
     * @author Niccolò Giuliani
     */
    public  Game createGame(){
        return null;
    }

    /**
     * set the number of the players
     * @author Niccolò Giuliani
     */
    private void setPlayerNumber(){

    }

    /**
     * method to add a player
     * @param nickname nickname of the player to add
     * @author Niccolò Giuliani
     */
    private void addPlayer(String nickname){

    }
}
