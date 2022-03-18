package org.sqli.authentification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sqli.authentification.dao.GroupRepository;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.dao.auth.AuthenticationOK;
import org.sqli.authentification.dao.auth.AuthentificationError;
import org.sqli.authentification.dao.create.GroupDto;
import org.sqli.authentification.dao.create.UserDto;
import org.sqli.authentification.dao.create.UserRequest;
import org.sqli.authentification.entitie.Group;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.services.AuthentificationService;
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
        List<User> users = userAuthentificationRepository.findAll();
        Integer id = users.size()+1;
        Optional<Group> group = groupRepository.findGroupByName(userInput.getGroup());
        if(group.isEmpty()){
            return ResponseEntity.ok(AuthentificationError.builder().error("Group (group in input) is not valid").build());
        }
        UserDto userDto = UserDto.builder().id(id)
                        .login(userInput.getLogin())
                .password(userInput.getPassword())
                                .enabled(true)
                                        .loginattempts(0)
                                                .group(group.get()).build();
        User user =  userDto.toEntity(userDto);

        if(creationService.validateSavingUser(user).equals("not Valid")){
            System.out.println(creationService.validateSavingUser(user));
            return ResponseEntity.ok(AuthentificationError.builder().error("Login (login in input) is not valid").build());
        }else{
            return ResponseEntity.ok(AuthenticationOK.builder().id(user.getId())
                    .login(user.getLogin())
                    .group(user.getGroup_id().getName()).build());
        }

    }
}
