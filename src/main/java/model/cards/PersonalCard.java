package model.cards;

import com.google.gson.stream.JsonReader;
import model.Board;
import model.Configurations;
import model.Token;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static model.Configurations.SHELF_COLUMNS;
import static model.Configurations.SHELF_ROWS;
import static model.Configurations.PERSONALCARD_POINTS;

/**
 * Personal objective cards.
 * @author Niccolò Galante
 */
public class PersonalCard extends Card implements Serializable {
    private Token[][] correctTiles;

    /**
     * Class constructor.
     * @author Niccolò Galante
     */
    public PersonalCard(int index) {
        String jsonFile = "";
        String jsonFilePath = "";
        JsonReader jsonReader;
        switch (index) {
            case 1 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal1.json";
            case 2 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal2.json";
            case 3 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal3.json";
            case 4 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal4.json";
            case 5 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal5.json";
            case 6 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal6.json";
            case 7 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal7.json";
            case 8 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal8.json";
            case 9 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal9.json";
            case 10 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoa10.json";
            case 11 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal11.json";
            case 12 -> jsonFilePath = "src/main/resources/PersonalCards/PersonalGoal12.json";


        }
        try {
            jsonFile = Files.readString(Paths.get(jsonFilePath));
            jsonReader = new JsonReader(new StringReader(jsonFile));
            jsonReader.beginObject();
            jsonReader.nextName();
            //correctTiles = Board.convertSelection(Configurations.readMatrix(jsonReader), 0);
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //reset();
        //TODO: Implement different personal cards through Json
    }

    /**
     *
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
}
