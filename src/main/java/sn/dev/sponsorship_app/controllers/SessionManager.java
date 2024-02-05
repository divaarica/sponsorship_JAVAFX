package sn.dev.sponsorship_app.controllers;

import sn.dev.sponsorship_app.User;

public class SessionManager {

    private static SessionManager instance;
    private User loggedInUser;

    private SessionManager() {
        // Empêche l'instanciation directe depuis l'extérieur.
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }
}

