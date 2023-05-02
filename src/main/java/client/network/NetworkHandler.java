package client.network;

public abstract class NetworkHandler implements Runnable {

    @Override
    public abstract void run();

    public abstract void sendInput(String input);
}
