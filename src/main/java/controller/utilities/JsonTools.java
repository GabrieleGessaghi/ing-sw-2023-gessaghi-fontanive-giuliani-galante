package controller.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import controller.exceptions.JsonCreationException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTools {

    private static void addProperty(JsonObject jsonObject, String field, Object property) throws JsonCreationException {
        if (property.getClass().equals(Integer.class))
            jsonObject.addProperty(field, (Integer) property);
        else if (property.getClass().equals(String.class))
            jsonObject.addProperty(field, (String) property);
        else if (property.getClass().equals(JsonArray.class))
            jsonObject.add(field, (JsonArray) property);
        else throw new JsonCreationException("Unknows property class!");
    }

    public static String createJson(Map<String, Object> elements) {
        JsonObject jsonObject = new JsonObject();
        for (String field : elements.keySet()) {
            try {
                addProperty(jsonObject, field, elements.get(field));
            } catch (JsonCreationException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonObject.toString();
    }

    public static HashMap<String, Object> parseJson(String jsonMessage) {
        HashMap<String, Object> elements = new HashMap<>();
        try {
            String field;
            JsonReader jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (jsonReader.peek()) {
                    case NUMBER -> elements.put(field, jsonReader.nextInt());
                    case STRING -> elements.put(field, jsonReader.nextString());
                    case BEGIN_ARRAY -> {
                        if (jsonReader.peek() == JsonToken.BEGIN_ARRAY)
                            elements.put(field, readMatrix(jsonReader));
                        else
                            elements.put(field, readArray(jsonReader));
                    }
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    public static JsonArray createJsonArray(int[] array) {
        JsonArray jsonArray = new JsonArray();
        for (int i : array)
            jsonArray.add(i);
        return jsonArray;
    }

    public static JsonArray createJsonMatrix(int[][] matrix) {
        JsonArray jsonArray = new JsonArray();
        for (int[] i : matrix)
            jsonArray.add(createJsonArray(i));
        return jsonArray;
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
        while (jsonReader.hasNext())
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
