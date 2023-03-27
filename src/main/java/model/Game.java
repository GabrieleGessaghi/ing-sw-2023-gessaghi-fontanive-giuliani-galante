package model;

import model.chat.Chat;

public class Game {

    private String currentPlayer;
    private Board gameBoard;
    //private final List <Player>;
    private Chat messages;
    /**
     * Class constructor
     * @author Gabriele Gessaghi
     */
    public Game () {}

    /**
     * Save the game state in case of disconnections or other problems
     * @author Gabriele Gessaghi
     */
    private void saveGame () {}

    /**
     * Collect the selected tiles from the game board and update the current player shelf
     * @author Gabriele Gessaghi
     */
    public void playerTurn (int [][] selectedTiles, int column) {}

    /**
     * end the game and return the winner
     * @author Gabriele Gessaghi
     */
    public String endGame () { return "tmp";}

    /**
     * reload a saved state of a previous game
     * @author Gabriele Gessaghi
     */
    public void loadGame () {}

}
