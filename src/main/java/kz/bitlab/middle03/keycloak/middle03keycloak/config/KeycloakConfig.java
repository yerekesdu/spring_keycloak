package kz.bitlab.middle03.keycloak.middle03keycloak.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class KeycloakConfig {

    @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

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

    @Bean
    public Keycloak keycloakBek() {
        log.info("Creating Keycloak instance");
        return KeycloakBuilder
                .builder()
                .serverUrl(keycloakUrl)
                .realm(keycloakRealm)
                .clientId(keycloakClient)
                .clientSecret(keycloakClientSecret)
                .username(keycloakUsername)
                .password(keycloakPassword)
                .grantType(keycloakGrantType)
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
