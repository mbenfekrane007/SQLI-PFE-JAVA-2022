package org.sqli.authentification.dao.create;


import lombok.Builder;
import lombok.Data;
import org.sqli.authentification.entitie.Group;
import org.sqli.authentification.entitie.User;

import java.util.List;

@Builder
@Data
public class GroupDto {

    private Integer id;
    private String name;
    private List<User> users;

    public Group toEntity(GroupDto groupDto){
        Group group = new Group();
        group.setId(groupDto.getId());
        group.setName(groupDto.getName());
        group.setUsers(groupDto.getUsers());
        return group;
    }
}
