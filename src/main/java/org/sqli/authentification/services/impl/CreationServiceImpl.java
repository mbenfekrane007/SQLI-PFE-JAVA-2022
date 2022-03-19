package org.sqli.authentification.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sqli.authentification.dao.GroupRepository;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.services.CreationService;

import javax.transaction.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional
public class CreationServiceImpl implements CreationService {



    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;


    @Override
    public String validateSavingUser(User user) {
        Pattern pattern = Pattern.compile("^[a-z][a-zA-Z_0-9]{3,7}$");
        Matcher matcher = pattern.matcher(user.getLogin());
        if (userAuthentificationRepository.findByLogin(user.getLogin()).isEmpty()){
            if (matcher.find()){
                userAuthentificationRepository.save(user);
                return "Valid";
            }
            return "Login is not match";
        }
        return "not Valid";
    }

    @Override
    public String delete(String login) {
        User user = userAuthentificationRepository.findByLogin(login).get();
        if (user.getGroup_id().getName().equals("admin")){
            return "Error";
        }
        userAuthentificationRepository.delete(user);
        return "Login " +login+" is deleted";
    }
}
