package model;

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
    private final String gameID;
    private int currentPlayerIndex;
    private final Board board;
    private final Player[] players;
    private final Chat chat;

    /**
     * Class constructor, currentPlayer is set to player with firstPlayer true.
     * @author Gabriele Gessaghi
     * @param numberOfPlayers is the number of players of the game session.
     * @param playerNicknames a list of all the players' nicknames
     */
    public Game(int numberOfPlayers, ArrayList<String> playerNicknames) {
        chat = new Chat();
        players = new Player[numberOfPlayers];
        board = new Board(numberOfPlayers);
        Configurations.loadConfiguration("/src/main/resources/configuration.json");

        //File creation with unique name
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            Date now = new Date();
            String input = now.toString();
            byte[] hash = md.digest(input.getBytes());
            int code = Math.abs(Arrays.hashCode(hash)) % 10000;
            gameID = String.format("%04d", code);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        genCommonCard(numberOfPlayers);
        //TODO: Add personal card generation
        for (int i = 0; i < numberOfPlayers; i++)
            //TODO: Add personal and common cards as parameters
            players[i] = new Player(playerNicknames.get(i),false,null,null);
        currentPlayerIndex = 0;
    }

    /**
     * Generates two random commonCard for the current game.
     * @author Gabriele Gessaghi
     * @param numberOfPlayers The number of player for the current game.
     */
    private ArrayList<CommonCard> genCommonCard (int numberOfPlayers){
        ArrayList<CommonType> types = new ArrayList<CommonType>(Arrays.asList(CommonType.values()));
        //TODO: Avoid duplicating code
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
     * @throws IOException When there's an error in the file creation.
     */
    private void saveGame() throws IOException {
        String fileName = String.format("/game-%s.txt",gameID);
        FileOutputStream fout = new FileOutputStream(new File(fileName));
        ObjectOutputStream out = new ObjectOutputStream(fout);
        //TODO: Ensure that everything is saved, not just the Game class
        out.writeObject(this);
        out.close();
        fout.close();
    }

    /**
     * Collect the selected tiles from the game board and update the current player shelf.
     * @author Gabriele Gessaghi
     * @param selectedTiles A matrix with -1 for the tiles not chosen and
     *                      the order of choice for the other ones.
     * @param column The column on which the player wants to put the new tiles.
     */
    public void playerTurn (int [][] selectedTiles, int column){
        if (players[currentPlayerIndex].isConnected) {

            //Gets the tiles from the board.
            Token[] selectedTokens;
            try{
                selectedTokens = board.selectTiles(selectedTiles);
            }catch (IllegalMoveException e1){
                System.out.println("The selected tokens are not valid!");
                return;
            }

            //Inserts the tokens in the shelf.
            try{
                players[currentPlayerIndex].insertTokens(selectedTokens, column);
            }catch (FullColumnException e2) {
                System.out.println("The selected column can't store all the selected tokens!");
                return;
            }

            //Removes tiles from the board.
            boolean [][] isSelected = new boolean[selectedTiles.length][selectedTiles[0].length];
            for (int i = 0; i < isSelected.length; i++){
                for (int j = 0; j < isSelected[0].length; j++){
                    if (selectedTiles[i][j] != -1)
                        isSelected[i][j] = true;
                }
            }
            board.removeTiles(isSelected);
        }
        currentPlayerIndex++;
    }

    /**
     * End the game and return the winner.
     * @author Gabriele Gessaghi
     * @return The nickname of the winner.
     */
    public String endGame() { return "";}

    /**
     * Reload a saved state of a previous game.
     * @author Gabriele Gessaghi
     */
    public void loadGame() {}
}
