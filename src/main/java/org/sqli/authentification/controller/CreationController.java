package org.sqli.authentification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sqli.authentification.dao.GroupRepository;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.dao.auth.AuthenticationOK;
import org.sqli.authentification.dao.auth.CustomError;
import org.sqli.authentification.dao.create.UserDto;
import org.sqli.authentification.dao.create.UserRequest;
import org.sqli.authentification.entitie.Group;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.services.impl.CreationServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class CreationController {

    @Autowired
    CreationServiceImpl creationService;

    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;

    @Autowired
    GroupRepository groupRepository;



    @PostMapping("/user")
    public ResponseEntity<?> saveUser(@RequestBody UserRequest userInput){
        //List<User> users = userAuthentificationRepository.findAll();
        //Integer id = users.size()+1;
        Optional<Group> group = groupRepository.findGroupByName(userInput.getGroup());
        if(group.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(CustomError.builder().error("Group ".concat(userInput.getGroup()).concat(" is not valid")).build());
        }
        if(!userInput.getPassword().equals(userInput.getPasswordConfirmation())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(CustomError.builder().error("passwords don't match!").build());
        }
        UserDto userDto = UserDto.builder()
                        .login(userInput.getLogin())
                .password(userInput.getPassword())
                                .enabled(true)
                                        .loginattempts(0)
                                                .group(group.get()).build();
        User user =  userDto.toEntity(userDto);
        String validUserSave = creationService.validateSavingUser(user);

        if(validUserSave.equals("not Valid") || validUserSave.equals("Login is not match")){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(CustomError.builder().error("Login ".concat(userInput.getLogin().concat(" is not valid"))).build());
        }else{
            return ResponseEntity.ok(AuthenticationOK.builder().id(user.getId())
                    .login(user.getLogin())
                    .group(user.getGroup_id().getName()).build());
        }

    }

    @DeleteMapping("/user/{login}")
    public ResponseEntity<?> deleteUser(@PathVariable("login") String login){

        if(userAuthentificationRepository.findByLogin(login).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomError.builder().error("Login "+ login + " is not found").build());
        }
        return ResponseEntity.ok(creationService.delete(login));
    }
}
