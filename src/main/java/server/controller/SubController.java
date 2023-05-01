package server.controller;

public abstract class SubController {
    private String lastError;

    public String getLastError() {
        String error = lastError;
        lastError = null;
        return error;
    }

}
