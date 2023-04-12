package controller;

/**
 *
 * @author
 */
public class TurnController implements Observer{
    private int[][] selectedTiles;
    private int selectedColumn;

    public TurnController() {
        selectedTiles = new int[1][1];
        selectedColumn = -1;
    }

    public void update(Event event) {

    }
}
