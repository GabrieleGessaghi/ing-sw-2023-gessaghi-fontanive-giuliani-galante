package server.model;

import com.google.gson.JsonObject;

/**
 * Allows the saving and loading an object's state via JSON strings.
 * @author Giorgio Massimo Fontanive
 */
public interface Savable {

    /**
     * Returns a JSON string containing all the necessary information.
     * @return The JSON string.
     */
    public JsonObject getState();

    /**
     * Loads all the necessary information from a JSON string.
     * @param jsonObject The JSON string from which to recover data.
     */
    public void loadState(JsonObject jsonObject);
}
