package org.sqli.authentification.dao.create;

import lombok.Builder;
import lombok.Data;
import org.sqli.authentification.entitie.Group;
import org.sqli.authentification.entitie.User;

@Builder
@Data
public class UserDto {
    private Integer id;
    private String login;
    private Group group;
    private boolean enabled;
    private Integer loginattempts;

    public User toEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        user.setEnabled(userDto.isEnabled());
        user.setLoginattempts(userDto.getLoginattempts());
        user.setGroup_id(userDto.getGroup());

        return user;
    }

}
