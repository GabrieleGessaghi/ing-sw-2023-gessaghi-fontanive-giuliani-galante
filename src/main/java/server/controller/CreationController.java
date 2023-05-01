package server.controller;

import com.google.gson.stream.JsonReader;
import server.controller.exceptions.TooManyPlayersException;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.model.Game;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Controller of the creation of the game.
 * @author Niccolò Giuliani
 */
public class CreationController implements Observer {
    private int playersNumber;
    private final ArrayList<String> playersNicknames;

    public CreationController(){
        playersNicknames = new ArrayList<String>();
        playersNumber = 0;
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
            }
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter of the state of the game (ready or not ready)
     * @return true if game is ready
     * @author Niccolò Giuliani
     */
    public boolean isGameReady(){
         return playersNicknames.size() == playersNumber;
    }

    /**
     * Checks whether there's only one player.
     * @return True if there's only one player connected.
     * @author Giorgio Massimo Fontanive
     */
    public boolean isThereOnlyOnePlayer() {
        return playersNicknames.size() == 1;
    }

    /**
     * method to create the Game
     * @return the Game created
     * @author Niccolò Giuliani
     */
    public Game createGame(){
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
        if (playersNumber == -1 || playersNicknames.size() < playersNumber)
            playersNicknames.add(nickname);
        else {
            //WARN CLIENTHANDLER
        }
    }
}
