package org.sqli.authentification.service;

import org.sqli.authentification.entitie.User;

public interface AuthentificationService {
    public void increaseFailedAttempts(User user);

    public void resetFailedAttempts(String login);

    public void lock(User user);

}
