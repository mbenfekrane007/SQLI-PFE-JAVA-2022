package org.sqli.authentification.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.dao.auth.AuthenticationRequest;
import org.sqli.authentification.dao.auth.AuthenticationOK;
import org.sqli.authentification.dao.auth.AuthentificationError;
import org.sqli.authentification.entitie.User;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {

    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;

    @PostMapping("/auth")
    public ResponseEntity<?> authUser(@RequestBody AuthenticationRequest authenticationRequest){
        Optional<User> user = userAuthentificationRepository.findByLogin(authenticationRequest.getLogin());
        Optional<User> password = userAuthentificationRepository.findUserByPassword(authenticationRequest.getPassword());
        return (!user.get().isEnabled())?
                ResponseEntity.ok(AuthentificationError.builder().error("User disabled")):(user.isPresent() && password.isPresent())
                ?ResponseEntity.ok(AuthenticationOK.builder().id(user.get().getId()).login(user.get().getLogin()).group(user.get().getGroup_id().getName()).build())
        :ResponseEntity.ok(AuthentificationError.builder().error("Authentication error").build());
    }
}
