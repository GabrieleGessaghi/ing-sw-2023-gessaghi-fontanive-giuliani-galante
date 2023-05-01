package server.model;

import server.controller.utilities.ConfigLoader;
import server.model.exceptions.IllegalColumnException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShelfTest {

    @Test
    public void constructorTest (){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Shelf testShelf = new Shelf();
        Token[][] generatedTiles = testShelf.getTiles();
        for (Token[] generatedTile : generatedTiles) {
            for (Token token : generatedTile) {
                assertEquals(Token.NOTHING, token);
                assertNotEquals(Token.TOY, token);
            }
        }
    }

    @Test
    public void insertionTest () {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Shelf testShelf = new Shelf();
        assertEquals(Token.NOTHING, testShelf.getTiles()[5][0]);
        try {
            testShelf.insertToken(Token.TOY, 0);
            assertEquals(Token.TOY, testShelf.getTiles()[5][0]);
        }catch (IllegalColumnException e){
            System.out.println(e);
        }
    }

    @Test
    public void removeTest (){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Shelf testShelf = new Shelf();
        try {
            testShelf.insertToken(Token.TOY, 0);
        }catch (IllegalColumnException e){
            System.out.println(e);
        }
        testShelf.removeToken(0);
        assertEquals(Token.NOTHING, testShelf.getTiles()[5][0]);
    }

    @Test
    public void isFullTest () throws IllegalColumnException {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Shelf testShelf = new Shelf();

        testShelf.insertToken(Token.TOY,0);
        testShelf.insertToken(Token.TOY,0);
        testShelf.insertToken(Token.TOY,0);
        testShelf.insertToken(Token.TOY,0);
        testShelf.insertToken(Token.TOY,0);
        testShelf.insertToken(Token.TOY,0);
        testShelf.insertToken(Token.TOY,1);
        testShelf.insertToken(Token.TOY,1);
        testShelf.insertToken(Token.TOY,1);
        testShelf.insertToken(Token.TOY,1);
        testShelf.insertToken(Token.TOY,1);
        testShelf.insertToken(Token.TOY,1);
        testShelf.insertToken(Token.TOY,2);
        testShelf.insertToken(Token.TOY,2);
        testShelf.insertToken(Token.TOY,2);
        testShelf.insertToken(Token.TOY,2);
        testShelf.insertToken(Token.TOY,2);
        testShelf.insertToken(Token.TOY,2);
        testShelf.insertToken(Token.TOY,3);
        testShelf.insertToken(Token.TOY,3);
        testShelf.insertToken(Token.TOY,3);
        testShelf.insertToken(Token.TOY,3);
        testShelf.insertToken(Token.TOY,3);
        testShelf.insertToken(Token.TOY,3);
        testShelf.insertToken(Token.TOY,4);
        testShelf.insertToken(Token.TOY,4);
        testShelf.insertToken(Token.TOY,4);
        testShelf.insertToken(Token.TOY,4);
        testShelf.insertToken(Token.TOY,4);
        testShelf.insertToken(Token.TOY,4);

        assertTrue(testShelf.isFull());
    }

}
