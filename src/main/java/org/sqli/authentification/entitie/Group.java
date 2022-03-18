package org.sqli.authentification.entitie;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user_group")
public class Group extends AbstractEntity{


    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "group_id", fetch = FetchType.EAGER)
    private List<User> users;


}
