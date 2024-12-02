package kz.bitlab.middle03.keycloak.middle03keycloak.api;

import kz.bitlab.middle03.keycloak.middle03keycloak.dto.UserChangePasswordDto;
import kz.bitlab.middle03.keycloak.middle03keycloak.dto.UserCreateDto;
import kz.bitlab.middle03.keycloak.middle03keycloak.dto.UserSignInDto;
import kz.bitlab.middle03.keycloak.middle03keycloak.service.KeyCloakService;
import kz.bitlab.middle03.keycloak.middle03keycloak.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final KeyCloakService keyCloakService;

    public UserController(KeyCloakService keyCloakService) {
        this.keyCloakService = keyCloakService;
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto user) {
        keyCloakService.createUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/sign-in")
    public String signIn(@RequestBody UserSignInDto user) {
        return keyCloakService.signIn(user);
    }

    @PostMapping(value = "/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordDto userChangePasswordDto) {

        String currentUserName = UserUtils.getCurrentUserLogin();
        System.out.println("username CURRENT: " + currentUserName);
        if (currentUserName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Couldn't find current user");
        }
        try {

            keyCloakService.changePassword(currentUserName, userChangePasswordDto.getNewPassword());
            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
