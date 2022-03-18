package org.sqli.authentification.dao.auth;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationOK {

    private Integer id;
    private String login;
    private String group;

}

