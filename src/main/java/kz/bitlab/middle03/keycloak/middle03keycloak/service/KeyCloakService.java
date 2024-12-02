package kz.bitlab.middle03.keycloak.middle03keycloak.service;

import kz.bitlab.middle03.keycloak.middle03keycloak.dto.UserCreateDto;
import kz.bitlab.middle03.keycloak.middle03keycloak.dto.UserSignInDto;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class KeyCloakService {

    private final Keycloak keycloak;

    private final RestTemplate restTemplate;

    public KeyCloakService(Keycloak keycloak, RestTemplate restTemplate) {
        this.keycloak = keycloak;
        this.restTemplate = restTemplate;
    }

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${keycloak.client}")
    private String keycloakClient;

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    @Value("${keycloak.username}")
    private String keycloakUsername;

    @Value("${keycloak.password}")
    private String keycloakPassword;

    @Value("${keycloak.grant-type}")
    private String keycloakGrantType;

    public UserRepresentation createUser(UserCreateDto user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(List.of(credentialRepresentation));

        Response response = keycloak
                .realm(keycloakRealm)
                .users()
                .create(userRepresentation);

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            log.error("Error on creating user");
            throw new RuntimeException("Failed to create user");
        }
        List<UserRepresentation> searchUser = keycloak
                .realm(keycloakRealm)
                .users()
                .search(user.getUserName());
        return searchUser.getFirst();
    }

    public String signIn(UserSignInDto userSignInDto) {

        String tokenEndPoint = keycloakUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", keycloakGrantType);
        formData.add("client_id", keycloakClient);
        formData.add("client_secret", keycloakClientSecret);
        formData.add("username", userSignInDto.getUserName());
        formData.add("password", userSignInDto.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate
                .postForEntity(tokenEndPoint, new HttpEntity<>(formData, headers), Map.class);
        Map<String, Object> responseBody = response.getBody();

        if(!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error on sign in");
            throw new RuntimeException("Failed to sign in");
        }
        return (String) responseBody.get("access_token");
    }

    public void changePassword(String userName, String newPassword) {
        List<UserRepresentation> users = keycloak
                .realm(keycloakRealm)
                .users()
                .search(userName);

        if (users.isEmpty()) {
            log.error("User not found to change");
            throw new RuntimeException("User not found with username " + userName);
        }

        UserRepresentation userRepresentation = users.getFirst();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(newPassword);
        credentialRepresentation.setTemporary(false);

        keycloak
                .realm(keycloakRealm)
                .users()
                .get(userRepresentation.getId())
                .resetPassword(credentialRepresentation);

        log.info("Password changed successfully");
    }
}
