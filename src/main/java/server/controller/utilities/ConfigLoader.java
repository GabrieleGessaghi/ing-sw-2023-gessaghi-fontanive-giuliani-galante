package server.controller.utilities;

import com.google.gson.stream.JsonReader;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles configuration constants.
 * @author Giorgio Massimo Fontanive
*/
public class ConfigLoader {
    public static int SERVER_PORT;
    public static int SHELF_ROWS;
    public static int SHELF_COLUMNS;
    public static int BOARD_SIZE;
    public static int PLAYERS_MIN;
    public static int PLAYERS_MAX;
    public static int MAX_TOKENS_PER_TURN;
    public static int NUMBER_OF_TOKEN_TYPES;
    public static int TOKENS_PER_TYPE;
    public static int NUMBER_OF_COMMON_CARDS;
    public static int[] PERSONALCARD_POINTS;
    public static int[][] COMMONCARD_POINTS;
    public static int PING_PERIOD;
    public static int LONE_PLAYER_WAIT;

    /**
     * Initializes all constants.
     * @author Giorgio Massimo Fontanive
     * @param configurationFilePath The path of the json configuration file.
     */
    public static void loadConfiguration(String configurationFilePath) {
        String jsonFile;
        String field;
        try {
            InputStream inputStream = ConfigLoader.class.getResourceAsStream(configurationFilePath);
            jsonFile = IOUtils.toString(inputStream);
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
                    case "COMMONCARD_POINTS" -> COMMONCARD_POINTS = JsonTools.readMatrix(jsonReader);
                    case "PERSONALCARD_POINTS" -> PERSONALCARD_POINTS = JsonTools.readArray(jsonReader);
                    case "NUMBER_OF_COMMON_CARDS" -> NUMBER_OF_COMMON_CARDS = jsonReader.nextInt();
                    case "SERVER_PORT" -> SERVER_PORT = jsonReader.nextInt();
                    case "PING_PERIOD" -> PING_PERIOD = jsonReader.nextInt();
                    case "LONE_PLAYER_WAIT" -> LONE_PLAYER_WAIT = jsonReader.nextInt();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
