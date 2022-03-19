package org.sqli.authentification.entitie;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
public class AbstractEntity implements Serializable {



    @Id
    @SequenceGenerator(name = "pet_seq",
            sequenceName = "pet_sequence",
            initialValue = 4, allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_seq")
    private Integer id;
}
