package client;

import client.gui.GUI;
import client.tui.TUI;

import java.util.Scanner;

/**
 * Starts the requested client.
 */
public class GameClient {

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        String response;
        System.out.println("Do you want to play with a TUI or with a GUI?");
        do {
            response = scn.nextLine();
        } while (response.charAt(0) != 'T' && response.charAt(0) != 'G');
        if (response.charAt(0) == 'T') {
            TUI.startGame();
        }
        if (response.charAt(0) == 'G') {
            GUI.startGame(args);
        }
    }
}
