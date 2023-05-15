package client.tui;

import client.Client;
import client.network.NetworkHandler;
import client.network.NetworkHandlerRMI;
import client.network.NetworkHandlerTCP;
import server.controller.utilities.ConfigLoader;

import java.util.Scanner;

import static client.tui.Colors.COLOUR_RESET;

public class TUI {

    public static void main(String[] args) {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        NetworkHandler networkHandler;
        String hostIp;
        String nickname;
        int selection;
        Scanner scn = new Scanner(System.in);

        printOpening();

        System.out.print("Insert nickname: ");
        nickname = scn.nextLine();

        System.out.print("Insert host's IP address: ");
        hostIp = scn.nextLine();

        System.out.print("Select connection type (0 TCP/1 RMI): ");
        selection = scn.nextInt();
        while(selection !=0 && selection != 1) {
            System.out.print("Connection type not valid!\n");
            System.out.print("Select connection type (0 TCP/1 RMI): ");
            selection = scn.nextInt();
        }

        Client client = new ClientTUI(nickname);
        if(selection == 0)
            networkHandler = new NetworkHandlerTCP();
        else
            networkHandler = new NetworkHandlerRMI();
        networkHandler.setClient(client);
        networkHandler.setHost(hostIp);
        new Thread(networkHandler).start();
        client.setNetworkHandler(networkHandler);
    }

    /**
     * Prints opening screen.
     * @author Niccolò Galante
     */
    private static void printOpening(){
        System.out.print("\033[0;1m" + "\n" +
                "                               ___             ___  .-.                \n" +
                "                              (   )           (   )/    \\  .-.         \n" +
                " ___ .-. .-.  ___  ___   .--.  | | .-.   .--.  | | | .`. ;( __) .--.   \n" +
                "(   )   '   \\(   )(   )/  _  \\ | |/   \\ /    \\ | | | |(___|''\")/    \\  \n" +
                " |  .-.  .-. ;| |  | |. .' `. ;|  .-. .|  .-. ;| | | |_    | ||  .-. ; \n" +
                " | |  | |  | || |  | || '   | || |  | ||  | | || |(   __)  | ||  | | | \n" +
                " | |  | |  | || '  | |_\\_`.(___) |  | ||  |/  || | | |     | ||  |/  | \n" +
                " | |  | |  | |'  `-' (   ). '. | |  | ||  ' _.'| | | |     | ||  ' _.' \n" +
                " | |  | |  | | `.__. || |  `\\ || |  | ||  .'.-.| | | |     | ||  .'.-. \n" +
                " | |  | |  | | ___ | |; '._,' '| |  | |'  `-' /| | | |     | |'  `-' / \n" +
                "(___)(___)(___|   )' | '.___.'(___)(___)`.__.'(___|___)   (___)`.__.'  \n" +
                "               ; `-' '                                                 \n" +
                "                .__.'                                                  \n" +
                COLOUR_RESET
        );
        System.out.print("\n\n");
        System.out.print("\033[0;1m" + "                              WELCOME!\n\n\n" + COLOUR_RESET);

    }
}
