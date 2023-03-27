package model;

import model.chat.Chat;
import model.exceptions.FullColumnException;
import model.exceptions.IllegalMoveException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Game implements Serializable {

    private String gameID;
    private Player currentPlayer;
    private Board gameBoard;
    private final Player[] playerList;
    private Chat messages;
    /**
     * Class constructor, currentPlayer is set to player with firstPlayer true
     * @author Gabriele Gessaghi
     * @param numberOfPlayer is the number of player of the game session
     */
    public Game (int numberOfPlayer) {
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
        playerList = new Player[numberOfPlayer];
        gameBoard = new Board(numberOfPlayer);
        messages = new Chat();
        for (Player p : playerList)
            if (p.getIsFirstPlayer())
                currentPlayer = p;
    }

    /**
     * Save the game state in case of disconnections or other problems
     * @author Gabriele Gessaghi
     */
    private void saveGame () throws IOException {
        String fileName = String.format("/game-%s.txt",gameID);
        FileOutputStream fout = new FileOutputStream(new File(fileName));
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(this);
        out.close();
        fout.close();

    }

    /**
     * Collect the selected tiles from the game board and update the current player shelf
     * @author Gabriele Gessaghi
     */
    public void playerTurn (int [][] selectedTiles, int column) throws IllegalMoveException, FullColumnException {
        if (currentPlayer.isConnected) {
            Token [] selectedTokens = gameBoard.selectTiles(selectedTiles);
            currentPlayer.insertTokens(selectedTokens, column);
            //TODO : gestire qual'ora la colonna non possa ospitare tutte le tiles
            //TODO : gestire qual'ora le tiles selezionate non vadano bene
        }
    }

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
