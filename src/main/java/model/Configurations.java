package model;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles configuration constants.
 * @author Giorgio Massimo Fontanive
*/
public class Configurations {
    public static int SHELF_ROWS;
    public static int SHELF_COLUMNS;
    public static int BOARD_SIZE;
    public static int PLAYERS_MIN;
    public static int PLAYERS_MAX;
    public static int MAX_TOKENS_PER_TURN;
    public static int NUMBER_OF_TOKEN_TYPES;
    public static int TOKENS_PER_TYPE;
    public static int[] PERSONALCARD_POINTS;
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
                    case "SHELF_ROWS" -> SHELF_ROWS = jsonReader.nextInt();
                    case "SHELF_COLUMNS" -> SHELF_COLUMNS = jsonReader.nextInt();
                    case "PLAYERS_MIN" -> PLAYERS_MIN = jsonReader.nextInt();
                    case "PLAYERS_MAX" -> PLAYERS_MAX = jsonReader.nextInt();
                    case "MAX_TOKENS_PER_TURN" -> MAX_TOKENS_PER_TURN = jsonReader.nextInt();
                    case "NUMBER_OF_TOKEN_TYPES" -> NUMBER_OF_TOKEN_TYPES = jsonReader.nextInt();
                    case "TOKENS_PER_TYPE" -> TOKENS_PER_TYPE = jsonReader.nextInt();
                    case "COMMONCARD_POINTS" -> COMMONCARD_POINTS = readMatrix(jsonReader);
                    case "PERSONALCARD_POINTS" -> PERSONALCARD_POINTS = readArray(jsonReader);
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads an array from a json file.
     * @author Giorgio Massimo Fontanive
     * @param jsonReader The json reader being used. (Must have already started object)
     * @return The array read from the json file.
     * @throws IOException When the reader has an issue.
     */
    public static int[] readArray(JsonReader jsonReader) throws IOException {
        List<Integer> listArray= new ArrayList<>();
        jsonReader.beginArray();
        for (int i = 0; jsonReader.hasNext(); i++)
            listArray.add(jsonReader.nextInt());
        jsonReader.endArray();
        return listArray.stream().mapToInt(x -> x).toArray();
    }

    /**
     * Reads a matrix from a json file.
     * @author Giorgio Massimo Fontanive
     * @param jsonReader The json reader being used. (Must have already started object)
     * @return The matrix read from the json file.
     * @throws IOException When the reader has an issue.
     */
    public static int[][] readMatrix(JsonReader jsonReader) throws IOException {
        List<List<Integer>> listMatrix = new ArrayList<>();
        jsonReader.beginArray();
        for (int i = 0; jsonReader.hasNext(); i++) {
            listMatrix.add(new ArrayList<>());
            jsonReader.beginArray();
            while (jsonReader.hasNext())
                listMatrix.get(i).add(jsonReader.nextInt());
            jsonReader.endArray();
        }
        jsonReader.endArray();
        int[][] matrix = new int[listMatrix.size()][];
        for (int i = 0; i < listMatrix.size(); i++)
            matrix[i] = listMatrix.get(i).stream().mapToInt(x -> x).toArray();
        return matrix;
    }
}
