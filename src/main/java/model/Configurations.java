package model;

/**
 * Configuration class.
 * @author Niccolò Giuliani
*/
public class Configurations {
    public static int BOARD_SIZE, MAX_TOKENS_PER_TURN,  MIN_PLAYERS, MAX_PLAYERS;
    public static int ROWS_SHELF=6;
    public static int COLUMNS_SHELF=5;
    public static int [][] COMMONCARD_POINTS;
    public Configurations(){
        COMMONCARD_POINTS = new int [3][4];
        COMMONCARD_POINTS[0][0] = 8;
        COMMONCARD_POINTS[0][1] = 4;
        COMMONCARD_POINTS[0][2] = 0;
        COMMONCARD_POINTS[0][3] = 0;
        COMMONCARD_POINTS[1][0] = 8;
        COMMONCARD_POINTS[1][1] = 6;
        COMMONCARD_POINTS[1][2] = 4;
        COMMONCARD_POINTS[1][3] = 0;
        COMMONCARD_POINTS[2][0] = 8;
        COMMONCARD_POINTS[2][1] = 6;
        COMMONCARD_POINTS[2][2] = 4;
        COMMONCARD_POINTS[2][3] = 2;
    }
    public int ok(){
        return 0;
    }

}
