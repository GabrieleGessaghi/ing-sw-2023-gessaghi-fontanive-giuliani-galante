package server.model.cards;

import com.google.gson.stream.JsonReader;
import server.controller.utilities.JsonTools;
import server.model.Token;
import server.controller.utilities.ConfigLoader;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Personal objective cards.
 * @author Niccolò Galante
 */
public class PersonalCard extends Card implements Serializable {
    private final Token[][] correctTiles = new Token[ConfigLoader.SHELF_ROWS][ConfigLoader.SHELF_COLUMNS];

    /**
     * Class constructor.
     * @author Niccolò Galante
     */
    public PersonalCard(int index) {
        int[][] intTiles;
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
            intTiles = JsonTools.readMatrix(jsonReader);
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i< ConfigLoader.SHELF_ROWS; i++)
            for (int j = 0; j< ConfigLoader.SHELF_COLUMNS; j++)
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
        for(int i = 0; i < ConfigLoader.SHELF_ROWS; i++)
            for(int j = 0; j < ConfigLoader.SHELF_COLUMNS; j++)
                if(shelf[i][j] != Token.NOTHING && shelf[i][j] == correctTiles[i][j])
                    countCorrect++;
        return (countCorrect == 0 ? 0 : ConfigLoader.PERSONALCARD_POINTS[countCorrect - 1]);
    }

    /**
     * Returns tiles from personal card.
     * @author Niccolò Galante
     * @return tiles from personal card.
     */
    public Token[][] getCorrectTiles() {
        return correctTiles;
    }

    //    /**
//     * Converts integer to token type.
//     * @author Niccolò Galante
//     * @param tile Tile that is being read from personal card.
//     * @return Token type.
//     */
//    public static Token intToToken(int tile){
//        Token type = Token.NOTHING;
//        switch (tile){
//            case 1 -> type = Token.CAT;
//            case 2 -> type = Token.BOOK;
//            case 3 -> type = Token.TOY;
//            case 4 -> type = Token.TROPHY;
//            case 5 -> type = Token.FRAME;
//            case 6 -> type = Token.PLANT;
//        }
//        return type;
//    }
}
