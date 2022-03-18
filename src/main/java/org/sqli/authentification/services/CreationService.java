package org.sqli.authentification.services;

import org.sqli.authentification.entitie.User;

public interface CreationService {

    public String validateSavingUser(User user);

    public String delete(String login);
}
