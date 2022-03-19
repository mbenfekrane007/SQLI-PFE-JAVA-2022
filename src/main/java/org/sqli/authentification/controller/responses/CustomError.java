package org.sqli.authentification.controller.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomError {
    private String error;
}
