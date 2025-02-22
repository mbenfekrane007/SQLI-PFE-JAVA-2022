package org.sqli.authentification.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.services.AuthentificationService;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthentificationServiceImpl implements AuthentificationService {

    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;

    ;

    @Override
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getLoginattempts()+1;
        userAuthentificationRepository.updateFailedAttempts(newFailAttempts, user.getLogin());
    }

    @Override
    public void resetFailedAttempts(String login) {
        userAuthentificationRepository.updateFailedAttempts(0,login);

    }

    @Override
    public void lock(User user) {
        user.setEnabled(false);
        userAuthentificationRepository.save(user);
    }
}
