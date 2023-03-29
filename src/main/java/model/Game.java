package model;

import model.board.Board;
import model.cards.CommonCard;
import model.cards.CommonObjective;
import model.cards.CommonType;
import model.cards.concreteobjectives.*;
import model.chat.Chat;
import model.exceptions.FullColumnException;
import model.exceptions.IllegalMoveException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

        genCommonCard(numberOfPlayers);;

        for (int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(playerNicknames.get(i));
        currentPlayerIndex = 0;
    }

    /**
     * Generates two random commonCard for the current game.
     * @author Gabriele Gessaghi
     * @param numberOfPlayers is the number of player for the current game.
     * @return a list with the commonCard objects.
     */
    private ArrayList<CommonCard> genCommonCard (int numberOfPlayers){
        ArrayList<CommonType> types = new ArrayList<CommonType>(Arrays.asList(CommonType.values()));

        Collections.shuffle(types);
        CommonType commonType1 = types.get(0);
        CommonType commonType2 = types.get(1);
        CommonObjective commonObj1 = null;
        CommonObjective commonObj2 = null;

        switch (commonType1){
            case STAIRS -> commonObj1 = new Stairs();
            case XSHAPE -> commonObj1 = new XShape();
            case CORNERS -> commonObj1 = new Corners();
            case TWOROWS -> commonObj1 = new TwoRows();
            case DIAGONAL -> commonObj1 = new Diagonal();
            case EIGHTANY -> commonObj1 = new Eightany();
            case FOURROWS -> commonObj1 = new FourRows();
            case SIXGROUPS -> commonObj1 = new SixGroups();
            case FOURGROUPS -> commonObj1 = new FourGroups();
            case TWOCOLUMNS -> commonObj1 = new TwoColumns();
            case TWOSQUARES -> commonObj1 = new TwoSquares();
            case THREECOLUMNS -> commonObj1 = new ThreeColumns();
        }

        switch (commonType2){
            case STAIRS -> commonObj2 = new Stairs();
            case XSHAPE -> commonObj2 = new XShape();
            case CORNERS -> commonObj2 = new Corners();
            case TWOROWS -> commonObj2 = new TwoRows();
            case DIAGONAL -> commonObj2 = new Diagonal();
            case EIGHTANY -> commonObj2 = new Eightany();
            case FOURROWS -> commonObj2 = new FourRows();
            case SIXGROUPS -> commonObj2 = new SixGroups();
            case FOURGROUPS -> commonObj2 = new FourGroups();
            case TWOCOLUMNS -> commonObj2 = new TwoColumns();
            case TWOSQUARES -> commonObj2 = new TwoSquares();
            case THREECOLUMNS -> commonObj2 = new ThreeColumns();
        }

        ArrayList <CommonCard> commonCards = new ArrayList<>();
        commonCards.add(new CommonCard(commonObj1, numberOfPlayers));
        commonCards.add(new CommonCard(commonObj2, numberOfPlayers));

        return commonCards;
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
    public void playerTurn (int [][] selectedTiles, int column){
        if (players[currentPlayerIndex].isConnected) {
            Token[] selectedTokens = null;
            try{
                selectedTokens = board.selectTiles(selectedTiles);
            }catch (IllegalMoveException e){
                System.out.println("The selected tokens are not valid!");
                return;
            }
            boolean [][] isSelected = new boolean[selectedTiles.length][selectedTiles[0].length];
            for (int i=0; i< isSelected.length; i++){
                for (int j=0; j< isSelected[0].length; j++){
                    if (selectedTiles[i][j]!=0)
                        isSelected[i][j] = true;
                }
            }
            board.removeTiles(isSelected);

            try{
                players[currentPlayerIndex].insertTokens(selectedTokens, column);
            }catch (FullColumnException e1) {
                System.out.println("The selected column can't store all the selected tokens!");
                return;
            }

            //If is all ok
            currentPlayerIndex++;
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
