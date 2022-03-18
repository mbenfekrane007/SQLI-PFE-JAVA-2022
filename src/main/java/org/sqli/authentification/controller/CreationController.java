package org.sqli.authentification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sqli.authentification.dao.UserAuthentificationRepository;
import org.sqli.authentification.dao.auth.AuthenticationOK;
import org.sqli.authentification.dao.auth.AuthentificationError;
import org.sqli.authentification.dao.create.GroupDto;
import org.sqli.authentification.dao.create.UserDto;
import org.sqli.authentification.entitie.User;
import org.sqli.authentification.services.AuthentificationService;
import org.sqli.authentification.services.impl.CreationServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class CreationController {

    @Autowired
    CreationServiceImpl creationService;

    @Autowired
    UserAuthentificationRepository userAuthentificationRepository;



    @PostMapping("/user")
    public ResponseEntity<?> saveUser(@RequestBody AuthenticationOK userInput){
        List<User> users = userAuthentificationRepository.findAll();
        Integer id = users.size()+1;
        System.out.println(id);
        GroupDto groupDto = GroupDto.builder().name(userInput.getGroup()).build();
        UserDto userDto = UserDto.builder().id(id)
                        .login(userInput.getLogin())
                                .enabled(true)
                                        .loginattempts(0)
                                                .group(groupDto.toEntity(groupDto)).build();
        //System.out.println(user);
        User user =  userDto.toEntity(userDto);
        System.out.println(user.getId());
        if(creationService.validateSavingUser(user).equals("not Valid")){
            return ResponseEntity.ok(AuthentificationError.builder().error("Group (group in input) is not valid").build());
        }
        return ResponseEntity.ok(AuthenticationOK.builder().id(user.getId())
                .login(user.getLogin())
                .group(user.getGroup_id().getName()).build());
    }
}
