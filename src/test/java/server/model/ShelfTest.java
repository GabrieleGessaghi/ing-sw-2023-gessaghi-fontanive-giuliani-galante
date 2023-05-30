package server.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.controller.utilities.ConfigLoader;
import server.model.cards.CommonCard;
import server.model.cards.CommonType;
import server.model.exceptions.IllegalColumnException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShelfTest {

    Shelf testShelf;

    @BeforeEach
    public void init() {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        testShelf = new Shelf();
    }

    @AfterEach
    public void teardown() {
        testShelf = null;
    }

    @Test
    public void constructorTest (){
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
