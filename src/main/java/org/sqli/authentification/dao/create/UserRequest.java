package org.sqli.authentification.dao.create;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String login;
    private String password;
    private String group;
}
