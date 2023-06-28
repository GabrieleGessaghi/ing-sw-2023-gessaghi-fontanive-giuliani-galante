package server.controller.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static server.controller.utilities.JsonTools.*;

class JsonToolsTest {
    @BeforeEach
    public void init(){
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
    }

    @Test
    public void createJsonArrayTest(){
        JsonArray jsonArrayTest = null;
        int[] elementsToInsert = new int[5];
        for(int i = 0; i < elementsToInsert.length; i++)
            elementsToInsert[i] = i;

        jsonArrayTest = createJsonArray(elementsToInsert);

        assertNotEquals(jsonArrayTest, null);
    }

    @Test
    public void createJsonMatrixTest(){
        JsonArray jsonArrayTest = null;
        int[][] elementsToInsert = new int[5][5];
        for(int i = 0; i < elementsToInsert.length; i++)
            for(int j = 0; j < elementsToInsert.length; j++)
                elementsToInsert[i][j] = i;


        jsonArrayTest = createJsonMatrix(elementsToInsert);

        assertNotEquals(jsonArrayTest, null);
    }

    @Test
    public void readMatrixTest() throws IOException {
        JsonArray jsonArray = new JsonArray();
        int[][] matrix = null;
        matrix = readMatrix(jsonArray);
        assertNotEquals(matrix, null);
    }
}