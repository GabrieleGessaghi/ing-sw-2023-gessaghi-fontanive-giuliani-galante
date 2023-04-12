package model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static controller.Configurations.NUMBER_OF_TOKEN_TYPES;
import static controller.Configurations.TOKENS_PER_TYPE;

/**
 * Bag of tokens for the game's board.
 * @author Giorgio Massimo Fontanive
 */
public class Bag {
    private final List<Token> tokens;

    /**
     * The tokens array has 22 tokens of each type.
     * @author Giorgio Massimo Fontanive
     */
    public Bag() {
        tokens = new ArrayList<>();
        Token[] tokenIndexes = Token.values();
        for (int tokenType = 1; tokenType <= NUMBER_OF_TOKEN_TYPES; tokenType++)
            for (int i = 0; i < TOKENS_PER_TYPE; i++)
                tokens.add(tokenIndexes[tokenType]);
        shuffle();
    }

    /**
     * Shuffles the tokens in the bag.
     * @author Giorgio Massimo Fontanive
     */
    private void shuffle() {
        Collections.shuffle(tokens);
    }

    /**
     * Removes and returns the first token in the bag.
     * @author Giorgio Massimo Fontanive
     * @return The first token in the bag's list;
     */
    public Token drawToken() {
        return tokens.remove(0);
    }
}
