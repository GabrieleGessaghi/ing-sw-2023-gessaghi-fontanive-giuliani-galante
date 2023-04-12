package controller;

import com.google.gson.stream.JsonReader;
import model.Game;

import java.io.IOException;
import java.io.StringReader;

import static controller.Configurations.*;

/**
 * Receives data for a new turn and acts once it's ready.
 * @author Giorgio Massimo Fontanive
 */
public class TurnController implements Observer {
    private int[][] selectedTiles;
    private int selectedColumn;
    private final Game game;

    public TurnController(Game game) {
        selectedTiles = new int[SHELF_ROWS][SHELF_COLUMNS];
        selectedColumn = -1;
        this.game = game;
    }

    /**
     * Checks whether the saved matrix contains a valid number in each cell.
     * @return True if the matrix represents a possible move.
     */
    private boolean isMatrixLegal() {
        boolean matrixFlag = true;
        for (int i = 0; i < SHELF_ROWS && matrixFlag; i++)
            for (int j = 0; j < SHELF_COLUMNS && matrixFlag; j++)
                if (selectedTiles[i][j] < -1 || selectedTiles[i][j] > MAX_TOKENS_PER_TURN)
                    matrixFlag = false;
        return matrixFlag;
    }

    /**
     * Checks whether the saved integer contains a valid column index.
     * @author Giorgio Massimo Fontanive
     * @return True if the integer represents a column.
     */
    private boolean isColumnLegal() {
        return selectedColumn >= 0 && selectedColumn <= SHELF_COLUMNS;
    }

    /**
     * Reads the JSON message and makes a new turn once all data is ready.
     * @author Giorgio Massimo Fontanive
     * @param event The event received from the observable object to which this is subscribed.
     */
    public void update(Event event) {
        String jsonMessage = event.getJsonMessage();
        String field;
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "selectedTiles" -> selectedTiles = readMatrix(jsonReader);
                    case "selectedColumn" -> selectedColumn = jsonReader.nextInt();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (isMatrixLegal() && isColumnLegal())
            game.playerTurn(selectedTiles, selectedColumn);
        //TODO: Maybe throw an exception
    }
}
