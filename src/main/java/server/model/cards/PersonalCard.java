package server.model.cards;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.IOUtils;
import server.controller.utilities.ConfigLoader;
import server.controller.utilities.JsonTools;
import server.model.Token;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static server.controller.utilities.ConfigLoader.*;

/**
 * Personal objective cards.
 * @author Niccolò Galante
 */
public class PersonalCard extends Card {
    int index;
    private final Token[][] correctTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];

    /**
     * Class constructor.
     * @author Niccolò Galante
     */
    public PersonalCard(int index) {
        this.index = index;
        int[][] intTiles;
        String jsonFile = "";
        String jsonFilePath = "";
        JsonReader jsonReader;
        switch (index) {
            case 1 -> jsonFilePath = "/personalcards/PersonalGoal1.json";
            case 2 -> jsonFilePath = "/personalcards/PersonalGoal2.json";
            case 3 -> jsonFilePath = "/personalcards/PersonalGoal3.json";
            case 4 -> jsonFilePath = "/personalcards/PersonalGoal4.json";
            case 5 -> jsonFilePath = "/personalcards/PersonalGoal5.json";
            case 6 -> jsonFilePath = "/personalcards/PersonalGoal6.json";
            case 7 -> jsonFilePath = "/personalcards/PersonalGoal7.json";
            case 8 -> jsonFilePath = "/personalcards/PersonalGoal8.json";
            case 9 -> jsonFilePath = "/personalcards/PersonalGoal9.json";
            case 10 -> jsonFilePath = "/personalcards/PersonalGoal10.json";
            case 11 -> jsonFilePath = "/personalcards/PersonalGoal11.json";
            case 12 -> jsonFilePath = "/personalcards/PersonalGoal12.json";
        }
        try {
            InputStream inputStream = ConfigLoader.class.getResourceAsStream(jsonFilePath);
            jsonFile = IOUtils.toString(inputStream);
            //jsonFile = Files.readString(Paths.get(jsonFilePath));
            jsonReader = new JsonReader(new StringReader(jsonFile));
            jsonReader.beginObject();
            jsonReader.nextName();
            intTiles = JsonTools.readMatrix(jsonReader);
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i< SHELF_ROWS; i++)
            for (int j = 0; j< SHELF_COLUMNS; j++)
                correctTiles[i][j] = Token.values()[intTiles[i][j]];
    }

    /**
     * Returns points from cards.
     * @author Niccolò Galante
     * @param shelf A matrix of Tokens taken from a player's shelf.
     * @return number of points based on number of tokens correctly placed.
     */
    public int getPoints(Token[][] shelf) {
        int countCorrect = 0;
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                if(shelf[i][j] != Token.NOTHING && shelf[i][j] == correctTiles[i][j])
                    countCorrect++;
        return (countCorrect == 0 ? 0 : PERSONALCARD_POINTS[countCorrect - 1]);
    }

    /**
     * Returns tiles from personal card.
     * @author Niccolò Galante
     * @return tiles from personal card.
     */
    public Token[][] getCorrectTiles() {
        return correctTiles;
    }

    public int getIndex() {
        return index;
    }

    public JsonObject getState() {
        JsonObject jsonObject = new JsonObject();
        int[][] correctTilesIndex = new int[SHELF_ROWS][SHELF_COLUMNS];
        for (int i = 0; i < SHELF_ROWS; i++)
            for (int j = 0; j < SHELF_COLUMNS; j++)
                correctTilesIndex[i][j] = correctTiles[i][j].ordinal();
        jsonObject.add("correctTiles", JsonTools.createJsonMatrix(correctTilesIndex));
        jsonObject.addProperty("cardIndex", index);
        return jsonObject;
    }

    @Override
    public void loadState(JsonObject jsonObject) {
        //Useless
    }
}
