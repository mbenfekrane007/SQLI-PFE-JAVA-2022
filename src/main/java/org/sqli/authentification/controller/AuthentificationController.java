package org.sqli.authentification.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.dao.auth.AuthenticationRequest;
import org.sqli.authentification.dao.auth.AuthenticationOK;
import org.sqli.authentification.dao.auth.AuthentificationError;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.service.impl.AuthentificationServiceImpl;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {

    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;
    @Autowired
    private AuthentificationServiceImpl authentificationService;

    private ResponseEntity<?> responseEntity;

    @PostMapping("/auth")
    public ResponseEntity<?> authUser(@RequestBody AuthenticationRequest authenticationRequest){
        Optional<User> user = userAuthentificationRepository.findByLogin(authenticationRequest.getLogin());

        if(user.isPresent()){
            if(user.get().isEnabled()) {
                if (user.get().getLogin().equals(authenticationRequest.getLogin()) && user.get().getPassword().equals(authenticationRequest.getPassword())) {
                    responseEntity = ResponseEntity.ok(AuthenticationOK.builder()
                            .id(user.get().getId())
                            .login(user.get().getLogin())
                            .group(user.get().getGroup_id().getName()).build());
                }else if(user.get().getLoginattempts()==AuthentificationServiceImpl.MAX_FAILED_ATTEMPTS){
                responseEntity = ResponseEntity.ok(AuthentificationError.builder().error("You have reached 3 failed authentication attempts, your account will be disabled").build());
                authentificationService.lock(user.get());
                }else{
                    responseEntity = ResponseEntity.ok(AuthentificationError.builder().error("Authentication error").build());
                    authentificationService.increaseFailedAttempts(user.get());
                }
            }else{
                System.out.println(user.get().getLogin());
                responseEntity = ResponseEntity.ok(AuthentificationError.builder().error("User disabled").build());

            }

        }else {
            responseEntity = ResponseEntity.ok(AuthentificationError.builder().error("Users not Found !").build());
        }
        return responseEntity;

    }
}
/* return (!user.get().isEnabled())?ResponseEntity.ok(AuthentificationError.builder().error("User disabled").build()):(user.isPresent() && password.isPresent())?ResponseEntity.ok(AuthenticationOK.builder().id(user.get().getId()).login(user.get().getLogin()).group(user.get().getGroup_id().getName()).build())
        :ResponseEntity.ok(AuthentificationError.builder().error("Authentication error").build());*/