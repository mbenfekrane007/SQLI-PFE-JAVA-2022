package org.sqli.authentification.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sqli.authentification.dao.GroupRepository;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.services.CreationService;

import javax.transaction.Transactional;


@Service
@Transactional
public class CreationServiceImpl implements CreationService {



    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;


    @Override
    public String validateSavingUser(User user) {
        if (userAuthentificationRepository.findByLogin(user.getLogin()).isEmpty()){
            userAuthentificationRepository.save(user);
            return "Valid";
        }
        return "not Valid";
    }

    @Override
    public String delete(String login) {
        if(userAuthentificationRepository.findByLogin(login).isEmpty()){
            return "Login (login in input) is not found";
        }
        userAuthentificationRepository.delete(userAuthentificationRepository.findByLogin(login).get());
        return "Login (login in input) is deleted";
    }
}
