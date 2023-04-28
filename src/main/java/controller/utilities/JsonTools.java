package controller.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonTools {

//    private void addProperty(JsonObject jsonObject, String field, Object property) {
//        if (property.getClass().equals(Integer.class))
//            jsonObject.addProperty(field, (Integer) property);
//        else if (property.getClass().equals(String.class))
//            jsonObject.addProperty(field, (String) property);
//        else if (property.getClass().equals(ArrayList.class)) {
//            ArrayList<Integer> list = (ArrayList<Integer>) property;
//            int[] array = list.stream().mapToInt(x -> x).toArray();
//            jsonObject.add(field, createJsonArray(array));
//        }
//    }

    public static String createJson(Map<String, Object> elements) {
        return null;
    }

    public static Map<String, Object> parseJson(String jsonMessage) {
        return null;
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
