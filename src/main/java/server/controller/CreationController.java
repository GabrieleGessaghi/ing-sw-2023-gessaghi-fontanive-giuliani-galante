package server.controller;

import com.google.gson.stream.JsonReader;
import server.Server;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.view.ClientHandler;

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
                    case "nickname" -> lastClientNickname = jsonReader.nextString();
                    case "index" -> lastClientIndex = jsonReader.nextInt();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            //Updates the client handler's nickname

            ClientHandler clientHandler = Controller.findClientHandler(lastClientIndex);
            if (lastClientNickname != null && clientHandler != null) {
                clientHandler.nickname = addPlayer(lastClientNickname);
                if (!lastClientNickname.equals(clientHandler.nickname))
                    clientHandler.sendOutput(JsonTools.createMessage("Duplicate name, yours is now " + clientHandler.nickname));
            }

            //Checks if the client was previously disconnected, otherwise deletes it
            if (!isSpotAvailable() && !Server.disconnectedClients.isEmpty())
                if (Server.disconnectedClients.containsKey(lastClientNickname) && clientHandler != null) {
                    clientHandler.index = Server.disconnectedClients.get(lastClientNickname);
                    Server.disconnectedClients.remove(lastClientNickname);
                } else {
                    Controller.clientHandlers.remove(clientHandler);
                }

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
        if (!playersNicknames.contains(nickname) || Server.disconnectedClients.containsKey(nickname))
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
