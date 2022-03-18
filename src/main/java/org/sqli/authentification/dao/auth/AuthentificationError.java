package org.sqli.authentification.dao.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthentificationError {
    private String error;
}
