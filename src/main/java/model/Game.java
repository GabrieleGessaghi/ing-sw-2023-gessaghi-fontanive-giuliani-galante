package model;

import model.chat.Chat;
import java.util.List;

public class Game {

    private String currentPlayer;
    private Board gameBoard;
    private final Player [] playerList;
    private Chat messages;
    /**
     * Class constructor
     * @author Gabriele Gessaghi
     */
    public Game (int numberOfPlayers) {
        playerList = new Player[numberOfPlayers];
    }

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
