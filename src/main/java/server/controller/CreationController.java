package server.controller;

import com.google.gson.stream.JsonReader;
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

    /**
     * Class constructor
     * @author Niccolò Giuliani
     */
    public CreationController(){
        playersNicknames = new ArrayList<>();
        playersNumber = -1;
    }

    @Override
    public void update(Event event) {
        int lastClientIndex = -1;
        String lastClientNickname = null;
        String jsonMessage = event.jsonMessage();
        String field;
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "playersNumber" -> setPlayerNumber(jsonReader.nextInt());
                    case "nickname" -> lastClientNickname = addPlayer(jsonReader.nextString());
                    case "index" -> lastClientIndex = jsonReader.nextInt();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            //Updates the client handler's nickname
            if (lastClientNickname != null && Controller.findClientHandler(lastClientIndex) != null)
                Controller.findClientHandler(lastClientIndex).nickname = lastClientNickname;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter of the state of the game (ready or not ready).
     * @return True if game is ready.
     * @author Niccolò Giuliani
     */
    public boolean isGameReady(){
         return playersNicknames.size() == playersNumber;
    }

    /**
     * Checks whether there's a spot available.
     * @return True if there's space for more players.
     * @author Giorgio Massimo Fontanive
     */
    public boolean isSpotAvailable() {
        return playersNumber == -1 || playersNicknames.size() < playersNumber;
    }

    /**
     * Method to create the Game.
     * @return The Game created.
     * @author Niccolò Giuliani
     */
    public Game createGame(){
        return new Game(playersNumber, playersNicknames);
    }

    /**
     * Set the number of the players.
     * @param playersNumber Number of players.
     * @author Niccolò Giuliani
     */
    private void setPlayerNumber(int playersNumber){
        this.playersNumber = playersNumber;
    }

    /**
     * Method to add a player and avoid name duplicates.
     * @param nickname Nickname of the player to add.
     * @author Giorgio Massimo Fontanive
     */
    private String addPlayer(String nickname) {
        String newNickname;
        if (!playersNicknames.contains(nickname))
            newNickname = nickname;
        else {
            int i = 0;
            for (String s : playersNicknames)
                if (s.equals(nickname))
                    i++;
            newNickname = nickname + i;
        }
        playersNicknames.add(newNickname);
        return newNickname;
    }
}
