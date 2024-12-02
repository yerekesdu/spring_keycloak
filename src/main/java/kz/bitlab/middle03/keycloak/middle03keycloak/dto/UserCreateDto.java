package kz.bitlab.middle03.keycloak.middle03keycloak.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserCreateDto {

    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
}
