package kz.bitlab.middle03.keycloak.middle03keycloak.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInDto {
    private String userName;
    private String password;
}
