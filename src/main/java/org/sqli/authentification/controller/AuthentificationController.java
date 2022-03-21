package org.sqli.authentification.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.dao.auth.AuthenticationRequest;
import org.sqli.authentification.dao.auth.AuthenticationOK;
import org.sqli.authentification.controller.responses.CustomError;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.services.AuthentificationService;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {

    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;
	
	
    @Autowired
    private AuthentificationService authentificationService;


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
                responseEntity = ResponseEntity.status(HttpStatus.LOCKED).body(CustomError.builder().error("You have reached 3 failed authentication attempts, your account will be disabled").build());
                authentificationService.lock(user.get());
                }else{
                    responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomError.builder().error("Authentication error").build());
                    authentificationService.increaseFailedAttempts(user.get());
                }
            }else{
                responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body(CustomError.builder().error("User disabled").build());
                authentificationService.increaseFailedAttempts(user.get());

            }

        }else {
            responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomError.builder().error("Users not Found !").build());
        }
        return responseEntity;

    }


}
/* return (!user.get().isEnabled())?ResponseEntity.ok(AuthentificationError.builder().error("User disabled").build()):(user.isPresent() && password.isPresent())?ResponseEntity.ok(AuthenticationOK.builder().id(user.get().getId()).login(user.get().getLogin()).group(user.get().getGroup_id().getName()).build())
        :ResponseEntity.ok(AuthentificationError.builder().error("Authentication error").build());*/