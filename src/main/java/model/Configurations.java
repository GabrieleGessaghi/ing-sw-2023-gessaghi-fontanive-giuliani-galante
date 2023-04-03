package model;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles configuration constants.
 * @author Giorgio Massimo Fontanive
*/
public class Configurations {
    public static int BOARD_SIZE;
    public static int SHELF_ROWS;
    public static int SHELF_COLUMNS;
    public static int MAX_TOKENS_PER_TURN;
    public static int PLAYERS_MIN;
    public static int PLAYERS_MAX;
    public static int[][] COMMONCARD_POINTS;

    /**
     * Initializes all constants.
     * @author Giorgio Massimo Fontanive
     * @param configurationFilePath The path of the json configuration file.
     */
    public static void loadConfiguration(String configurationFilePath) {
        String jsonFile;
        String field;
        try {
            jsonFile = Files.readString(Paths.get(configurationFilePath));
            JsonReader jsonReader = new JsonReader(new StringReader(jsonFile));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "BOARD_SIZE" -> BOARD_SIZE = jsonReader.nextInt();
                    case "MAX_TOKENS_PER_TURN" -> MAX_TOKENS_PER_TURN = jsonReader.nextInt();
                    case "SHELF_ROWS" -> SHELF_ROWS = jsonReader.nextInt();
                    case "SHELF_COLUMNS" -> SHELF_COLUMNS = jsonReader.nextInt();
                    case "PLAYERS_MIN" -> PLAYERS_MIN = jsonReader.nextInt();
                    case "PLAYERS_MAX" -> PLAYERS_MAX = jsonReader.nextInt();
                    case "COMMONCARD_POINTS" -> COMMONCARD_POINTS = readMatrix(jsonReader);
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a matrix from a json file.
     * @author Giorgio Massimo Fontanive
     * @param jsonReader The json reader being used.
     * @return The matrix read from the json file.
     * @throws IOException When the reader has an issue.
     */
    public static int[][] readMatrix(JsonReader jsonReader) throws IOException {
        int[][] matrix = new int[0][0];

        return matrix;
    }
}
