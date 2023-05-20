package server.controller;

import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.model.exceptions.IllegalColumnException;
import server.model.exceptions.IllegalMoveException;
import server.view.ClientHandler;

import java.io.IOException;
import java.io.StringReader;

import static server.controller.utilities.ConfigLoader.*;

/**
 * Receives data for a new turn and acts once it's ready.
 * @author Giorgio Massimo Fontanive
 */
public class TurnController implements Observer {
    private int[][] selectedTiles;
    private int selectedColumn;
    private final Game game;
    private final ClientHandler currentClientHandler;

    public TurnController(Game game, ClientHandler currentClient) {
        selectedTiles = null;
        selectedColumn = -1;
        currentClientHandler = currentClient;
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
                if (selectedTiles[i][j] < -1 || selectedTiles[i][j] > MAX_TOKENS_PER_TURN - 1)
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

    public synchronized void newTurn() {
        currentClientHandler.requestInput(Prompt.TOKENS);
        while (selectedTiles == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Received tokens"); //

        currentClientHandler.requestInput(Prompt.COLUMN);
        while (selectedColumn == -1) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Received column"); //

        if (isMatrixLegal() && isColumnLegal())
            finalizeTurn();
    }

    private synchronized void finalizeTurn() {
        try {
            game.playerTurn(selectedTiles, selectedColumn);
        } catch (IllegalMoveException | IllegalColumnException e) {
            currentClientHandler.sendOutput(JsonTools.createMessage(e.getMessage()));
            selectedTiles = null;
            selectedColumn = -1;
            newTurn();
        }
    }

    /**
     * Reads the JSON message and makes a new turn once all data is ready.
     * @author Giorgio Massimo Fontanive
     * @param event The event received from the observable object to which this is subscribed.
     */
    public synchronized void update(Event event) {
        String jsonMessage = event.jsonMessage();
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
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            this.notifyAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
