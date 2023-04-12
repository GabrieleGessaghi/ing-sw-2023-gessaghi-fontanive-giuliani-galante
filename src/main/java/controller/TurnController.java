package controller;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

import static controller.Configurations.*;

/**
 *
 * @author
 */
public class TurnController implements Observer{
    private int[][] selectedTiles;
    private int selectedColumn;

    public TurnController() {
        selectedTiles = new int[SHELF_ROWS][SHELF_COLUMNS];
        selectedColumn = -1;
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
    }
}
