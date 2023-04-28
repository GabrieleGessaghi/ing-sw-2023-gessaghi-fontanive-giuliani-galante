package model;

public interface Saveable {
    public String getState();
    public void loadState(String jsonMessage);
}
