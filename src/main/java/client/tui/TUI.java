package client.tui;

import client.network.NetworkHandler;
import client.network.NetworkHandlerRMI;
import client.network.NetworkHandlerTCP;
import server.controller.utilities.ConfigLoader;

import java.util.Scanner;

public class TUI {
    public static void main(String[] args) {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        NetworkHandler networkHandler;
        String hostIp;
        String nickname;
        int selection;
        Scanner scn = new Scanner(System.in);

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

        ClientTUI client = new ClientTUI(nickname);
        if(selection == 0)
            networkHandler = new NetworkHandlerTCP(client, hostIp);
        else
            networkHandler = new NetworkHandlerRMI(client, hostIp);
        new Thread(networkHandler).start();
        client.setNetworkHandler(networkHandler);
    }
}
