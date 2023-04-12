package controller;

import com.google.gson.stream.JsonReader;
import model.Game;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import static controller.Configurations.*;

/**
 * Receives data for a new turn
 * @author Giorgio Massimo Fontanive
 */
public class TurnController implements Observer{
    private int[][] selectedTiles;
    private int selectedColumn;
    private final Game game;

    public TurnController(Game game) {
        selectedTiles = new int[SHELF_ROWS][SHELF_COLUMNS];
        selectedColumn = -1;
        this.game = game;
    }

    private boolean isMatrixLegal() {
        boolean cellFlag;
        boolean matrixFlag = true;
        boolean[] cellValues = new boolean[MAX_TOKENS_PER_TURN + 1];
        for (int i = 0; i < SHELF_ROWS && matrixFlag; i++)
            for (int j = 0; j < SHELF_COLUMNS && matrixFlag; j++) {
                Arrays.fill(cellValues, true);
                cellFlag = false;
                for (int k = 0; k < MAX_TOKENS_PER_TURN; k++)
                    if (selectedTiles[i][j] != k - 1)
                        cellValues[k] = false;
                for (int k = 0; k < MAX_TOKENS_PER_TURN && !cellFlag; k++)
                    if (cellValues[k])
                        cellFlag = true;
                matrixFlag = cellFlag;
            }
        return matrixFlag;
    }

    private boolean isColumnLegal() {
        return selectedColumn >= 0 && selectedColumn <= SHELF_COLUMNS;
    }

    private boolean isTurnReady() {
        return isMatrixLegal() && isColumnLegal();
    }

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

        if (isTurnReady())
            game.playerTurn(selectedTiles, selectedColumn);
    }
}
