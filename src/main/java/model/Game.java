package model;

import model.chat.Chat;
import model.exceptions.FullColumnException;
import model.exceptions.IllegalMoveException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class Game implements Serializable {

    private String gameID;
    private int currentPlayerIndex;
    private Board board;
    private final Player[] players;
    private Chat chat;

    /**
     * Class constructor, currentPlayer is set to player with firstPlayer true.
     * @author Gabriele Gessaghi
     * @param numberOfPlayers is the number of players of the game session.
     * @param playerNicknames
     */
    public Game(int numberOfPlayers, ArrayList<String> playerNicknames) {
        chat = new Chat();
        players = new Player[numberOfPlayers];
        board = new Board(numberOfPlayers);

        //File creation
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            Date now = new Date();
            String input = now.toString();
            byte[] hash = md.digest(input.getBytes());
            int code = Math.abs(hash.hashCode()) % 10000;
            gameID = String.format("%04d", code);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        //TODO: Add cards and other thing for player initialization

        for (int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(playerNicknames.get(i));
        currentPlayerIndex = 0;
    }

    /**
     * Save the game state in case of disconnections or other problems.
     * @author Gabriele Gessaghi
     * @throws IOException
     */
    private void saveGame() throws IOException {
        String fileName = String.format("/game-%s.txt",gameID);
        FileOutputStream fout = new FileOutputStream(new File(fileName));
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(this);
        out.close();
        fout.close();
    }

    /**
     * Collect the selected tiles from the game board and update the current player shelf.
     * @author Gabriele Gessaghi
     * @param selectedTiles
     * @param column
     * @throws IllegalMoveException
     * @throws FullColumnException
     */
    public void playerTurn (int [][] selectedTiles, int column) throws IllegalMoveException, FullColumnException {
        if (players[currentPlayerIndex].isConnected) {
            Token[] selectedTokens = board.selectTiles(selectedTiles);
            players[currentPlayerIndex].insertTokens(selectedTokens, column);
            //TODO: Handle exceptions
        }
    }

    /**
     * End the game and return the winner.
     * @author Gabriele Gessaghi
     * @return the nickname of the winner.
     */
    public String endGame() { return "";}

    /**
     * Reload a saved state of a previous game.
     * @author Gabriele Gessaghi
     */
    public void loadGame() {}
}
