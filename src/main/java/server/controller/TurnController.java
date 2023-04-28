package server.controller;

import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.controller.utilities.ConfigLoader;

import java.io.IOException;
import java.io.StringReader;

/**
 * Receives data for a new turn and acts once it's ready.
 * @author Giorgio Massimo Fontanive
 */
public class TurnController implements Observer {
    private int[][] selectedTiles;
    private int selectedColumn;
    private final Game game;

    public TurnController(Game game) {
        selectedTiles = new int[ConfigLoader.SHELF_ROWS][ConfigLoader.SHELF_COLUMNS];
        selectedColumn = -1;
        this.game = game;
    }

    /**
     * Checks whether the saved matrix contains a valid number in each cell.
     * @return True if the matrix represents a possible move.
     */
    private boolean isMatrixLegal() {
        boolean matrixFlag = true;
        for (int i = 0; i < ConfigLoader.SHELF_ROWS && matrixFlag; i++)
            for (int j = 0; j < ConfigLoader.SHELF_COLUMNS && matrixFlag; j++)
                if (selectedTiles[i][j] < -1 || selectedTiles[i][j] > ConfigLoader.MAX_TOKENS_PER_TURN - 1)
                    matrixFlag = false;
        return matrixFlag;
    }

    /**
     * Checks whether the saved integer contains a valid column index.
     * @author Giorgio Massimo Fontanive
     * @return True if the integer represents a column.
     */
    private boolean isColumnLegal() {
        return selectedColumn >= 0 && selectedColumn <= ConfigLoader.SHELF_COLUMNS;
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
                    case "selectedTiles" -> selectedTiles = JsonTools.readMatrix(jsonReader);
                    case "selectedColumn" -> selectedColumn = jsonReader.nextInt();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (isMatrixLegal() && isColumnLegal()) {
            //TODO: Insert try catch
            game.playerTurn(selectedTiles, selectedColumn);
        }
        //TODO: Maybe throw an exception
    }

    public boolean isGameOver() {
        return game.endGame() == null;
    }
}
