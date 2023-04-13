package controller;

import com.google.gson.stream.JsonReader;
import controller.exceptions.TooManyPlayersException;
import model.Game;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static controller.Configurations.readMatrix;


/**
 * Controller of the creation of the game
 * @author Niccolò Giuliani
 */
public class CreationController implements Observer {
    private int playersNumber;
    private ArrayList<String> playersNicknames;
    private boolean isGameReady;


    public CreationController(){
        playersNicknames = new ArrayList<String>();
        int playersNumber = 0;
        isGameReady = false;
    }
    @Override
    public void update(Event event) {
        String jsonMessage = event.getJsonMessage();
        String field;
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {

                        case "playersNumber" -> setPlayerNumber(jsonReader.nextInt());
                        case "playersNickname" -> addPlayer(jsonReader.nextString());

                }
                if(playersNumber < playersNicknames.size())
                    throw new TooManyPlayersException("Too many players trying to play the game");
            }
            jsonReader.endObject();
        } catch (IOException | TooManyPlayersException e) {
            throw new RuntimeException(e);
        }
        isGameReady = playersNicknames.size() == playersNumber;

    }

    /**
     * getter of the state of the game (ready or not ready)
     * @return true if game is ready
     * @author Niccolò Giuliani
     */
    public boolean getIsGameReady(){
         return isGameReady;
    }

    /**
     * method to create the Game
     * @return the Game created
     * @author Niccolò Giuliani
     */
    public  Game createGame(){
        return new Game(playersNumber, playersNicknames);
    }

    /**
     * set the number of the players
     * @param playersNumber number of players
     * @author Niccolò Giuliani
     */
    private void setPlayerNumber(int playersNumber){
        this.playersNumber = playersNumber;
    }

    /**
     * method to add a player
     * @param nickname nickname of the player to add
     * @author Niccolò Giuliani
     */
    private void addPlayer(String nickname) {
            playersNicknames.add(nickname);
    }
}
