package org.sqli.authentification.services;

import org.sqli.authentification.entitie.User;

public interface AuthentificationService {

    public static final int MAX_FAILED_ATTEMPTS = 3;
    public void increaseFailedAttempts(User user);

    public void resetFailedAttempts(String login);

    public void lock(User user);

}
