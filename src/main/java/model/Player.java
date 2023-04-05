package model;

import model.cards.Card;
import model.cards.CommonCard;
import model.cards.PersonalCard;
import model.exceptions.FullColumnException;
import model.Token;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static model.Configurations.SHELF_COLUMNS;
import static model.Configurations.SHELF_ROWS;

/**
 * Handles players' shelves and cards.
 * @author Niccolò Galante
 */
public class Player implements Serializable {
    private final static int NUMBER_OF_CARDS = 3;
    private final String nickname;
    private int points;
    private final boolean isFirstPlayer;
    private List<Card> cards;
    private final Shelf playerShelf;
    public boolean isConnected;
    public boolean[] isComplete;
    private int adjacentPoints;
    private int cellsPerIsland;

    /**
     * Class constructor.
     * @author Niccolò Galante
     * @param nickname Player's nickname.
     * @param isFirstPlayer Indicates whether player is first or not.
     * @param personal Player's personal card.
     * @param common Common cards.
     */
    public Player(String nickname, boolean isFirstPlayer, PersonalCard personal, List<CommonCard> common) {
        cards = new ArrayList<>();
        playerShelf = new Shelf();
        this.nickname = nickname;
        this.isFirstPlayer = isFirstPlayer;
        cards.add(personal);
        cards.add(common.get(0));
        cards.add(common.get(1));
    }

    /**
     * Getter for player's nickname.
     * @author Niccolò Galante
     * @return Player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for player's points.
     * @author Niccolò Galante
     * @return Player's points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for boolean to indicate if player is first.
     * @author Niccolò Galante
     * @return True if player is first.
     */
    public boolean getIsFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * Updates player's points.
     * @author Niccolò Galante.
     */
    public void updatePoints() {
        int tempPoints = points - adjacentPoints;
        isComplete = new boolean[NUMBER_OF_CARDS];

        //Checks if card objectives have been reached
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            if (!isComplete[i] && cards.get(i).getPoints(playerShelf.getTiles()) != 0) {
                isComplete[i] = true;
                tempPoints += cards.get(i).getPoints(playerShelf.getTiles());
            }
        }

        //Checks if tiles of same type are adjacent
        adjacentPoints = 0;
        adjacentPoints = adjPointsPerType(Token.CAT, playerShelf.getTiles()) + adjPointsPerType(Token.BOOK, playerShelf.getTiles()) +
                adjPointsPerType(Token.TOY, playerShelf.getTiles()) + adjPointsPerType(Token.TROPHY, playerShelf.getTiles()) +
                adjPointsPerType(Token.FRAME, playerShelf.getTiles()) + adjPointsPerType(Token.PLANT, playerShelf.getTiles());
        points = tempPoints + adjacentPoints;
        //TODO: Check if player arrives at endgame first (adds 1 point)
    }

    /**
     * Counts points for adjacent tokens of same type.
     * @author Niccolò Galante
     * @param type Type of token.
     * @param shelf Player's shelf.
     * @return Points for particular type.
     */
    private int adjPointsPerType(Token type, Token[][] shelf){
        int pointsPerType= 0; //counter of points to be added
        cellsPerIsland = 0; // counts number of cells in island
        boolean[][] checked = new boolean[SHELF_ROWS][SHELF_COLUMNS];
        for (int i = 0; i < SHELF_ROWS; i++)
            for (int j = 0; j < SHELF_COLUMNS; j++)
                checked[i][j] = false;
        for (int i = 0; i < SHELF_ROWS; i++)
            for (int j = 0; j < SHELF_COLUMNS; j++)
                if (shelf[i][j] == type && !checked[i][j]){
                    cellsPerIsland = 1;
                    findIsland(shelf, i, j, checked, type);
                    if(cellsPerIsland >= 6)
                        pointsPerType += 8;
                    else if(cellsPerIsland == 5)
                        pointsPerType += 5;
                    else if(cellsPerIsland == 4)
                        pointsPerType += 3;
                    else if(cellsPerIsland == 3)
                        pointsPerType += 2;
                }
        //TODO: Take these values from configuration file (Giorgio)
        return pointsPerType;
    }

    /**
     * Finds islands starting from an unchecked cell.
     * @author Niccolò Galante
     * @param shelf Player's shelf.
     * @param row Row of the first cell.
     * @param col Column of the first cell.
     * @param checked Matrix of checked cells.
     * @param type Type of token to check for.
     */
    private void findIsland(Token[][] shelf, int row, int col, boolean[][] checked, Token type) {
        //arrays for the position of the neighbors
        int[] rowIndex = new int[] {-1, 0, 0, 1 };
        int[] colIndex = new int[] { 0,-1, 1, 0 };

        checked[row][col] = true;

        for (int i = 0; i < 4; i++)
            if (isOk(shelf, row + rowIndex[i], col + colIndex[i],
                    checked,  type)) {
                cellsPerIsland++;
                findIsland(shelf, row + rowIndex[i], col + colIndex[i], checked, type);
            }
    }
    /**
     * Checks if the cell is equal to the Token type.
     * @author Niccolò Galante
     * @param shelf Player's shelf.
     * @param row Row of cell to check.
     * @param col Column of cell to check.
     * @param checked Matrix of check cells.
     * @param type Type of token.
     * @return true if the cell is equal the token type
     */
    boolean isOk(Token[][] shelf, int row, int col, boolean[][] checked, Token type) {
        return (row >= 0) && (row < SHELF_ROWS) && (col >= 0) && (col < SHELF_COLUMNS)
                && (shelf[row][col] == type && !checked[row][col]);
    }

    /**
     * Inserts tokens.
     * @param tokens Tokens to insert in shelf.
     * @param column Column in which tokens are to be inserted.
     * @author Niccolò Galante.
     */
    public void insertTokens(Token[] tokens, int column) throws FullColumnException {
        int tokensInserted = 0;
        for (Token t: tokens)
            if(!t.equals(Token.NOTHING))
                try {
                    playerShelf.insertToken(t, column);
                    tokensInserted++;
                } catch(FullColumnException e) {
                    for(int i = 0; i < tokensInserted; i++)
                        playerShelf.removeToken(column);
                }
        updatePoints();
    }
}
