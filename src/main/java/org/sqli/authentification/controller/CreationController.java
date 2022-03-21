package org.sqli.authentification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sqli.authentification.dao.GroupRepository;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.dao.auth.AuthenticationOK;
import org.sqli.authentification.controller.responses.CustomError;
import org.sqli.authentification.dao.auth.AuthenticationRequest;
import org.sqli.authentification.dao.create.UserDto;
import org.sqli.authentification.dao.create.UserRequest;
import org.sqli.authentification.entitie.Group;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.controller.responses.MessageSuccess;
import org.sqli.authentification.services.CreationService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class CreationController {

    @Autowired
    CreationService creationService;

    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;

    @Autowired
    GroupRepository groupRepository;



    @PostMapping("/user")
    public ResponseEntity<?> saveUser(@RequestBody UserRequest userInput){
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
    public ResponseEntity<?> deleteUser(@PathVariable("login") String login, @RequestHeader Map<String,String> userAuth){
        if (userAuth.get("login").isEmpty() || userAuth.get("password").isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomError.builder().error("please provide user information for deleting").build());
        }
        Optional<User> user = userAuthentificationRepository.findByLoginAndPassword(userAuth.get("login"),userAuth.get("password"));

        if (user.isPresent()){
            if(user.get().getGroup_id().getName().equals("admin")){
                String deleteUser = creationService.delete(login);
                return (!deleteUser.equals("Error"))?ResponseEntity.ok(MessageSuccess.builder().success(deleteUser).build()):
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomError.builder().error("Admin can't delete him self").build());
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomError.builder().error("You are not authorized to delete "+login).build());
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomError.builder().error("Login "+ login + " is not found").build());
        }

    }
}
