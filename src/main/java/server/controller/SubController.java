package server.controller;

import server.controller.observer.Observer;

public abstract class SubController implements Observer {
    protected String lastError;

    public String getLastError() {
        String error = lastError;
        lastError = null;
        return error;
    }
}
